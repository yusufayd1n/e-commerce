package com.example.e_commerce.extension

import com.example.e_commerce.data.local.model.ProductDaoModel
import com.example.e_commerce.data.local.model.ProductType
import com.example.e_commerce.domain.model.Product


fun Product.toDaoModel(type: ProductType): ProductDaoModel {
    return ProductDaoModel(
        id = 0, // Auto-generated ID
        name = this.name,
        image = this.image,
        price = this.price,
        description = this.description,
        model = this.model,
        brand = this.brand,
        type = type
    )
}
