package com.example.e_commerce.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.local.model.ProductDaoModel
import com.example.e_commerce.domain.usecase.AddProductToStorageUseCase
import com.example.e_commerce.extension.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val addProductUseCase: AddProductToStorageUseCase,
) : ViewModel() {
    private val _addProductStatus = MutableLiveData<Resource<Unit>>()
    val addProductStatus: LiveData<Resource<Unit>> get() = _addProductStatus

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
}