package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.TransactionEntity
import com.example.ui.theme.BlackBackground
import com.example.ui.theme.BlackSurface
import com.example.ui.theme.BlackSurfaceBorder
import com.example.ui.theme.BlackSurfaceVariant
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.GoldVariant
import com.example.ui.theme.GrayText
import com.example.ui.theme.WhitePure
import com.example.ui.util.CurrencyUtils
import com.example.ui.util.DateUtils
import com.example.ui.viewmodel.DashboardSummary

@Composable
fun HomeScreen(
    summary: DashboardSummary,
    onAddTransactionClick: () -> Unit,
    onNavigateToTransactions: () -> Unit,
    onNavigateToBarbermen: () -> Unit,
    onNavigateToReports: () -> Unit,
    modifier: Modifier = Modifier
) {
    val todayMillis = System.currentTimeMillis()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BlackBackground)
            .testTag("home_screen"),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 1. Barbershop Brand Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = BlackSurface),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        listOf(GoldPrimary.copy(alpha = 0.7f), BlackSurfaceBorder)
                    )
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo Image
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .border(2.dp, GoldPrimary, CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.khc_barber_logo),
                            contentDescription = "Logo KHC Barbershop",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = GoldContainer,
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Text(
                                text = "OFFICIAL CASHIER",
                                color = GoldVariant,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = "KHC BARBERSHOP",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.5.sp,
                                color = WhitePure
                            )
                        )

                        Text(
                            text = "Modern Haircuts & Grooming",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = GrayText,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                }
            }
        }

        // 2. Date Pill
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = DateUtils.formatDate(todayMillis),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = GrayText,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BlackSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BlackSurfaceBorder)
                ) {
                    Text(
                        text = "Hari Ini",
                        color = GoldVariant,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // 3. Highlight Metrics: Potong Rambut Hari Ini & Pendapatan Hari Ini
        item {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // Total Pendapatan Hari Ini (Prominent Hero Card)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = BlackSurface),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldPrimary)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TOTAL PENDAPATAN HARI INI",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GoldVariant,
                                    letterSpacing = 1.sp
                                )
                            )
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(GoldContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AttachMoney,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = CurrencyUtils.formatRupiah(summary.todayIncome),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = GoldPrimary,
                                fontSize = 32.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Rupiah Indonesia (IDR)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = GrayText
                            )
                        )
                    }
                }

                // Jumlah Potong Rambut Hari Ini Card
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = BlackSurfaceVariant),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BlackSurfaceBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(BlackSurface),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCut,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Potong Hari Ini",
                                style = MaterialTheme.typography.labelMedium.copy(color = GrayText)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "${summary.todayCuts}",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = WhitePure
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "orang",
                                    modifier = Modifier.padding(bottom = 2.dp),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = GrayText
                                    )
                                )
                            }
                        }
                    }

                    // Total Keseluruhan
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = BlackSurfaceVariant),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BlackSurfaceBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(BlackSurface),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = GoldVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Total Riwayat",
                                style = MaterialTheme.typography.labelMedium.copy(color = GrayText)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "${summary.totalCutsOverall}",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = WhitePure
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "cukur",
                                    modifier = Modifier.padding(bottom = 2.dp),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = GrayText
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Action Button: Tambah Transaksi
        item {
            Button(
                onClick = onAddTransactionClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("add_transaction_main_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = BlackBackground,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Tambah Transaksi Baru",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BlackBackground
                    )
                )
            }
        }

        // 5. Recent Transactions Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Transaksi Terbaru",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = WhitePure
                    )
                )

                if (summary.recentTransactions.isNotEmpty()) {
                    Row(
                        modifier = Modifier.clickable { onNavigateToTransactions() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Lihat Semua",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = GoldVariant,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = GoldVariant,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // 6. Recent Transaction Items
        if (summary.recentTransactions.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BlackSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BlackSurfaceBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(BlackSurfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCut,
                                contentDescription = null,
                                tint = GrayText,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Belum Ada Transaksi",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = WhitePure
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Klik tombol 'Tambah Transaksi Baru' untuk mencatat pelanggan pertama hari ini.",
                            style = MaterialTheme.typography.bodySmall.copy(color = GrayText),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(summary.recentTransactions) { tx ->
                TransactionCardItem(tx = tx)
            }
        }
    }
}

@Composable
fun TransactionCardItem(
    tx: TransactionEntity,
    onDelete: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BlackSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, BlackSurfaceBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(GoldContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tx.barbermanName.take(1).uppercase(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = tx.barbermanName,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = WhitePure
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${tx.cutCount} potong @ ${CurrencyUtils.formatRupiah(tx.pricePerCut)}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = GrayText
                        )
                    )
                    if (tx.notes.isNotBlank()) {
                        Text(
                            text = tx.notes,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = GoldVariant,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        )
                    }
                    Text(
                        text = "${DateUtils.formatShortDate(tx.dateMillis)} • ${DateUtils.formatTime(tx.dateMillis)}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = GrayText.copy(alpha = 0.7f),
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = CurrencyUtils.formatRupiah(tx.totalAmount),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = GoldPrimary
                    )
                )

                if (onDelete != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Hapus",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { onDelete() }
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}
