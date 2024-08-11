package com.example.e_commerce.domain.usecase

import com.example.e_commerce.data.local.model.ProductDaoModel
import com.example.e_commerce.domain.repository.ProductStorageRepository
import javax.inject.Inject

class AddProductToStorageUseCase @Inject constructor(private val productStorageRepository: ProductStorageRepository) {
    suspend fun execute(product: ProductDaoModel) {
        productStorageRepository.addProductToStorage(product)
    }
}