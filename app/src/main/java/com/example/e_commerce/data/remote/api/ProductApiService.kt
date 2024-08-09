package com.example.e_commerce.data.remote.api

import com.example.e_commerce.data.remote.dto.ProductResponse
import com.example.e_commerce.extension.Resource
import retrofit2.http.GET

interface ProductApiService {
    @GET("products")
    suspend fun getCars(): List<ProductResponse>
}