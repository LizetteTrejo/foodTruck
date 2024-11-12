<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::table('orders', function (Blueprint $table) {


            $table->foreignId('delivery_location_id')->constrained('delivery_locations');
            $table->foreignId('deliver_id')->constrained('users')->nullable();

            // fecha de entrega
            $table->timestamp('date_delivery')->nullable();
            $table->timestamp('date_delivery_end')->nullable();
            $table->timestamp('date_cancel')->nullable();


        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('orders', function (Blueprint $table) {
            //
        });
    }
};
