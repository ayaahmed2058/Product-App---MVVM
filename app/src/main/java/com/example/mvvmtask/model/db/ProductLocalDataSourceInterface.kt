package com.example.mvvmtask.model.db

import kotlinx.coroutines.flow.Flow


interface ProductLocalDataSourceInterface {
    suspend fun addProduct(product: ProductDataClass):Long

    suspend fun getStoredProducts(): Flow<List<ProductDataClass>>

    suspend fun deleteProduct(product: ProductDataClass?):Int
}