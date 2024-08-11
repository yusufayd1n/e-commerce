package com.example.e_commerce.data.local.database.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.e_commerce.data.local.model.ProductDaoModel
import com.example.e_commerce.data.local.model.ProductType
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductDaoModel)

    @Query("UPDATE products SET quantity = :quantity WHERE name = :name AND model = :model AND type = :type")
    suspend fun updateProductQuantity(name: String, model: String, type: ProductType, quantity: Int)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteProduct(id: String)

    @Query("SELECT * FROM products WHERE type = :type")
    fun getProductsByType(type: ProductType): Flow<MutableList<ProductDaoModel>>
}