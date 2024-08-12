package com.example.e_commerce.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.local.model.ProductDaoModel
import com.example.e_commerce.data.local.model.ProductType
import com.example.e_commerce.domain.usecase.AddProductToStorageUseCase
import com.example.e_commerce.domain.usecase.GetProductsFromStorageUseCase
import com.example.e_commerce.domain.usecase.RemoveProductFromStorageUseCase
import com.example.e_commerce.extension.Resource
import com.example.e_commerce.extension.toDaoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    private val _addProductStatus = MutableLiveData<Resource<Unit>>()
    val addProductStatus: LiveData<Resource<Unit>> get() = _addProductStatus

    private val _removeProductStatus = MutableLiveData<Resource<Unit>>()
    val removeProductStatus: LiveData<Resource<Unit>> get() = _removeProductStatus

    fun addProduct(product: ProductDaoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _addProductStatus.postValue(Resource.loading(null))
            try {
                addProductUseCase.execute(product)
                _addProductStatus.postValue(Resource.success(null))
            } catch (e: Exception) {
                _addProductStatus.postValue(Resource.error("Error during add", null))
            }
        }

    }

    fun removeProduct(product: ProductDaoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeProductStatus.postValue(Resource.loading(null))
            try {
                removeProductUseCase.execute(product)
                _removeProductStatus.postValue(Resource.success(null))
            } catch (e: Exception) {
                _removeProductStatus.postValue(Resource.error("Error during add", null))
            }
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
            ?.sumOf { it.price * it.quantity } ?: 0.0
    }

    init {
        getProductsByType()
    }
}