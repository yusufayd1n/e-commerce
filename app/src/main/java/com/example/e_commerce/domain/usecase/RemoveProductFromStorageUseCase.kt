package com.example.e_commerce.domain.usecase

import com.example.e_commerce.data.local.model.ProductDaoModel
import com.example.e_commerce.domain.repository.ProductStorageRepository
import javax.inject.Inject

class RemoveProductFromStorageUseCase @Inject constructor(private val productStorageRepository: ProductStorageRepository) {
    suspend fun execute(productId: String) {
        productStorageRepository.removeProductFromStorage(productId)
    }
}