<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Product;
use App\Models\FileProduct;
use Illuminate\Support\Facades\Auth;

class ProductController extends Controller
{
    // documentamos con swagger

/**
 * @OA\Post(
 *     path="/api/products",
 *     summary="Create a new product",
 *     description="Creates a new product with the provided details.",
 *     @OA\RequestBody(
 *         required=true,
 *         description="JSON object containing product details",
 *         @OA\JsonContent(
 *             required={"name", "price", "description", "category_id"},
 *             @OA\Property(property="name", type="string", description="Name of the product"),
 *             @OA\Property(property="price", type="number", description="Price of the product"),
 *             @OA\Property(property="description", type="string", description="Description of the product"),
 *             @OA\Property(property="category_id", type="integer", description="ID of the product category"),
 *         )
 *     ),
 *     @OA\Response(
 *         response="201",
 *         description="Product created successfully",
 *         @OA\JsonContent(
 *             example={"success": true, "message": "Producto creado correctamente", "data": {"id": 1, "name": "Product Name", "price": 9.99, "description": "Product Description", "category_id": 1, "user_create_id": 1, "created_at": "2024-01-01 12:00:00", "updated_at": "2024-01-01 12:00:00"}}
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
 *         response="500",
 *         description="Error creating the product",
 *         @OA\JsonContent(
 *             example={"success": false, "message": "Error al crear el producto"}
 *         )
 *     )
 * )
 */
    public function store(Request $request)
    {

        $validate = validateRequestParams($request, [
            'name' => 'required|string',
            'price' => 'required|numeric',
            'description' => 'required|string',
            'category_id' => 'required|integer'
        ]);

        if (!$validate['success'])
            return validateResponseError($validate);

        $product = new Product($request->all());
        $product->user_create_id = Auth::user()->id;

        if (!$product->save())
            return response()->json([
                'success' => false,
                'message' => 'Error al crear el producto'
            ], 500);

        return response()->json([
            'success' => true,
            'message' => 'Producto creado correctamente',
            'data' => $product
        ], 201);
    }

/**
 * @OA\Get(
 *     path="/api/products",
 *     summary="Retrieve a list of products",
 *     description="Retrieves a list of products based on certain criteria.",
 *     @OA\Parameter(
 *         name="id_category",
 *         in="query",
 *         description="ID of the product category to filter by (optional)",
 *         required=false,
 *         @OA\Schema(type="integer")
 *     ),
 *     @OA\Response(
 *         response="200",
 *         description="List of products",
 *         @OA\JsonContent(
 *             example={"success": true, "message": "Productos obtenidos correctamente", "data": {{"id": 1, "name": "Product 1", "price": 9.99, "description": "Description 1", "category_id": 1, "user_create_id": 1, "created_at": "2024-01-01 12:00:00", "updated_at": "2024-01-01 12:00:00"}, {"id": 2, "name": "Product 2", "price": 19.99, "description": "Description 2", "category_id": 2, "user_create_id": 1, "created_at": "2024-01-02 12:00:00", "updated_at": "2024-01-02 12:00:00"}}}
 *         )
 *     ),
 *     @OA\Response(
 *         response="400",
 *         description="Invalid input or validation error",
 *         @OA\JsonContent(
 *             example={"success": false, "message": "Validation error details"}
 *         )
 *     )
 * )
 */
    public function index(Request $request)
    {
        $products = Product::where('status', 1)->get();

        $id_category = $request->query('id_category');

        if (isset($id_category) && $id_category != 0)
            $products = Product::where('category_id', $id_category)->get();


        // obtenemos los archivos de cada producto
        // foreach ($products as $product) {
        //     $product->files = FileProduct::where('product_id', $product->id)->get();
        // }

        return response()->json([
            'success' => true,
            'message' => 'Productos obtenidos correctamente',
            'data' => $products
        ], 200);
    }

