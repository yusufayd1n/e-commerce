package com.example.e_commerce.domain.repository

import com.example.e_commerce.domain.model.Product
import com.example.e_commerce.extension.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(pageSize: Int, page: Int): Flow<Resource<MutableList<Product>>>
}