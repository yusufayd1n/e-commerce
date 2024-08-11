package com.example.e_commerce.domain.usecase

import com.example.e_commerce.data.local.model.ProductDaoModel
import com.example.e_commerce.data.local.model.ProductType
import com.example.e_commerce.domain.repository.ProductStorageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsFromStorageUseCase @Inject constructor(private val productStorageRepository: ProductStorageRepository) {
    fun execute(type: ProductType): Flow<MutableList<ProductDaoModel>>{
        return productStorageRepository.getProductsFromStorage(type)
    }
}