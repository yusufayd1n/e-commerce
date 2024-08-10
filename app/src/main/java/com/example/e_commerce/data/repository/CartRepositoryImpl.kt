package com.example.e_commerce.data.repository

import com.example.e_commerce.data.local.database.dao.ProductDao
import com.example.e_commerce.data.local.model.ProductDaoModel
import com.example.e_commerce.data.local.model.ProductType
import com.example.e_commerce.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CartRepositoryImpl(
    private val productDao: ProductDao
) : CartRepository {

    override suspend fun addProductToCart(product: ProductDaoModel) {
        try {
            productDao.insertProduct(product)
        } catch (e: Exception) {
            // Hata yönetimi (isteğe bağlı)
        }
    }


    override fun getCartProducts(type: ProductType): Flow<MutableList<ProductDaoModel>> = flow {
        emit(productDao.getProductsByType(type))
    }

    override suspend fun removeProductFromCart(productId: Int) {
        try {
            productDao.deleteProduct(productId)
        } catch (e: Exception) {
            // Hata yönetimi (isteğe bağlı)
        }
    }
}