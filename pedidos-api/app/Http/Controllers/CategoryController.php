<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Category;
use App\Models\Product;
use Illuminate\Support\Facades\Auth;

// documentamos con swagger


class CategoryController extends Controller
{

    // documentamos con swagger
    /**
     * @OA\Post(
     *    path="/api/category",
     *   summary="Crear categoría",
     *  tags={"Category"},
     * security={ {"sanctum": {} }},
     * @OA\RequestBody(
     *   required=true,
     *  description="Datos para crear una categoría",
     * @OA\JsonContent(
     *   required={"name","description"},
     * @OA\Property(property="name", type="string", example="Tecnología"),
     * @OA\Property(property="description", type="string", example="Productos de tecnología"),
     * ),
     * ),
     * @OA\Response(
     *  response=201,
     * description="Categoría creada correctamente",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="true"),
     * @OA\Property(property="message", type="string", example="Categoría creada correctamente"),
     * ),
     * ),
     * @OA\Response(
     * response=400,
     * description="Ya existe una categoría con ese nombre",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="false"),
     * @OA\Property(property="message", type="string", example="Ya existe una categoría con ese nombre"),
     * ),
     * ),
     * @OA\Response(
     * response=500,
     * description="Error al crear la categoría",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="false"),
     * @OA\Property(property="message", type="string", example="Error al crear la categoría"),
     * ),
     * ),
     * )
     */
    public function store(Request $request)
    {
        $validate = validateRequestParams($request, [
            'name' => 'required|string',
            'description' => 'required|string'
        ]);

        if (!$validate['success'])
            return validateResponseError($validate);

        // buscamos que el nombre de la categoría no exista
        $category = Category::where('name', $request->name)->first();

        if ($category)
            return response()->json([
                'success' => false,
                'message' => 'Ya existe una categoría con ese nombre'
            ], 400);

        $category = new Category($request->all());
        $category->user_create_id = Auth::user()->id;

        if(!$category->save())
            return response()->json([
                'success' => false,
                'message' => 'Error al crear la categoría'
            ], 500);

        return response()->json([
            'success' => true,
            'message' => 'Categoría creada correctamente'
        ], 201);


    }

    // documentamos con swagger
    /**
     * @OA\Get(
     *    path="/api/category",
     *   summary="Listar categorías",
     *  tags={"Category"},
     * security={ {"sanctum": {} }},
     * @OA\Response(
     *  response=200,
     * description="Listado de categorías",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="true"),
     * @OA\Property(property="data", type="array",
     * @OA\Items(
     * @OA\Property(property="id", type="integer", example="1"),
     * @OA\Property(property="name", type="string", example="Tecnología"),
     * @OA\Property(property="description", type="string", example="Productos de tecnología"),
     * @OA\Property(property="status", type="integer", example="1"),
     * @OA\Property(property="user_create_id", type="integer", example="1"),
     * @OA\Property(property="created_at", type="string", example="2021-08-30T22:00:00.000000Z"),
     * @OA\Property(property="updated_at", type="string", example="2021-08-30T22:00:00.000000Z"),
     * @OA\Property(property="count_products", type="integer", example="2"),
     * ),
     * ),
     * ),
     * ),
     * @OA\Response(
     * response=500,
     * description="Error al listar las categorías",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="false"),
     * @OA\Property(property="message", type="string", example="Error al listar las categorías"),
     * ),
     * ),
     * )
     */
    public function index(){

        $categories = Category::where('status', 1);

        $categories = $categories->get();

        foreach ($categories as $category) {
            $category->count_products = Product::where('category_id', $category->id)->count();
        }

        return response()->json([
            'success' => true,
            'data' => $categories
        ], 200);
    }

