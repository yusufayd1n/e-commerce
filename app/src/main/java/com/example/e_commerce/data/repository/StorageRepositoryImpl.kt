package com.example.e_commerce.data.repository

import com.example.e_commerce.data.local.database.dao.ProductDao
import com.example.e_commerce.data.local.model.ProductDaoModel
import com.example.e_commerce.data.local.model.ProductType
import com.example.e_commerce.domain.repository.ProductStorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StorageRepositoryImpl(
    private val productDao: ProductDao
) : ProductStorageRepository {

    override suspend fun addProductToStorage(product: ProductDaoModel) {
        try {
            productDao.insertProduct(product)
        } catch (e: Exception) {
            // Hata yönetimi (isteğe bağlı)
        }
    }


    override fun getProductsFromStorage(type: ProductType): Flow<MutableList<ProductDaoModel>> = flow {
        emit(productDao.getProductsByType(type))
    }

    override suspend fun removeProductFromStorage(productId: String) {
        try {
            productDao.deleteProduct(productId)
        } catch (e: Exception) {
            // Hata yönetimi (isteğe bağlı)
        }
    }
}