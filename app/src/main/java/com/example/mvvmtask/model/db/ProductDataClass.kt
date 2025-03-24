package com.example.mvvmtask.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "products")
data class ProductDataClass(@PrimaryKey val id: Int ,
                            val title: String ,
                            var brand: String ,
                            val description: String ,
                            val price: Double,
                            val thumbnail: String)