    // documentamos con swagger
    /**
     * @OA\Get(
     *    path="/api/category/{id}",
     *   summary="Obtener categoría",
     *  tags={"Category"},
     * security={ {"sanctum": {} }},
     * @OA\Parameter(
     *  name="id",
     * description="Id de la categoría",
     * required=true,
     * in="path",
     * @OA\Schema(
     * type="integer",
     * ),
     * ),
     * @OA\Response(
     *  response=200,
     * description="Categoría obtenida correctamente",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="true"),
     * @OA\Property(property="data", type="object",
     * @OA\Property(property="id", type="integer", example="1"),
     * @OA\Property(property="name", type="string", example="Tecnología"),
     * @OA\Property(property="description", type="string", example="Productos de tecnología"),
     * @OA\Property(property="status", type="integer", example="1"),
     * @OA\Property(property="user_create_id", type="integer", example="1"),
     * @OA\Property(property="created_at", type="string", example="2021-08-30T22:00:00.000000Z"),
     * @OA\Property(property="updated_at", type="string", example="2021-08-30T22:00:00.000000Z"),
     * ),
     * ),
     * ),
     * @OA\Response(
     * response=404,
     * description="No existe la categoría",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="false"),
     * @OA\Property(property="message", type="string", example="No existe la categoría"),
     * ),
     * ),
     * @OA\Response(
     * response=500,
     * description="Error al obtener la categoría",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="false"),
     * @OA\Property(property="message", type="string", example="Error al obtener la categoría"),
     * ),
     * ),
     * )
     */
    public function update(Request $request, $id){

        $validate = validateRequestParams($request, [
            'name' => 'required|string',
            'description' => 'required|string'
        ]);

        if (!$validate['success'])
            return validateResponseError($validate);

        $category = Category::find($id);

        if(!$category)
            return response()->json([
                'success' => false,
                'message' => 'No existe la categoría'
            ], 404);

        $category->name = $request->name;
        $category->description = $request->description;

        if(!$category->save())
            return response()->json([
                'success' => false,
                'message' => 'Error al actualizar la categoría'
            ], 500);

        return response()->json([
            'success' => true,
            'message' => 'Categoría actualizada correctamente'
        ], 200);

    }

    // documentamos con swagger
    /**
     * @OA\Put(
     *    path="/api/category/changeStatus/{id}",
     *   summary="Cambiar estado de la categoría",
     *  tags={"Category"},
     * security={ {"sanctum": {} }},
     * @OA\Parameter(
     *  name="id",
     * description="Id de la categoría",
     * required=true,
     * in="path",
     * @OA\Schema(
     * type="integer",
     * ),
     * ),
     * @OA\Response(
     *  response=200,
     * description="Categoría eliminada correctamente",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="true"),
     * @OA\Property(property="message", type="string", example="Categoría eliminada correctamente"),
     * ),
     * ),
     * @OA\Response(
     * response=404,
     * description="No existe la categoría",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="false"),
     * @OA\Property(property="message", type="string", example="No existe la categoría"),
     * ),
     * ),
     * @OA\Response(
     * response=400,
     * description="No se puede eliminar la categoría porque tiene productos asociados",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="false"),
     * @OA\Property(property="message", type="string", example="No se puede eliminar la categoría porque tiene productos asociados"),
     * ),
     * ),
     * @OA\Response(
     * response=500,
     * description="Error al eliminar la categoría",
     * @OA\JsonContent(
     * @OA\Property(property="success", type="boolean", example="false"),
     * @OA\Property(property="message", type="string", example="Error al eliminar la categoría"),
     * ),
     * ),
     * )
     */
    public function changeStatus(Request $request, $id){

            $category = Category::find($id);

            if(!$category)
                return response()->json([
                    'success' => false,
                    'message' => 'No existe la categoría'
                ], 404);

            $products = Product::where('category_id', $id)->get();

            if(count($products) > 0)
                return response()->json([
                    'success' => false,
                    'message' => 'No se puede eliminar la categoría porque tiene productos asociados'
                ], 400);

            $category->status = 0;

            if(!$category->save())
                return response()->json([
                    'success' => false,
                    'message' => 'Error al eliminar la categoría'
                ], 500);

            return response()->json([
                'success' => true,
                'message' => 'Categoría eliminada correctamente'
            ], 200);
    }
}
