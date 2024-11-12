<?php

use Illuminate\Support\Facades\Route;

use App\Http\Controllers\CategoryController;

Route::group([
    'middleware' => ['api', 'auth:sanctum'],
], function () {

    Route::post('category', [CategoryController::class, 'store']);
    Route::put('category/{id}', [CategoryController::class, 'update']);
    Route::put('category/changeStatus/{id}', [CategoryController::class, 'changeStatus']);
});

Route::get('category', [CategoryController::class, 'index']);
