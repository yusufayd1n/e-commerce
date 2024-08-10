package com.example.e_commerce.domain.repository

import com.example.e_commerce.data.local.model.ProductDaoModel
import com.example.e_commerce.data.local.model.ProductType
import com.example.e_commerce.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addProductToCart(product: ProductDaoModel)
    fun getCartProducts(type: ProductType): Flow<MutableList<ProductDaoModel>>
    suspend fun removeProductFromCart(productId: Int)
}