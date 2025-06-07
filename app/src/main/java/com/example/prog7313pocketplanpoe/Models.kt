package com.example.pocketplan

import android.net.Uri

data class Transaction(
    val id: Long,
    val amount: Double,
    val category: String,
    val date: String,
    val receiptUri: Uri? = null
)

data class Category(
    val id: Int,
    val name: String
)