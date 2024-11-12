<?php

use Illuminate\Support\Facades\Route;

use App\Http\Controllers\DeliveryLocationController;

Route::group([
    'middleware' => ['api', 'auth:sanctum'],
], function () {

    Route::post('deliveryLocation', [DeliveryLocationController::class, 'store']);
    Route::put('deliveryLocation/{id}', [DeliveryLocationController::class, 'update']);
    Route::put('deliveryLocation/changeStatus/{id}', [DeliveryLocationController::class, 'changeStatus']);
    Route::get('deliveryLocation', [DeliveryLocationController::class, 'index']);

});

