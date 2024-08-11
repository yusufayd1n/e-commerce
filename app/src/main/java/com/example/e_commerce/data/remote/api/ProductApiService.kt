package com.example.e_commerce.data.remote.api

import com.example.e_commerce.data.remote.dto.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApiService {
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): MutableList<ProductResponse>
}