package com.example.mvvmtask.model

import com.example.mvvmtask.model.db.ProductDataClass

sealed class Response {

    data object Loading : Response()
    data class Success (val data : List<ProductDataClass>) : Response()
    data class Failure (val error: Throwable) : Response()
}