package com.example.e_commerce.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.local.model.ProductType
import com.example.e_commerce.domain.model.Product
import com.example.e_commerce.domain.usecase.AddProductToStorageUseCase
import com.example.e_commerce.domain.usecase.GetProductsUseCase
import com.example.e_commerce.extension.Resource
import com.example.e_commerce.extension.toDaoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productsUseCase: GetProductsUseCase,
    private val addProductUseCase: AddProductToStorageUseCase,
) : ViewModel() {
    private var _getProducts = MutableLiveData<Resource<MutableList<Product>>>()
    val getProducts: LiveData<Resource<MutableList<Product>>>
        get() = _getProducts

    private var page = 1
    private var pageSize = 4
    private fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _getProducts.postValue(Resource.loading(null))
            try {
                productsUseCase.invoke(pageSize, page).collect { productList ->
                    _getProducts.postValue(productList)
                }

            } catch (e: Exception) {
                _getProducts.postValue(Resource.error(e.message ?: "Unknown error", null))
            }
        }
    }

    fun addProduct(product: Product, type: ProductType) {
        viewModelScope.launch(Dispatchers.IO) {
            addProductUseCase.execute(product.toDaoModel(type))
        }
    }

    fun loadNextPage() {
        page += 1
        getProducts()
    }

    init {
        getProducts()
    }

}