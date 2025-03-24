package com.example.mvvmtask.model.network

import com.example.mvvmtask.model.pojo.ProductResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun getProducts(): Response<ProductResponse> // retrofit is not compatible with flow to listen with api
}
