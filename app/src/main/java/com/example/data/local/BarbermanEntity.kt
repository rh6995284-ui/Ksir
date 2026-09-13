package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "barbermen")
data class BarbermanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String = "",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
