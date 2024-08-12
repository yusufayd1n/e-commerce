package com.example.e_commerce.data.repository

import android.util.Log
import com.example.e_commerce.data.local.database.dao.ProductDao
import com.example.e_commerce.data.local.model.ProductDaoModel
import com.example.e_commerce.data.local.model.ProductType
import com.example.e_commerce.domain.repository.ProductStorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class StorageRepositoryImpl(
    private val productDao: ProductDao
) : ProductStorageRepository {

    override suspend fun addProductToStorage(product: ProductDaoModel) {
        try {
            val existingProducts =
                productDao.getProductsByType(ProductType.CART).first()

            val existingProduct = existingProducts
                .find { it.name == product.name && it.model == product.model && it.type == product.type }
            if (existingProduct != null) {
                productDao.updateProductQuantity(
                    product.name,
                    product.model,
                    product.type,
                    1
                )
            } else {
                productDao.insertProduct(product)
            }
        } catch (e: Exception) {
            Log.d("StorageRepositoryImpl", e.message.toString())
        }
    }

    override fun getProductsFromStorage(type: ProductType): Flow<MutableList<ProductDaoModel>> {
        return productDao.getProductsByType(type)
    }


    override suspend fun removeProductFromStorage(product: ProductDaoModel) {
        if (product.quantity <= 1) {
            productDao.deleteProduct(product.id.toString())
        } else {
            productDao.updateProductQuantity(
                product.name,
                product.model,
                product.type,
                -1
            )
        }
    }
}