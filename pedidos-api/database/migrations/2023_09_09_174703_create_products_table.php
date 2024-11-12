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
        Schema::create('products', function (Blueprint $table) {
            $table->id();

            $table->string('name');
            $table->text('description')->nullable();
            $table->tinyInteger('status')->default(1);
            $table->double('price', 8, 2)->default(0.00);
            $table->foreignId('category_id')->constrained('categories')->onDelete('cascade');

            $table->foreignId('user_create_id')->constrained('users')->onDelete('cascade');
            $table->foreignId('user_update_id')->nullable()->constrained('users')->onDelete('cascade');
            $table->foreignId('user_delete_id')->nullable()->constrained('users')->onDelete('cascade');

            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('products');
    }
};
