<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Order;
use App\Models\Product;
use App\Models\User;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

class OrderController extends Controller
{

    // documentamos con swagger
    /**
     * @OA\Post(
     *    path="/api/order",
     *   summary="Crear un pedido",
     *  tags={"Pedido"},
     *
     *  @OA\Parameter(
     *     name="products",
     *    in="query",
     *   description="Lista de productos",
     * required=true,
     *  @OA\Schema(
     *        type="string",
     *   ),
     * ),
     *
     * @OA\Parameter(
     *    name="delivery_location_id",
     *  in="query",
     *  description="Id de la ubicación de entrega",
     * required=true,
     * @OA\Schema(
     *       type="integer",
     * ),
     * ),
     *
     * @OA\Parameter(
     *   name="type_delivery",
     * in="query",
     * description="Tipo de pago 1 = efectivo, 2 = tarjeta",
     * required=true,
     * @OA\Schema(
     *      type="integer",
     * ),
     * ),
     *
     * @OA\Response(
     *   response=200,
     * description="Pedido creado correctamente",
     * @OA\JsonContent(
     *     @OA\Property(property="success", type="boolean", example=true),
     *   @OA\Property(property="message", type="string", example="Pedido creado correctamente"),
     * ),
     * ),
     *
     * @OA\Response(
     *  response=400,
     * description="Error al crear el pedido",
     * @OA\JsonContent(
     *   @OA\Property(property="success", type="boolean", example=false),
     * @OA\Property(property="message", type="string", example="Error al crear el pedido"),
     * ),
     * ),
     *
     * @OA\Response(
     * response=401,
     * description="No autorizado",
     * @OA\JsonContent(
     *  @OA\Property(property="success", type="boolean", example=false),
     * @OA\Property(property="message", type="string", example="No autorizado"),
     * ),
     * ),
     *
     * @OA\Response(
     * response=500,
     * description="Error interno del servidor",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example=false),
     * @OA\Property(property="message", type="string", example="Error interno del servidor"),
     * ),
     * ),
     *
     * security={
     * {"bearer": {}}
     * }
     * )
     */
    public function store(Request $request)
    {

        $validate = validateRequestParams($request, [
            'products' => 'required|string',
            'delivery_location_id' => 'required|integer',
            'type_delivery' => 'required|integer', // tipo de pago 1 = efectivo, 2 = tarjeta
        ]);

        if (!$validate['success'])
            return validateResponseError($validate);

        $order = new Order();
        $order->user_id = Auth::user()->id;
        $order->order_number = uniqid();
        $order->delivery_location_id = $request->delivery_location_id;
        $order->type_delivery = $request->type_delivery;
        $order->save();

        $total_products = 0;
        $total_price = 0;
        $total_shipping = 0;

        // convertir el string a un array
        $array_products = json_decode($request->products, true);


        foreach ($array_products as $product) {

            // buscamos el producto en la base de datos
            $product_db = Product::find($product);

            if ($product_db == null) {
                return response()->json([
                    'success' => false,
                    'message' => 'No se encontró el producto con id: ' . $product
                ]);
            }

            $total_products += 1;
            $total_price += $product_db->price;
            $total_shipping++;

            DB::table('order_products')->insert([
                'order_id' => $order->id,
                'product_id' => $product_db->id,
            ]);
        }


        $order->total_products = $total_products;
        $order->cost = $total_price;
        $order->save();

        return response()->json([
            'success' => true,
            'message' => 'Tu pedido se ha realizado con éxito, puedes verlo en la sección de pedidos dentro de tu perfil.'
        ]);
    }

