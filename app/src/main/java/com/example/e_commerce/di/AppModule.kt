package com.example.e_commerce.di

import android.content.Context
import com.example.e_commerce.App
import com.example.e_commerce.data.local.database.dao.ProductDao
import com.example.e_commerce.data.remote.api.ProductApiService
import com.example.e_commerce.data.repository.CartRepositoryImpl
import com.example.e_commerce.domain.repository.ProductRepository
import com.example.e_commerce.data.repository.RemoteProductRepositoryImpl
import com.example.e_commerce.domain.repository.CartRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideMainApplication(@ApplicationContext context: Context): App {
        return context as App
    }
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://5fc9346b2af77700165ae514.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideProductRepository(productApiService: ProductApiService): ProductRepository {
        return RemoteProductRepositoryImpl(productApiService)
    }

    @Provides
    @Singleton
    fun provideLocalProductRepository(productDao: ProductDao): CartRepository {
        return CartRepositoryImpl(productDao)
    }

    @Provides
    @Singleton
    fun provideProductApiService(retrofit: Retrofit): ProductApiService {
        return retrofit.create(ProductApiService::class.java)
    }
}