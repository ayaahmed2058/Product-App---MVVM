package com.example.mvvmtask.allProduct.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mvvmtask.R
import com.example.mvvmtask.model.repository.Repository
import com.example.mvvmtask.model.db.ProductDataClass
import com.example.mvvmtask.model.db.ProductDatabase
import com.example.mvvmtask.model.db.ProductLocalDataSourceImp
import com.example.mvvmtask.model.network.ProductRemoteDataSource
import com.example.mvvmtask.model.network.RetrofitInstance
import com.example.mvvmtask.allProduct.viewModel.AllProductViewModel
import com.example.mvvmtask.allProduct.viewModel.AllProductViewModelFactory
import com.example.mvvmtask.model.Response
import kotlinx.coroutines.flow.collect

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val factory = AllProductViewModelFactory(
                Repository.getInstance(
                ProductRemoteDataSource(RetrofitInstance.api),
                ProductLocalDataSourceImp(ProductDatabase.getInstance(this).productDao())
            ))

            //val viewModel = ViewModelProvider(this,factory).get(AllProductViewModel::class.java)

            val viewModel: AllProductViewModel = viewModel(factory = factory)
            
            AllProductScreen(viewModel = viewModel)

        }
    }
}

@Composable
fun AllProductScreen(viewModel: AllProductViewModel){
    val context = LocalContext.current
    viewModel.getAllProduct()
    val uiState by viewModel.products.collectAsStateWithLifecycle()

    val productState : List<ProductDataClass>
    val messageState : Throwable

//    //= viewModel.products.observeAsState()
//    val messageState = viewModel.message.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.message.collect{
            message -> Toast.makeText(context,message,Toast.LENGTH_LONG).show()
        }
    }

    when(uiState){

        is Response.Loading -> {
            LoadingIndicator()
        }

        is Response.Success -> {

            productState = (uiState as Response.Success).data

            val snackBarHostState = remember {
                SnackbarHostState()
            }

            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackBarHostState)}
            ) {contentPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center

                ) {
                    LazyColumn {
                        items(productState?.size?:0){it -> Int
                            ProductItem(productState?.get(it), "Favorite") {
                                viewModel.addToFav(productState?.get(it))
                                Toast.makeText(context,"Product Added Successfully",Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }

        is Response.Failure -> {
            messageState = (uiState as Response.Failure).error
            LaunchedEffect(messageState) {
                Toast.makeText(context, "Error: $messageState", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    LaunchedEffect(messageState.value) {
//        if(!messageState.value.isNullOrBlank()){
//            snackBarHostState.showSnackbar(
//                message = messageState.value.toString(),
//                duration =  SnackbarDuration.Short
//            )
//        }
//    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductItem(
    product: ProductDataClass?,
    actionName: String, action:() -> Unit
) {
    Card(
        modifier = Modifier.padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .background(color = colorResource(id = R.color.teal_700))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                GlideImage(
                    model = product?.thumbnail,
                    contentDescription = "image",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(10.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = product?.title.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = product?.price.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = product?.brand.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = product?.description.toString(),
                fontSize = 20.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                colors = ButtonDefaults.buttonColors(colorResource(R.color.brown)),
                modifier = Modifier.align(Alignment.End),
                onClick = action,
            ) {
                Text(actionName, color = Color.Black)
            }
        }
    }
}



@Composable
public fun LoadingIndicator() {
    Row(
        modifier = Modifier.fillMaxSize(),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            progress = 1f,
            modifier = Modifier
                .size(50.dp),
            color = Color(0xFFBB86FC),
            trackColor = Color.LightGray
        )
    }
}