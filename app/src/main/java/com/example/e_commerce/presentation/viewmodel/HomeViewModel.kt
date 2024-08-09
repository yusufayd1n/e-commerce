package com.example.e_commerce.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.domain.model.Product
import com.example.e_commerce.domain.usecase.GetProductsUseCase
import com.example.e_commerce.extension.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productsUseCase: GetProductsUseCase
) : ViewModel() {
    private var _getProducts = MutableLiveData<Resource<List<Product>>>()
    val getProducts: LiveData<Resource<List<Product>>>
        get() = _getProducts

    fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            productsUseCase.invoke().collect {
                _getProducts.postValue(it)
            }
        }
    }

    init {
        getProducts()
        viewModelScope.launch {
            delay(5000)
            Log.d("YUSUFAYDIN", getProducts.value.toString())
        }
    }
}