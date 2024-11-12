<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Order extends Model
{
    use HasFactory;

    protected $fillable=[
        'user_id',
        'order_number',
        'delivery_location_id',
        'type_delivery',
        'total_products',
        'cost',
        'total_shipping',
        'status',
        'deliver_id',
        'date_delivery',
        'date_delivery_end',
        'date_cancel',
    ];
}
