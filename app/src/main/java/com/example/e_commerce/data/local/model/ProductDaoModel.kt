package com.example.e_commerce.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductDaoModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val image: String,
    val price: Double,
    val description: String,
    val model: String,
    val brand: String,
    val type: ProductType,
    val quantity: Int = 1
)

enum class ProductType {
    CART, FAVORITE
}