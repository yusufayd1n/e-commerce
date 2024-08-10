package com.example.e_commerce.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String,
    val name: String,
    val image: String,
    val price: Double,
    val description: String,
    val model: String,
    val brand: String
) : Parcelable