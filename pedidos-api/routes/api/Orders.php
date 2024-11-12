<?php

use Illuminate\Support\Facades\Route;

use App\Http\Controllers\OrderController;

Route::group([
    'middleware' => ['api', 'auth:sanctum'],
], function () {

    Route::post('order', [OrderController::class, 'store']);
    Route::get('order', [OrderController::class, 'index']);
    Route::get('order/{id}', [OrderController::class, 'getDetail']);
    Route::put('order/changeStatus/{id}', [OrderController::class, 'changeStatus']);
    Route::put("order/assignDelivery/{id}", [OrderController::class, "assignDelivery"]);

});


