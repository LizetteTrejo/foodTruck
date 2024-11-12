<?php

use Illuminate\Support\Facades\Route;

use App\Http\Controllers\ProductController;
use App\Http\Controllers\FileProductController;

Route::group([
    'middleware' => ['api', 'auth:sanctum'],
], function () {

    Route::post('product', [ProductController::class, 'store']);
    Route::put('product/{id}', [ProductController::class, 'update']);
    Route::put('product/changeStatus/{id}', [ProductController::class, 'changeStatus']);

});

Route::post('product/uploadFile/{id}', [FileProductController::class, 'uploadFile']);
Route::get('product', [ProductController::class, 'index']);
