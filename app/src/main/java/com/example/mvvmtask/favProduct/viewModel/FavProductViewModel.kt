package com.example.mvvmtask.favProduct.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mvvmtask.model.Response
import com.example.mvvmtask.model.db.ProductDataClass
import com.example.mvvmtask.model.repository.Repository
import com.example.mvvmtask.model.repository.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavProductViewModel (private var repo: RepositoryInterface): ViewModel() {

    private val mutableProducts =  MutableStateFlow<Response>(Response.Loading)
    val products = mutableProducts.asStateFlow()

    private val mutableMessage =  MutableSharedFlow<String>()
    val message= mutableMessage.asSharedFlow()

//    private val mutableProducts: MutableLiveData<List<ProductDataClass>> = MutableLiveData()
//    val products: LiveData<List<ProductDataClass>> = mutableProducts
//
//    private val mutableMessage: MutableLiveData<String> = MutableLiveData("")
//    val message: LiveData<String> = mutableMessage

//    fun getStoredProducts(){
//        viewModelScope.launch (Dispatchers.IO){
//            try {
//                val result = repo.getAllProduct(false)
//                val list:List<ProductDataClass> = result
//                mutableProducts.postValue(list)
//            }catch (ex:Exception){
//                mutableMessage.postValue("an error occurs ${ex.message}")
//            }
//        }
//    }
    fun deleteFromFav (product: ProductDataClass?){
        if(product != null){
            viewModelScope.launch (Dispatchers.IO){
                try {
                    val result = repo.deleteProduct(product)
                    //getStoredProducts()
                    if(result>0){
                        mutableMessage.emit("Product deleted successfully")
                    }else{
                        mutableMessage.emit("Product doesn't found")
                    }
                }catch (ex:Exception){
                    mutableMessage.emit("an error occurs ${ex.message}")
                }
            }
        }else{
            //mutableMessage.emit("we can't delete product")
        }
    }

    fun getLocalProduct (){ //listen direct
        viewModelScope.launch {
            val localProducts = repo.getAllProduct(false)
            localProducts
                .catch {
                    ex -> mutableProducts.value = Response.Failure(ex)
                    mutableMessage.emit("Error From API: ${ex.message}")
                }
                .collect{
                mutableProducts.value = Response.Success(it)
            }
        }
    }
}

class FavProductViewModelFactory (private val repo: Repository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavProductViewModel(repo) as T
    }
}