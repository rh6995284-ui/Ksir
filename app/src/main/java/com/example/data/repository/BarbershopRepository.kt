package com.example.data.repository

import com.example.data.local.BarbermanDao
import com.example.data.local.BarbermanEntity
import com.example.data.local.TransactionDao
import com.example.data.local.TransactionEntity
import kotlinx.coroutines.flow.Flow

class BarbershopRepository(
    private val barbermanDao: BarbermanDao,
    private val transactionDao: TransactionDao
) {
    val allBarbermen: Flow<List<BarbermanEntity>> = barbermanDao.getAllBarbermen()
    val activeBarbermen: Flow<List<BarbermanEntity>> = barbermanDao.getActiveBarbermen()
    val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()

    fun getTransactionsForDateRange(startOfDay: Long, endOfDay: Long): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactionsForDateRange(startOfDay, endOfDay)
    }

    suspend fun insertBarberman(barberman: BarbermanEntity): Long {
        return barbermanDao.insertBarberman(barberman)
    }

    suspend fun updateBarberman(barberman: BarbermanEntity) {
        barbermanDao.updateBarberman(barberman)
    }

    suspend fun deleteBarberman(id: Long) {
        barbermanDao.deleteBarbermanById(id)
    }

    suspend fun insertTransaction(transaction: TransactionEntity): Long {
        return transactionDao.insertTransaction(transaction)
    }

    suspend fun deleteTransaction(id: Long) {
        transactionDao.deleteTransactionById(id)
    }

    suspend fun seedInitialBarbermenIfEmpty() {
        val count = barbermanDao.getBarbermanCount()
        if (count == 0) {
            barbermanDao.insertBarberman(
                BarbermanEntity(name = "Mas Dimas", phone = "08123456789")
            )
            barbermanDao.insertBarberman(
                BarbermanEntity(name = "Rian Barber", phone = "08234567890")
            )
            barbermanDao.insertBarberman(
                BarbermanEntity(name = "Bang Andi", phone = "08345678901")
            )
        }
    }
}
