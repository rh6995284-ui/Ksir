package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BarbermanDao {
    @Query("SELECT * FROM barbermen ORDER BY name ASC")
    fun getAllBarbermen(): Flow<List<BarbermanEntity>>

    @Query("SELECT * FROM barbermen WHERE isActive = 1 ORDER BY name ASC")
    fun getActiveBarbermen(): Flow<List<BarbermanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBarberman(barberman: BarbermanEntity): Long

    @Update
    suspend fun updateBarberman(barberman: BarbermanEntity)

    @Query("DELETE FROM barbermen WHERE id = :id")
    suspend fun deleteBarbermanById(id: Long)

    @Query("SELECT COUNT(*) FROM barbermen")
    suspend fun getBarbermanCount(): Int
}
