package com.example.mvvmtask.model.repository

import com.example.mvvmtask.model.db.ProductDataClass
import com.example.mvvmtask.model.db.ProductLocalDataSourceInterface
import com.example.mvvmtask.model.network.ProductRemoteDataSourceInterface
import kotlinx.coroutines.flow.Flow

class Repository private constructor(
    private val productRemoteDataSource: ProductRemoteDataSourceInterface,
    private val productLocalDataSource: ProductLocalDataSourceInterface
) : RepositoryInterface {

    override suspend fun getAllProduct(isOnline: Boolean): Flow<List<ProductDataClass>> {
        return if(isOnline){
            productRemoteDataSource.getAllProduct()
        }else{
            productLocalDataSource.getStoredProducts()
        }
    }

    override suspend fun insertProduct (product: ProductDataClass):Long{
        return productLocalDataSource.addProduct(product)
    }

    override suspend fun deleteProduct(product: ProductDataClass):Int{
        return productLocalDataSource.deleteProduct(product)
    }

    companion object{
        private var INSTANCE: Repository? = null
        fun getInstance(productRemoteDataSource: ProductRemoteDataSourceInterface,
                        productLocalDataSource: ProductLocalDataSourceInterface
        ): Repository {
            return INSTANCE ?: synchronized(this){
                val temp = Repository(productRemoteDataSource,productLocalDataSource)
                INSTANCE = temp
                temp
            }
        }
    }
}
