package com.example.e_commerce.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.e_commerce.data.local.database.dao.ProductDao
import com.example.e_commerce.data.local.model.ProductDaoModel

@Database(entities = [ProductDaoModel::class], version = 1, exportSchema = false)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}