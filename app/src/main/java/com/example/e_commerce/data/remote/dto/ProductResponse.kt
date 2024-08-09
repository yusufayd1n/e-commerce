package com.example.e_commerce.data.remote.dto

data class ProductResponse(
    val id: String,
    val createdAt: String,
    val name: String,
    val image: String,
    val price: String,
    val description: String,
    val model: String,
    val brand: String
)
