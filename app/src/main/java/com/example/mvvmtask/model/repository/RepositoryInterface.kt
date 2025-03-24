package com.example.mvvmtask.model.repository

import com.example.mvvmtask.model.db.ProductDataClass
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    suspend fun getAllProduct(isOnline: Boolean): Flow<List<ProductDataClass>>
    suspend fun insertProduct(product: ProductDataClass): Long
    suspend fun deleteProduct(product: ProductDataClass): Int
}