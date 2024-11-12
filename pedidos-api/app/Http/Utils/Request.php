<?php

use Illuminate\Support\Facades\Validator;
use Illuminate\Http\Request;

function validateRequestParams(Request $request, array $params, string $message = "Revise los campos")
{

    $validacion = Validator::make($request->all(), $params);

    if ($validacion->fails()) {

        // Errores
        $errors_values = [];
        foreach ($validacion->errors()->all() as $error) {
            $errors_values[] = $error;
        }

        return [
            'success' => false,
            'message' => $message,
            'errors' => $errors_values
        ];
    }

    return [
        'success' => true
    ];
}

function validateResponseError($validate)
{
    return response()->json([
        'success' => false,
        'message' => $validate['message'],
        'errors' => $validate['errors']
    ], 400);
}
