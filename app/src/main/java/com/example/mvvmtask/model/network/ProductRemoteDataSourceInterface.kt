package com.example.mvvmtask.model.network

import com.example.mvvmtask.model.db.ProductDataClass
import kotlinx.coroutines.flow.Flow

interface ProductRemoteDataSourceInterface {

    suspend fun getAllProduct (): Flow<List<ProductDataClass>>
}