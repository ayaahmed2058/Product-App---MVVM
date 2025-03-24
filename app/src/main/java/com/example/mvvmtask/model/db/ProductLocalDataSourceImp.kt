package com.example.mvvmtask.model.db

import kotlinx.coroutines.flow.Flow


class ProductLocalDataSourceImp(private val dao: ProductDao): ProductLocalDataSourceInterface {



    override suspend fun addProduct(product: ProductDataClass):Long {
        return dao.insertProducts(product)
    }

    override suspend fun getStoredProducts(): Flow<List<ProductDataClass>> {
        return dao.getAllProducts()
    }

    override suspend fun deleteProduct(product: ProductDataClass?):Int {
        return if(product!=null)
            dao.deleteProduct(product)
        else
            -1
    }
}
