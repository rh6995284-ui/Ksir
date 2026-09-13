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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.viewmodel.BarbermanStat
import com.example.ui.viewmodel.DailyStat
import com.example.ui.viewmodel.DashboardSummary

enum class ReportTab(val title: String) {
    BARBERMAN("Rekap Barberman"),
    DAILY("Rekap Harian")
}

@Composable
fun ReportScreen(
    summary: DashboardSummary,
    barbermanStats: List<BarbermanStat>,
    dailyStats: List<DailyStat>,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(ReportTab.BARBERMAN) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BlackBackground)
            .testTag("report_screen"),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. Header
        item {
            Column {
                Text(
                    text = "Laporan & Rekapitulasi",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = WhitePure
                    )
                )
                Text(
                    text = "Ringkasan data pendapatan KHC Barbershop",
                    style = MaterialTheme.typography.bodySmall.copy(color = GrayText)
                )
            }
        }

        // 2. Global Metric Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Total Potong
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = BlackSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BlackSurfaceBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(GoldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCut,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "TOTAL POTONG",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GrayText,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${summary.totalCutsOverall} kali",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = WhitePure
                            )
                        )
                    }
                }

                // Total Pendapatan
                Card(
                    modifier = Modifier.weight(1.3f),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = BlackSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.6f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(GoldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Assessment,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "TOTAL PENDAPATAN",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GoldVariant,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = CurrencyUtils.formatRupiah(summary.totalIncomeOverall),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = GoldPrimary,
                                fontSize = 18.sp
                            )
                        )
                    }
                }
            }
        }

        // 3. Tab Switcher (Barberman vs Harian)
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = BlackSurfaceVariant,
                border = androidx.compose.foundation.BorderStroke(1.dp, BlackSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    ReportTab.values().forEach { tab ->
                        val isSelected = selectedTab == tab
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) GoldPrimary else androidx.compose.ui.graphics.Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedTab = tab }
                        ) {
                            Text(
                                text = tab.title,
                                color = if (isSelected) BlackBackground else WhitePure,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 14.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }
                    }
                }
            }
        }

        // 4. Content based on Tab
        when (selectedTab) {
            ReportTab.BARBERMAN -> {
                if (barbermanStats.isEmpty()) {
                    item {
                        EmptyReportCard("Belum ada data transaksi barberman.")
                    }
                } else {
                    itemsIndexed(barbermanStats) { index, stat ->
                        BarbermanReportCard(index = index, stat = stat)
                    }
                }
            }
            ReportTab.DAILY -> {
                if (dailyStats.isEmpty()) {
                    item {
                        EmptyReportCard("Belum ada data rekap harian.")
                    }
                } else {
                    items(dailyStats) { stat ->
                        DailyReportCard(stat = stat)
                    }
                }
            }
        }
    }
}

@Composable
fun BarbermanReportCard(index: Int, stat: BarbermanStat) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BlackSurface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (index == 0) GoldPrimary.copy(alpha = 0.7f) else BlackSurfaceBorder
        )
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
                // Rank Badge / Icon
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (index == 0) GoldContainer else BlackSurfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    if (index == 0) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Peringkat 1",
                            tint = GoldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text(
                            text = "#${index + 1}",
                            fontWeight = FontWeight.Bold,
                            color = GrayText,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = stat.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = WhitePure
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${stat.totalCuts} orang potong (${stat.transactionCount} transaksi)",
                        style = MaterialTheme.typography.bodySmall.copy(color = GrayText)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = CurrencyUtils.formatRupiah(stat.totalRevenue),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = GoldPrimary
                    )
                )
                Text(
                    text = "Total Kontribusi",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = GrayText.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}

@Composable
fun DailyReportCard(stat: DailyStat) {
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
                        .background(BlackSurfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = GoldVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = stat.formattedDate,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = WhitePure
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${stat.totalCuts} orang dicukur (${stat.transactionCount} transaksi)",
                        style = MaterialTheme.typography.bodySmall.copy(color = GrayText)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = CurrencyUtils.formatRupiah(stat.totalRevenue),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = GoldPrimary
                    )
                )
                Text(
                    text = "Pendapatan Hari",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = GrayText.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}

@Composable
fun EmptyReportCard(message: String) {
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
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(color = GrayText)
            )
        }
    }
}
