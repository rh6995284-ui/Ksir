package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.BarbermanEntity
import com.example.data.local.TransactionEntity
import com.example.data.repository.BarbershopRepository
import com.example.ui.util.DateUtils
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

data class BarbermanStat(
    val name: String,
    val totalCuts: Int,
    val totalRevenue: Long,
    val transactionCount: Int
)

data class DailyStat(
    val dateKey: String,
    val dateMillis: Long,
    val formattedDate: String,
    val totalCuts: Int,
    val totalRevenue: Long,
    val transactionCount: Int
)

data class DashboardSummary(
    val todayCuts: Int = 0,
    val todayIncome: Long = 0L,
    val totalCutsOverall: Int = 0,
    val totalIncomeOverall: Long = 0L,
    val recentTransactions: List<TransactionEntity> = emptyList()
)

class BarbershopViewModel(
    private val repository: BarbershopRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.seedInitialBarbermenIfEmpty()
        }
    }

    val barbermen: StateFlow<List<BarbermanEntity>> = repository.allBarbermen
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val activeBarbermen: StateFlow<List<BarbermanEntity>> = repository.activeBarbermen
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val transactions: StateFlow<List<TransactionEntity>> = repository.allTransactions
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Derived Dashboard summary
    val dashboardSummary: StateFlow<DashboardSummary> = repository.allTransactions
        .map { txList ->
            val startToday = DateUtils.getStartOfDayMillis()
            val endToday = DateUtils.getEndOfDayMillis()

            var todayCuts = 0
            var todayIncome = 0L
            var totalCuts = 0
            var totalIncome = 0L

            for (tx in txList) {
                totalCuts += tx.cutCount
                totalIncome += tx.totalAmount

                if (tx.dateMillis in startToday..endToday) {
                    todayCuts += tx.cutCount
                    todayIncome += tx.totalAmount
                }
            }

            DashboardSummary(
                todayCuts = todayCuts,
                todayIncome = todayIncome,
                totalCutsOverall = totalCuts,
                totalIncomeOverall = totalIncome,
                recentTransactions = txList.take(5)
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardSummary()
        )

    // Derived recap based on Barberman
    val barbermanRecap: StateFlow<List<BarbermanStat>> = repository.allTransactions
        .map { txList ->
            val map = mutableMapOf<String, Triple<Int, Long, Int>>() // name -> (cuts, revenue, count)
            for (tx in txList) {
                val current = map[tx.barbermanName] ?: Triple(0, 0L, 0)
                map[tx.barbermanName] = Triple(
                    current.first + tx.cutCount,
                    current.second + tx.totalAmount,
                    current.third + 1
                )
            }
            map.map { (name, stats) ->
                BarbermanStat(
                    name = name,
                    totalCuts = stats.first,
                    totalRevenue = stats.second,
                    transactionCount = stats.third
                )
            }.sortedByDescending { it.totalRevenue }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Derived daily recap
    val dailyRecap: StateFlow<List<DailyStat>> = repository.allTransactions
        .map { txList ->
            val groups = txList.groupBy { DateUtils.getDateGroupKey(it.dateMillis) }
            groups.map { (dateKey, items) ->
                val firstItemDate = items.firstOrNull()?.dateMillis ?: System.currentTimeMillis()
                val totalCuts = items.sumOf { it.cutCount }
                val totalRevenue = items.sumOf { it.totalAmount }
                val label = if (DateUtils.isToday(firstItemDate)) {
                    "Hari Ini (${DateUtils.formatShortDate(firstItemDate)})"
                } else if (DateUtils.isYesterday(firstItemDate)) {
                    "Kemarin (${DateUtils.formatShortDate(firstItemDate)})"
                } else {
                    DateUtils.formatDate(firstItemDate)
                }

                DailyStat(
                    dateKey = dateKey,
                    dateMillis = firstItemDate,
                    formattedDate = label,
                    totalCuts = totalCuts,
                    totalRevenue = totalRevenue,
                    transactionCount = items.size
                )
            }.sortedByDescending { it.dateMillis }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Barberman Actions
    fun addBarberman(name: String, phone: String) {
        viewModelScope.launch {
            if (name.isNotBlank()) {
                repository.insertBarberman(
                    BarbermanEntity(name = name.trim(), phone = phone.trim())
                )
            }
        }
    }

    fun updateBarberman(entity: BarbermanEntity) {
        viewModelScope.launch {
            repository.updateBarberman(entity)
        }
    }

    fun deleteBarberman(id: Long) {
        viewModelScope.launch {
            repository.deleteBarberman(id)
        }
    }

    // Transaction Actions
    fun addTransaction(
        barbermanId: Long,
        barbermanName: String,
        cutCount: Int,
        pricePerCut: Long,
        dateMillis: Long = System.currentTimeMillis(),
        notes: String = ""
    ) {
        viewModelScope.launch {
            if (cutCount > 0 && pricePerCut > 0) {
                val total = cutCount.toLong() * pricePerCut
                repository.insertTransaction(
                    TransactionEntity(
                        barbermanId = barbermanId,
                        barbermanName = barbermanName,
                        cutCount = cutCount,
                        pricePerCut = pricePerCut,
                        totalAmount = total,
                        dateMillis = dateMillis,
                        notes = notes.trim()
                    )
                )
            }
        }
    }

    fun deleteTransaction(id: Long) {
        viewModelScope.launch {
            repository.deleteTransaction(id)
        }
    }
}

class BarbershopViewModelFactory(
    private val repository: BarbershopRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BarbershopViewModel::class.java)) {
            return BarbershopViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
