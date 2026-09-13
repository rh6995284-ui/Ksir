package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val barbermanId: Long,
    val barbermanName: String,
    val cutCount: Int,
    val pricePerCut: Long,
    val totalAmount: Long, // cutCount * pricePerCut
    val dateMillis: Long,
    val notes: String = ""
)
