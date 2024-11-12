<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\DeliveryLocation;
use Illuminate\Support\Facades\Auth;

class DeliveryLocationController extends Controller
{
    // documnetamos con swagger
    /**
     * @OA\Post(
     *    path="/api/delivery-location",
     *   summary="Guardar ubicación de entrega",
     *  tags={"Delivery Location"},
     * security={ {"sanctum": {} }},
     * @OA\RequestBody(
     *   required=true,
     *  description="Datos para guardar la ubicación de entrega",
     * @OA\JsonContent(
     *   required={"name","address","city","state","cp"},
     * @OA\Property(property="name", type="string", example="Casa"),
     * @OA\Property(property="address", type="string", example="Calle 1"),
     * @OA\Property(property="city", type="string", example="Ciudad"),
     * @OA\Property(property="state", type="string", example="Estado"),
     * @OA\Property(property="cp", type="string", example="12345"),
     * ),
     * ),
     * @OA\Response(
     *  response=201,
     * description="Ubicación de entrega guardada correctamente",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="true"),
     * @OA\Property(property="message", type="string", example="Ubicación de entrega guardada correctamente"),
     * ),
     * ),
     * @OA\Response(
     * response=500,
     * description="Error al guardar la ubicación de entrega",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="false"),
     * @OA\Property(property="message", type="string", example="Error al guardar la ubicación de entrega"),
     * ),
     * ),
     * )
     */
    public function store(Request $request){

        $validate = validateRequestParams($request, [
            'name' => 'required|string',
            'address' => 'required|string',
            'city' => 'required|string',
            'state' => 'required|string',
            'cp' => 'required|string',
        ]);

        if (!$validate['success'])
            return validateResponseError($validate);

        $deliveryLocation = new DeliveryLocation($request->all());
        $deliveryLocation->user_id = Auth::user()->id;

        if(!$deliveryLocation->save())
            return response()->json([
                'success' => false,
                'message' => 'Error al guardar la ubicación de entrega'
            ], 500);

        return response()->json([
            'success' => true,
            'message' => 'Ubicación de entrega guardada correctamente'
        ], 201);

    }

    // documnetamos con swagger
    /**
     * @OA\Get(
     *    path="/api/delivery-location",
     *   summary="Obtener ubicaciones de entrega",
     *  tags={"Delivery Location"},
     * security={ {"sanctum": {} }},
     * @OA\Response(
     *  response=200,
     * description="Ubicaciones de entrega obtenidas correctamente",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="true"),
     * @OA\Property(property="message", type="string", example="Ubicaciones de entrega obtenidas correctamente"),
     * @OA\Property(property="data", type="array", @OA\Items(
     * @OA\Property(property="id", type="integer", example="1"),
     * @OA\Property(property="user_id", type="integer", example="1"),
     * @OA\Property(property="name", type="string", example="Casa"),
     * @OA\Property(property="address", type="string", example="Calle 1"),
     * @OA\Property(property="city", type="string", example="Ciudad"),
     * @OA\Property(property="state", type="string", example="Estado"),
     * @OA\Property(property="cp", type="string", example="12345"),
     * @OA\Property(property="status", type="integer", example="1"),
     * @OA\Property(property="created_at", type="string", example="2021-09-01T00:00:00.000000Z"),
     * @OA\Property(property="updated_at", type="string", example="2021-09-01T00:00:00.000000Z"),
     * )),
     * ),
     * ),
     * @OA\Response(
     * response=500,
     * description="Error al obtener las ubicaciones de entrega",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="false"),
     * @OA\Property(property="message", type="string", example="Error al obtener las ubicaciones de entrega"),
     * ),
     * ),
     * )
     */
    public function index(Request $request){

        $deliveryLocation = DeliveryLocation::where('user_id', Auth::user()->id)
        ->where('status', 1)
        ->get();

        return response()->json([
            'success' => true,
            'message' => 'Ubicaciones de entrega obtenidas correctamente',
            'data' => $deliveryLocation
        ], 200);

    }

