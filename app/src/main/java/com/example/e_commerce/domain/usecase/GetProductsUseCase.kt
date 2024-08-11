package com.example.e_commerce.domain.usecase


import com.example.e_commerce.domain.model.Product
import com.example.e_commerce.domain.repository.ProductRepository
import com.example.e_commerce.extension.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(pageSize: Int, page: Int): Flow<Resource<MutableList<Product>>> {
        return productRepository.getProducts(pageSize, page)
    }
}