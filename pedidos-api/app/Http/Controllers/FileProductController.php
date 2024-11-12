<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Product;
use App\Models\FileProduct;
use App\Models\User;
use Illuminate\Support\Str;
use Illuminate\Support\Facades\Storage;

class FileProductController extends Controller
{

    // documentamos con swagger
/**
 * @OA\Post(
 *     path="/api/products/{id_product}/upload-file",
 *     summary="Upload files for a product",
 *     description="Uploads files for a specific product.",
 *     @OA\Parameter(
 *         name="id_product",
 *         in="path",
 *         description="ID of the product",
 *         required=true,
 *         @OA\Schema(type="integer")
 *     ),
 *     @OA\RequestBody(
 *         required=true,
 *         description="Form data containing files",
 *         @OA\MediaType(
 *             mediaType="multipart/form-data",
 *             @OA\Schema(
 *                 type="object",
 *                 required={"files"},
 *                 @OA\Property(
 *                     property="files",
 *                     type="array",
 *                     @OA\Items(type="string", format="binary")
 *                 ),
 *                 @OA\Property(property="id_user", type="integer", description="ID of the user")
 *             )
 *         )
 *     ),
 *     @OA\Response(
 *         response="200",
 *         description="Files uploaded successfully",
 *         @OA\JsonContent(
 *             example={"success": true, "message": "Archivos guardados correctamente"}
 *         )
 *     ),
 *     @OA\Response(
 *         response="400",
 *         description="Validation error or user/product not found",
 *         @OA\JsonContent(
 *             example={"success": false, "message": "Validation error details or User/Producto no encontrada"}
 *         )
 *     )
 * )
 */
    public function uploadFile(Request $request, $id_product)
    {

        $validate = validateRequestParams($request, [
            'files' => 'required',
        ]);

        if (!$validate['success'])
            return validateResponseError($validate);

        $id_user = $request->id_user;

        $user = User::find($id_user);
        if (!$user)
            return response()->json([
                'success' => false,
                'message' => 'Usuario no encontrado'
            ], 400);

        $quotation = Product::find($id_product);
        if (!$quotation)
            return response()->json([
                'success' => false,
                'message' => 'Producto no encontrada'
            ], 400);

        $files = $request->file('files');
        foreach ($files as $file) {
            // guardamos el archivo
            $path = Storage::disk('public')->put("products/$id_product", $file);
            $fileQuotation = new FileProduct();
            $fileQuotation->name = Str::lower($file->getClientOriginalName());
            $fileQuotation->type = Str::lower($file->getClientOriginalExtension());
            $fileQuotation->product_id = $id_product;
            $fileQuotation->path = $path;
            $fileQuotation->save();
        }

        return response()->json([
            'success' => true,
            'message' => 'Archivos guardados correctamente'
        ], 200);
    }
}
