package com.example.e_commerce.domain.usecase

import com.example.e_commerce.data.local.model.ProductDaoModel
import com.example.e_commerce.domain.repository.CartRepository
import javax.inject.Inject

class AddProductToCartUseCase @Inject constructor(private val cartRepository: CartRepository) {

    suspend fun execute(product: ProductDaoModel) {
        cartRepository.addProductToCart(product)
    }
}