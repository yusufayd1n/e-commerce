package com.example.e_commerce.data.repository

import com.example.e_commerce.data.remote.api.ProductApiService
import com.example.e_commerce.data.remote.dto.ProductResponse
import com.example.e_commerce.domain.model.Product
import com.example.e_commerce.domain.repository.ProductRepository
import com.example.e_commerce.extension.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteProductRepositoryImpl(
    private val productApiService: ProductApiService
) : ProductRepository {

    override suspend fun getProducts(
        pageSize: Int,
        page: Int
    ): Flow<Resource<MutableList<Product>>> = flow {
        emit(Resource.loading(null))

        val productResponse: MutableList<ProductResponse> =
            productApiService.getProducts(pageSize, page)

        try {
            val products = productResponse.map { response ->
                Product(
                    id = response.id,
                    name = response.name,
                    image = response.image,
                    price = response.price.toDouble(),
                    description = response.description,
                    model = response.model,
                    brand = response.brand
                )
            }.toMutableList()

            emit(Resource.success(products))
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Unknown error", null))
        }
    }

    override suspend fun searchProducts(name: String): Flow<Resource<MutableList<Product>>> = flow {
        emit(Resource.loading(null))
        try {
            val productResponse = productApiService.searchProducts(name)
            val products = productResponse.map { response ->
                Product(
                    id = response.id,
                    name = response.name,
                    image = response.image,
                    price = response.price.toDouble(),
                    description = response.description,
                    model = response.model,
                    brand = response.brand
                )
            }.toMutableList()
            emit(Resource.success(products))
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Unknown error", null))
        }
    }
}