    // documentamos con swagger
    /**
 * @OA\Put(
 *     path="/api/products/{id}",
 *     summary="Update an existing product",
 *     description="Updates an existing product with the provided details.",
 *     @OA\Parameter(
 *         name="id",
 *         in="path",
 *         description="ID of the product to update",
 *         required=true,
 *         @OA\Schema(type="integer")
 *     ),
 *     @OA\RequestBody(
 *         required=true,
 *         description="JSON object containing product details",
 *         @OA\JsonContent(
 *             required={"name", "description", "price", "category_id"},
 *             @OA\Property(property="name", type="string", description="Name of the product"),
 *             @OA\Property(property="description", type="string", description="Description of the product"),
 *             @OA\Property(property="price", type="number", description="Price of the product"),
 *             @OA\Property(property="category_id", type="integer", description="ID of the product category"),
 *         )
 *     ),
 *     @OA\Response(
 *         response="200",
 *         description="Product updated successfully",
 *         @OA\JsonContent(
 *             example={"success": true, "message": "Producto actualizada correctamente"}
 *         )
 *     ),
 *     @OA\Response(
 *         response="400",
 *         description="Invalid input or validation error",
 *         @OA\JsonContent(
 *             example={"success": false, "message": "Validation error details"}
 *         )
 *     ),
 *     @OA\Response(
 *         response="404",
 *         description="Product not found",
 *         @OA\JsonContent(
 *             example={"success": false, "message": "No existe el producto"}
 *         )
 *     ),
 *     @OA\Response(
 *         response="500",
 *         description="Error updating the product",
 *         @OA\JsonContent(
 *             example={"success": false, "message": "Error al actualizar el producto"}
 *         )
 *     )
 * )
 */
    public function update(Request $request, $id)
    {

        $validate = validateRequestParams($request, [
            'name' => 'required|string',
            'description' => 'required|string',
            'price' => 'required|numeric',
            'category_id' => 'required|integer'
        ]);

        if (!$validate['success'])
            return validateResponseError($validate);

        $product = Product::find($id);

        if (!$product)
            return response()->json([
                'success' => false,
                'message' => 'No existe el producto'
            ], 404);

        $product->name = $request->name;
        $product->description = $request->description;
        $product->price = $request->price;
        $product->category_id = $request->category_id;


        if (!$product->save())
            return response()->json([
                'success' => false,
                'message' => 'Error al actualizar el producto'
            ], 500);

        return response()->json([
            'success' => true,
            'message' => 'Producto actualizada correctamente'
        ], 200);
    }

/**
 * @OA\Get(
 *     path="/api/products/{id}",
 *     summary="Update an existing product",
 *     description="Updates an existing product with the provided details.",
 *     @OA\Parameter(
 *         name="id",
 *         in="path",
 *         description="ID of the product to update",
 *         required=true,
 *         @OA\Schema(type="integer")
 *     ),
 *     @OA\RequestBody(
 *         required=true,
 *         description="JSON object containing product details",
 *         @OA\JsonContent(
 *             required={"name", "description", "price", "category_id"},
 *             @OA\Property(property="name", type="string", description="Name of the product"),
 *             @OA\Property(property="description", type="string", description="Description of the product"),
 *             @OA\Property(property="price", type="number", description="Price of the product"),
 *             @OA\Property(property="category_id", type="integer", description="ID of the product category"),
 *         )
 *     ),
 *     @OA\Response(
 *         response="200",
 *         description="Product updated successfully",
 *         @OA\JsonContent(
 *             example={"success": true, "message": "Producto actualizada correctamente"}
 *         )
 *     ),
 *     @OA\Response(
 *         response="400",
 *         description="Invalid input or validation error",
 *         @OA\JsonContent(
 *             example={"success": false, "message": "Validation error details"}
 *         )
 *     ),
 *     @OA\Response(
 *         response="404",
 *         description="Product not found",
 *         @OA\JsonContent(
 *             example={"success": false, "message": "No existe el producto"}
 *         )
 *     ),
 *     @OA\Response(
 *         response="500",
 *         description="Error updating the product",
 *         @OA\JsonContent(
 *             example={"success": false, "message": "Error al actualizar el producto"}
 *         )
 *     )
 * )
 */
    public function changeStatus(Request $request, $id)
    {

        $product = Product::find($id);

        if (!$product)
            return response()->json([
                'success' => false,
                'message' => 'No existe el producto'
            ], 404);


        $product->status = 0;

        if (!$product->save())
            return response()->json([
                'success' => false,
                'message' => 'Error al eliminar el producto'
            ], 500);

        return response()->json([
            'success' => true,
            'message' => 'Producto eliminada correctamente'
        ], 200);
    }
}
