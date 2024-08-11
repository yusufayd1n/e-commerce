package com.example.e_commerce.domain.repository

import com.example.e_commerce.data.local.model.ProductDaoModel
import com.example.e_commerce.data.local.model.ProductType
import kotlinx.coroutines.flow.Flow

interface ProductStorageRepository {
    suspend fun addProductToStorage(product: ProductDaoModel)
    fun getProductsFromStorage(type: ProductType): Flow<MutableList<ProductDaoModel>>
    suspend fun removeProductFromStorage(product: ProductDaoModel)
}