    /**
     * @OA\Get(
     *     path="/api/orders",
     *     summary="Retrieve orders",
     *     description="Retrieves a list of orders based on certain criteria.",
     *     @OA\Parameter(
     *         name="is_deliver",
     *         in="query",
     *         description="Filter orders by deliverer (true/false)",
     *         required=false,
     *         @OA\Schema(type="string")
     *     ),
     *     @OA\Response(
     *         response="200",
     *         description="A list of orders",
     *         @OA\JsonContent(
     *             example={"success": true, "data": {{"id": 1, "type_pay": "Efectivo", "status_text": "Nuevo"}, {"id": 2, "type_pay": "Tarjeta", "status_text": "En camino"}}}
     *         )
     *     ),
     *     @OA\Response(
     *         response="500",
     *         description="Internal Server Error",
     *         @OA\JsonContent(
     *             example={"error": "Internal Server Error"}
     *         )
     *     )
     * )
     */
    public function index(Request $request)
    {

        // obtenemos el usuario
        $user = 1;

        // buscamos el usuario en la base de datos

        $user = User::find($user);

        $orders = Order::select(
            'orders.*',
            DB::raw('
                CASE
                    WHEN orders.type_delivery = 1 THEN "Efectivo"
                    WHEN orders.type_delivery = 2 THEN "Tarjeta"
                END as type_pay
            '),
            DB::raw(
                '
                CASE
                    WHEN orders.status = 0 THEN "Nuevo"
                    WHEN orders.status = 1 THEN "En camino"
                    WHEN orders.status = 2 THEN "Entregado"
                    WHEN orders.status = -1 THEN "Cancelado"
                END as status_text'
            )
        );
        if ($request->is_deliver == "true") {
            $orders->where('orders.deliver_id', $user->id);
        } else
        if ($user->is_admin == 0) {
            $orders->where('orders.user_id', $user->id);
        } else {
            $orders->where('orders.status', '=', 0);
        }

        return response()->json([
            'success' => true,
            'data' => $orders->get()
        ]);
    }

    /**
/**
     * @OA\Get(
     *     path="/api/orders/{id}",
     *     summary="Get order details",
     *     description="Retrieves detailed information about a specific order.",
     *     @OA\Parameter(
     *         name="id",
     *         in="path",
     *         description="ID of the order",
     *         required=true,
     *         @OA\Schema(type="integer")
     *     ),
     *     @OA\Response(
     *         response="200",
     *         description="Order details",
     *         @OA\JsonContent(
     *             example={"success": true, "data": {"id": 1, "type_pay": "Efectivo", "status_text": "Nuevo", "products": "#/components/schemas/products", "delivery_location": "#/components/schemas/delivery_location", "user": "#/components/schemas/user", "deliver": "#/components/schemas/deliver"}}
     *         )
     *     ),
     *     @OA\Response(
     *         response="404",
     *         description="Order not found",
     *         @OA\JsonContent(
     *             example={"error": "Order not found"}
     *         )
     *     ),
     *     @OA\Components(
     *         @OA\Schema(
     *             schema="products",
     *             type="array",
     *             description="List of products",
     *             @OA\Items(type="object", properties={"id": {"type": "integer"}, "name": {"type": "string"}, "price": {"type": "number"}})
     *         ),
     *         @OA\Schema(
     *             schema="delivery_location",
     *             type="object",
     *             description="Delivery location",
     *             properties={"id": {"type": "integer"}, }
     *         ),
     *         @OA\Schema(
     *             schema="user",
     *             type="object",
     *             description="User details",
     *             properties={"id": {"type": "integer"},}
     *         ),
     *         @OA\Schema(
     *             schema="deliver",
     *             type="object",
     *             description="Deliver details",
     *             properties={"id": {"type": "integer"}, }
     *         )
     *     )
     * )
     */
    public function getDetail(Request $request, $id)
    {

        $order = Order::select(
            'orders.*',
            DB::raw('
                CASE
                    WHEN orders.type_delivery = 1 THEN "Efectivo"
                    WHEN orders.type_delivery = 2 THEN "Tarjeta"
                END as type_pay
            '),
            DB::raw(
                '
                CASE
                    WHEN orders.status = 0 THEN "Nuevo"
                    WHEN orders.status = 1 THEN "En camino"
                    WHEN orders.status = 2 THEN "Entregado"
                    WHEN orders.status = -1 THEN "Cancelado"
                END as status_text'
            )
        )
            ->where('orders.id', $id)
            ->first();

        $order->products = DB::table('order_products')
            ->select(
                'order_products.*',
                'products.name',
                'products.price',
            )
            ->join('products', 'products.id', '=', 'order_products.product_id')
            ->where('order_products.order_id', $id)
            ->get();

        $order->delivery_location = DB::table('delivery_locations')
            ->select(
                'delivery_locations.*',
            )
            ->where('delivery_locations.id', $order->delivery_location_id)
            ->first();

        $order->user = DB::table('users')
            ->select(
                'users.*',
            )
            ->where('users.id', $order->user_id)
            ->first();

        $order->deliver = DB::table('users')
            ->select(
                'users.*',
            )
            ->where('users.id', $order->deliver_id)
            ->first();

        return response()->json([
            'success' => true,
            'data' => $order
        ]);
    }
    /**
     * @OA\Put(
     *     path="/api/orders/{id}/status",
     *     summary="Change order status",
     *     description="Updates the status of a specific order.",
     *     @OA\Parameter(
     *         name="id",
     *         in="path",
     *         description="ID of the order",
     *         required=true,
     *         @OA\Schema(type="integer")
     *     ),
     *     @OA\RequestBody(
     *         required=true,
     *         description="JSON object containing the new status",
     *         @OA\JsonContent(
     *             required={"status"},
     *             @OA\Property(property="status", type="integer", description="New status code"),
     *         )
     *     ),
     *     @OA\Response(
     *         response="200",
     *         description="Order status updated successfully",
     *         @OA\JsonContent(
     *             example={"success": true, "message": "Se ha actualizado el estado del pedido"}
     *         )
     *     ),
     *     @OA\Response(
     *         response="400",
     *         description="Validation error",
     *         @OA\JsonContent(
     *             example={"success": false, "message": "Validation error details"}
     *         )
     *     ),
     *     @OA\Response(
     *         response="404",
     *         description="Order not found",
     *         @OA\JsonContent(
     *             example={"success": false, "message": "No se encontró el pedido"}
     *         )
     *     )
     * )
     */
    public function changeStatus(Request $request, $id)
    {

        $validate = validateRequestParams($request, [
            'status' => 'required|integer',
        ]);

        if (!$validate['success'])
            return validateResponseError($validate);

        $order = Order::find($id);

        if ($order == null) {
            return response()->json([
                'success' => false,
                'message' => 'No se encontró el pedido'
            ]);
        }

        $order->status = $request->status;
        $order->save();

        return response()->json([
            'success' => true,
            'message' => 'Se ha actualizado el estado del pedido'
        ]);
    }

    /**
     * @OA\Put(
     *     path="/api/orders/{id}/assign-delivery",
     *     summary="Assign delivery to an order",
     *     description="Assigns a delivery person to a specific order.",
     *     @OA\Parameter(
     *         name="id",
     *         in="path",
     *         description="ID of the order",
     *         required=true,
     *         @OA\Schema(type="integer")
     *     ),
     *     @OA\RequestBody(
     *         required=true,
     *         description="JSON object containing the delivery person ID",
     *         @OA\JsonContent(
     *             required={"deliver_id"},
     *             @OA\Property(property="deliver_id", type="integer", description="ID of the delivery person"),
     *         )
     *     ),
     *     @OA\Response(
     *         response="200",
     *         description="Delivery person assigned successfully",
     *         @OA\JsonContent(
     *             example={"success": true, "message": "Se ha asignado el repartidor al pedido"}
     *         )
     *     ),
     *     @OA\Response(
     *         response="400",
     *         description="Validation error",
     *         @OA\JsonContent(
     *             example={"success": false, "message": "Validation error details"}
     *         )
     *     ),
     *     @OA\Response(
     *         response="404",
     *         description="Order not found",
     *         @OA\JsonContent(
     *             example={"success": false, "message": "No se encontró el pedido"}
     *         )
     *     )
     * )
     */
    public function assignDelivery(Request $request, $id)
    {

        $validate = validateRequestParams($request, [
            'deliver_id' => 'required|integer',
        ]);

        if (!$validate['success'])
            return validateResponseError($validate);

        $order = Order::find($id);

        if ($order == null) {
            return response()->json([
                'success' => false,
                'message' => 'No se encontró el pedido'
            ]);
        }

        $order->status = 1;
        $order->deliver_id = $request->deliver_id;
        $order->save();

        return response()->json([
            'success' => true,
            'message' => 'Se ha asignado el repartidor al pedido'
        ]);
    }
}
