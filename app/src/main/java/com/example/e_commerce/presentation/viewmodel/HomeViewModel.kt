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
import com.example.e_commerce.domain.usecase.SearchProductsUseCase
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
    private val searchProductUseCase: SearchProductsUseCase,
) : ViewModel() {
    private var _getProducts = MutableLiveData<Resource<MutableList<Product>>>()
    val getProducts: LiveData<Resource<MutableList<Product>>>
        get() = _getProducts

    private var _searchResults = MutableLiveData<Resource<MutableList<Product>>>()
    val searchResults: LiveData<Resource<MutableList<Product>>>
        get() = _searchResults

    private val _addProductStatus = MutableLiveData<Resource<Unit>>()
    val addProductStatus: LiveData<Resource<Unit>> get() = _addProductStatus

    var page = 1
    private var pageSize = 8

    fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _getProducts.postValue(Resource.loading(null))
            try {
                productsUseCase.invoke(pageSize, page).collect { productList ->
                    if (page == 1) {
                        _getProducts.postValue(productList)
                    } else {
                        val currentList = _getProducts.value?.data?.toMutableList() ?: mutableListOf()
                        productList.data?.let { currentList.addAll(it) }
                        _getProducts.postValue(Resource.success(currentList))
                    }
                }
            } catch (e: Exception) {
                _getProducts.postValue(Resource.error(e.message ?: "Unknown error", null))
            }
        }
    }

    fun searchOrLoadProducts(query: String?) {
        if (query.isNullOrEmpty()) {
            page = 1
            getProducts()
        } else {
            searchProducts(query)
        }
    }

    private fun searchProducts(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchResults.postValue(Resource.loading(null))
            try {
                searchProductUseCase.invoke(name).collect { productList ->
                    _searchResults.postValue(productList)
                }
            } catch (e: Exception) {
                _searchResults.postValue(Resource.error(e.message ?: "Unknown error", null))
            }
        }
    }

    fun addProduct(product: Product, type: ProductType) {
        viewModelScope.launch(Dispatchers.IO) {
            _addProductStatus.postValue(Resource.loading(null))
            try {
                addProductUseCase.execute(product.toDaoModel(type))
                _addProductStatus.postValue(Resource.success(null))
            } catch (e: Exception) {
                _addProductStatus.postValue(Resource.error("Error during add", null))
            }
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