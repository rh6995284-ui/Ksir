package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TransactionEntity
import com.example.ui.theme.BlackBackground
import com.example.ui.theme.BlackSurface
import com.example.ui.theme.BlackSurfaceBorder
import com.example.ui.theme.BlackSurfaceVariant
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.GoldVariant
import com.example.ui.theme.GrayText
import com.example.ui.theme.WhitePure
import com.example.ui.util.CurrencyUtils
import com.example.ui.util.DateUtils

enum class TransactionFilter(val label: String) {
    ALL("Semua"),
    TODAY("Hari Ini"),
    YESTERDAY("Kemarin")
}

@Composable
fun TransactionScreen(
    transactions: List<TransactionEntity>,
    onAddTransactionClick: () -> Unit,
    onDeleteTransaction: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf(TransactionFilter.ALL) }
    var transactionToDelete by remember { mutableStateOf<TransactionEntity?>(null) }

    val filteredList = remember(transactions, selectedFilter) {
        when (selectedFilter) {
            TransactionFilter.ALL -> transactions
            TransactionFilter.TODAY -> transactions.filter { DateUtils.isToday(it.dateMillis) }
            TransactionFilter.YESTERDAY -> transactions.filter { DateUtils.isYesterday(it.dateMillis) }
        }
    }

    val totalCuts = filteredList.sumOf { it.cutCount }
    val totalRevenue = filteredList.sumOf { it.totalAmount }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BlackBackground)
            .testTag("transaction_screen"),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Action & Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Riwayat Transaksi",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = WhitePure
                        )
                    )
                    Text(
                        text = "Pencatatan potong rambut harian",
                        style = MaterialTheme.typography.bodySmall.copy(color = GrayText)
                    )
                }

                Button(
                    onClick = onAddTransactionClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("add_transaction_screen_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = BlackBackground,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Tambah",
                        fontWeight = FontWeight.Bold,
                        color = BlackBackground,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Summary Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BlackSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "TOTAL PENDAPATAN (${selectedFilter.label.uppercase()})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GoldVariant,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = CurrencyUtils.formatRupiah(totalRevenue),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = GoldPrimary,
                                fontSize = 24.sp
                            )
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "JUMLAH POTONG",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GrayText,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$totalCuts orang",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = WhitePure
                            )
                        )
                    }
                }
            }
        }

        // Filter Pills
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(TransactionFilter.values()) { filter ->
                    val isSelected = selectedFilter == filter
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedFilter = filter },
                        color = if (isSelected) GoldPrimary else BlackSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) GoldPrimary else BlackSurfaceBorder
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = filter.label,
                            color = if (isSelected) BlackBackground else WhitePure,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Transactions List
        if (filteredList.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BlackSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BlackSurfaceBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(GoldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCut,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Tidak Ada Transaksi",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = WhitePure
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Belum ada riwayat transaksi untuk filter '${selectedFilter.label}'.",
                            style = MaterialTheme.typography.bodySmall.copy(color = GrayText)
                        )
                    }
                }
            }
        } else {
            items(filteredList, key = { it.id }) { tx ->
                TransactionCardItem(
                    tx = tx,
                    onDelete = { transactionToDelete = tx }
                )
            }
        }
    }

    transactionToDelete?.let { target ->
        AlertDialog(
            onDismissRequest = { transactionToDelete = null },
            containerColor = BlackSurface,
            title = {
                Text(
                    text = "Hapus Transaksi?",
                    color = WhitePure,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Hapus transaksi oleh '${target.barbermanName}' sebesar ${CurrencyUtils.formatRupiah(target.totalAmount)}?",
                    color = GrayText
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteTransaction(target.id)
                        transactionToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Hapus", color = WhitePure)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { transactionToDelete = null }
                ) {
                    Text("Batal", color = GrayText)
                }
            }
        )
    }
}