    // documnetamos con swagger
    /**
     * @OA\Get(
     *    path="/api/delivery-location/{id}",
     *   summary="Obtener ubicación de entrega",
     *  tags={"Delivery Location"},
     * security={ {"sanctum": {} }},
     * @OA\Parameter(
     *  name="id",
     * description="Id de la ubicación de entrega",
     * required=true,
     * in="path",
     * @OA\Schema(
     * type="integer",
     * ),
     * ),
     * @OA\Response(
     *  response=200,
     * description="Ubicación de entrega obtenida correctamente",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="true"),
     * @OA\Property(property="message", type="string", example="Ubicación de entrega obtenida correctamente"),
     * @OA\Property(property="data", type="object",
     * @OA\Property(property="id", type="integer", example="1"),
     * @OA\Property(property="user_id", type="integer", example="1"),
     * @OA\Property(property="name", type="string", example="Casa"),
     * @OA\Property(property="address", type="string", example="Calle 1"),
     * @OA\Property(property="city", type="string", example="Ciudad"),
     * @OA\Property(property="state", type="string", example="Estado"),
     * @OA\Property(property="cp", type="string", example="12345"),
     * @OA\Property(property="status", type="integer", example="1"),
     * @OA\Property(property="created_at", type="string", example="2021-09-01T00:00:00.000000Z"),
     * @OA\Property(property="updated_at", type="string", example="2021-09-01T00:00:00.000000Z"),
     * ),
     * ),
     * ),
     * @OA\Response(
     * response=404,
     * description="No se encontró la ubicación de entrega",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="false"),
     * @OA\Property(property="message", type="string", example="No se encontró la ubicación
     * de entrega"),
     * ),
     * ),
     * )
     */
    public function update(Request $request, $id){

        $validate = validateRequestParams($request, [
            'name' => 'required|string',
            'address' => 'required|string',
            'city' => 'required|string',
            'state' => 'required|string',
            'cp' => 'required|string',
        ]);

        if (!$validate['success'])
            return validateResponseError($validate);

        $deliveryLocation = DeliveryLocation::find($id);

        if(!$deliveryLocation)
            return response()->json([
                'success' => false,
                'message' => 'No se encontró la ubicación de entrega'
            ], 404);

        if($deliveryLocation->user_id != Auth::user()->id)
            return response()->json([
                'success' => false,
                'message' => 'No tienes permiso para editar esta ubicación de entrega'
            ], 403);

        $deliveryLocation->fill($request->all());

        if(!$deliveryLocation->save())
            return response()->json([
                'success' => false,
                'message' => 'Error al actualizar la ubicación de entrega'
            ], 500);

        return response()->json([
            'success' => true,
            'message' => 'Ubicación de entrega actualizada correctamente'
        ], 200);

    }

    // documnetamos con swagger
    /**
     * @OA\Delete(
     *    path="/api/delivery-location/{id}",
     *   summary="Eliminar ubicación de entrega",
     *  tags={"Delivery Location"},
     * security={ {"sanctum": {} }},
     * @OA\Parameter(
     *  name="id",
     * description="Id de la ubicación de entrega",
     * required=true,
     * in="path",
     * @OA\Schema(
     * type="integer",
     * ),
     * ),
     * @OA\Response(
     *  response=200,
     * description="Ubicación de entrega eliminada correctamente",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="true"),
     * @OA\Property(property="message", type="string", example="Ubicación de entrega eliminada correctamente"),
     * ),
     * ),
     * @OA\Response(
     * response=404,
     * description="No se encontró la ubicación de entrega",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="false"),
     * @OA\Property(property="message", type="string", example="No se encontró la ubicación
     * de entrega"),
     * ),
     * ),
     * )
     */
    public function changeStatus(Request $request, $id){

        $validate = validateRequestParams($request, [
            'status' => 'required|numeric'
        ]);

        if (!$validate['success'])
            return validateResponseError($validate);

        $deliveryLocation = DeliveryLocation::find($id);

        if(!$deliveryLocation)
            return response()->json([
                'success' => false,
                'message' => 'No se encontró la ubicación de entrega'
            ], 404);

        if($deliveryLocation->user_id != Auth::user()->id)
            return response()->json([
                'success' => false,
                'message' => 'No tienes permiso para editar esta ubicación de entrega'
            ], 403);

        $deliveryLocation->status = $request->status;

        if(!$deliveryLocation->save())
            return response()->json([
                'success' => false,
                'message' => 'Error al actualizar la ubicación de entrega'
            ], 500);

        return response()->json([
            'success' => true,
            'message' => 'Ubicación de entrega actualizada correctamente'
        ], 200);

    }
}
