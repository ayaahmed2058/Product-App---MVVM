package com.example.mvvmtask.model.db


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductDataClass>> //listen - observe - don't use suspend because we talk to an external data source

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(product: ProductDataClass):Long

    @Delete
    suspend fun deleteProduct(product: ProductDataClass):Int

}
