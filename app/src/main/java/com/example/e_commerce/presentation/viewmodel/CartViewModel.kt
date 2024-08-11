package com.example.e_commerce.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.local.model.ProductDaoModel
import com.example.e_commerce.data.local.model.ProductType
import com.example.e_commerce.domain.model.Product
import com.example.e_commerce.domain.usecase.AddProductToStorageUseCase
import com.example.e_commerce.domain.usecase.GetProductsFromStorageUseCase
import com.example.e_commerce.domain.usecase.RemoveProductFromStorageUseCase
import com.example.e_commerce.domain.usecase.GetProductsUseCase
import com.example.e_commerce.extension.Resource
import com.example.e_commerce.extension.toDaoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val addProductUseCase: AddProductToStorageUseCase,
    private val removeProductUseCase: RemoveProductFromStorageUseCase,
    private val getProductsProductUseCase: GetProductsFromStorageUseCase,
) : ViewModel() {
    private var _getProductsFromLocalDatabase =
        MutableLiveData<Resource<MutableList<ProductDaoModel>>>()
    val getProductsFromLocalDatabase: LiveData<Resource<MutableList<ProductDaoModel>>>
        get() = _getProductsFromLocalDatabase

    fun addProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            addProductUseCase.execute(product.toDaoModel(ProductType.CART))
        }
    }

    fun removeProduct(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            removeProductUseCase.execute(id)
        }
    }

    fun getProductsByType() {
        viewModelScope.launch(Dispatchers.IO) {
            _getProductsFromLocalDatabase.postValue(Resource.loading(null))
            try {
                getProductsProductUseCase.execute(ProductType.CART).collect {
                    _getProductsFromLocalDatabase.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _getProductsFromLocalDatabase.postValue(
                    Resource.error(
                        e.message ?: "Unknown error",
                        null
                    )
                )
            }

        }
    }

    fun calculateCartAmount(): Double {
        return _getProductsFromLocalDatabase.value?.data
            ?.sumOf { it.price } ?: 0.0
    }

    init {
        getProductsByType()
    }
}