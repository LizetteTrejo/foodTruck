<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Product extends Model
{
    use HasFactory;

    protected  $fillable = [
        'name',
        'description',
        'price',
        'category_id',
        'user_create_id',
        'user_update_id',
        'user_delete_id',
        'status'
    ];
}
