package com.example.e_commerce.di

import androidx.room.Room
import com.example.e_commerce.App
import com.example.e_commerce.data.local.database.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        application: App
    ): ProductDatabase {
        return Room.databaseBuilder(
            application, ProductDatabase::class.java, "products.db"
        ).fallbackToDestructiveMigration().build()
    }


    @Provides
    @Singleton
    fun provideProductDao(
        productDatabase: ProductDatabase
    ) = productDatabase.productDao()
}