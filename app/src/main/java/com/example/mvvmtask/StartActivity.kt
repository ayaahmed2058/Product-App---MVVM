package com.example.mvvmtask

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mvvmtask.allProduct.view.MainActivity
import com.example.mvvmtask.favProduct.view.FavProduct


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun StartScreen() {

    var context = LocalContext.current

    Column ( modifier = Modifier
        .fillMaxSize()
        .padding(top = 90.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Image(
            painter = painterResource(id = R.drawable.main_img),
            contentDescription = "Main Image",
            modifier = Modifier
                .size(300.dp)
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.brown)),

        ) {
            Text("get All Product", color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val intent = Intent(context, FavProduct::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.brown)),

        ) {
            Text("get Fav Product", color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

//        Button(
//            onClick = {
//
//            },
//            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.brown)),
//
//            ) {
//            Text("Exit", color = Color.White)
//        }
    }


}

class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StartScreen()
        }
    }
}