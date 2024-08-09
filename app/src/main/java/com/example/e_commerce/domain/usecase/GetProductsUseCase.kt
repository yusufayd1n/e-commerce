package com.example.e_commerce.domain.usecase


import com.example.e_commerce.domain.model.Product
import com.example.e_commerce.domain.repository.ProductRepository
import com.example.e_commerce.extension.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val carRepository: ProductRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Product>>> {
        return carRepository.getProducts()
    }
}