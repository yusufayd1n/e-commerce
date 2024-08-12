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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val addProductUseCase: AddProductToStorageUseCase,
) : ViewModel() {
    private var _getProductsFromLocalDatabase =
        MutableLiveData<Resource<MutableList<ProductDaoModel>>>()
    val getProductsFromLocalDatabase: LiveData<Resource<MutableList<ProductDaoModel>>>
        get() = _getProductsFromLocalDatabase

    fun addProduct(product: ProductDaoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addProductUseCase.execute(product)
        }
    }

}