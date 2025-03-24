package com.example.mvvmtask.allProduct.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mvvmtask.model.Response
import com.example.mvvmtask.model.repository.Repository
import com.example.mvvmtask.model.repository.RepositoryInterface
import com.example.mvvmtask.model.db.ProductDataClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

//hot stream
class AllProductViewModel(private var repo: RepositoryInterface):ViewModel() {

    private val mutableProducts =  MutableStateFlow<Response>(Response.Loading)
    val products = mutableProducts.asStateFlow()

    private val mutableMessage =  MutableSharedFlow<String>()
    val message= mutableMessage.asSharedFlow()


//    private val mutableProducts: MutableLiveData<List<ProductDataClass>> = MutableLiveData()
//    val products: LiveData<List<ProductDataClass>> = mutableProducts

//    private val mutableMessage: MutableLiveData<String> = MutableLiveData("")
//    val message: LiveData<String> = mutableMessage

//    fun getProducts(){
//        viewModelScope.launch (Dispatchers.IO){
//            try {
//                val result = repo.getAllProduct(true)
//                val list:List<ProductDataClass> = result
//                mutableProducts.postValue(list)
//            }catch (ex:Exception){
//                mutableMessage.postValue("an error occurs ${ex.message}")
//            }
//        }
//    }

    init {
        getAllProduct()
    }

    fun addToFav (product: ProductDataClass?){
        if(product != null){
            viewModelScope.launch (Dispatchers.IO){
                try {
                    val result = repo.insertProduct(product)
                    if(result>0){
                        mutableMessage.emit("Product added successfully")
                    }else{
                        mutableMessage.emit("Product os already exit")
                    }
                }catch (ex:Exception){
                    mutableMessage.emit("an error occurs ${ex.message}")
                }
            }
        }else{
           // mutableMessage.emit("we can't add product")
        }
    }

    fun getAllProduct(){
        viewModelScope.launch {
            try {
                val products = repo.getAllProduct(true)
                products
                    .catch {
                        ex -> mutableProducts.value = Response.Failure(ex)
                        mutableMessage.emit("Error From API: ${ex.message}")
                    }
                    .collect{
                        mutableProducts.value = Response.Success(it)
                    }
            }catch (ex: Exception){
                mutableProducts.value = Response.Failure(ex)
                mutableMessage.emit("an error occurs ${ex.message}")
            }
        }
    }
}

class AllProductViewModelFactory (private val repo: Repository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllProductViewModel(repo) as T
    }
}