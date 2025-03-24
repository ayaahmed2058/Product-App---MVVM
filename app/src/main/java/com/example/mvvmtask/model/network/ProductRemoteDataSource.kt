package com.example.mvvmtask.model.network

import com.example.mvvmtask.model.db.ProductDataClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ProductRemoteDataSource(private val service: ApiService): ProductRemoteDataSourceInterface {

    override suspend fun getAllProduct(): Flow<List<ProductDataClass>> {

            val response = service.getProducts().body()?.products ?: emptyList()
            return flowOf(response)

    }

}