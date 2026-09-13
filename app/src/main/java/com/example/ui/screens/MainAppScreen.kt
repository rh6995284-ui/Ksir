package com.example.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.BlackBackground
import com.example.ui.theme.BlackSurface
import com.example.ui.theme.BlackSurfaceBorder
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.GoldVariant
import com.example.ui.theme.GrayText
import com.example.ui.theme.WhitePure
import com.example.ui.viewmodel.BarbershopViewModel

enum class MainNavigationTab(
    val title: String,
    val icon: ImageVector,
    val tag: String
) {
    HOME("Beranda", Icons.Default.Home, "nav_tab_home"),
    TRANSACTION("Transaksi", Icons.Default.ReceiptLong, "nav_tab_transaction"),
    BARBERMAN("Barberman", Icons.Default.People, "nav_tab_barberman"),
    REPORT("Laporan", Icons.Default.Assessment, "nav_tab_report")
}

@Composable
fun MainAppScreen(
    viewModel: BarbershopViewModel,
    modifier: Modifier = Modifier
) {
    var currentTab by remember { mutableStateOf(MainNavigationTab.HOME) }
    var showAddTransactionDialog by remember { mutableStateOf(false) }

    val barbermen by viewModel.barbermen.collectAsStateWithLifecycle()
    val activeBarbermen by viewModel.activeBarbermen.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val dashboardSummary by viewModel.dashboardSummary.collectAsStateWithLifecycle()
    val barbermanStats by viewModel.barbermanRecap.collectAsStateWithLifecycle()
    val dailyStats by viewModel.dailyRecap.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BlackBackground,
        bottomBar = {
            NavigationBar(
                containerColor = BlackSurface,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("bottom_navigation_bar")
            ) {
                MainNavigationTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BlackBackground,
                            selectedTextColor = GoldPrimary,
                            indicatorColor = GoldPrimary,
                            unselectedIconColor = GrayText,
                            unselectedTextColor = GrayText
                        ),
                        modifier = Modifier.testTag(tab.tag)
                    )
                }
            }
        },
        floatingActionButton = {
            if (currentTab == MainNavigationTab.HOME || currentTab == MainNavigationTab.TRANSACTION) {
                FloatingActionButton(
                    onClick = { showAddTransactionDialog = true },
                    containerColor = GoldPrimary,
                    contentColor = BlackBackground,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                    modifier = Modifier.testTag("fab_add_transaction")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah Transaksi",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MainNavigationTab.HOME -> {
                    HomeScreen(
                        summary = dashboardSummary,
                        onAddTransactionClick = { showAddTransactionDialog = true },
                        onNavigateToTransactions = { currentTab = MainNavigationTab.TRANSACTION },
                        onNavigateToBarbermen = { currentTab = MainNavigationTab.BARBERMAN },
                        onNavigateToReports = { currentTab = MainNavigationTab.REPORT }
                    )
                }
                MainNavigationTab.TRANSACTION -> {
                    TransactionScreen(
                        transactions = transactions,
                        onAddTransactionClick = { showAddTransactionDialog = true },
                        onDeleteTransaction = { id -> viewModel.deleteTransaction(id) }
                    )
                }
                MainNavigationTab.BARBERMAN -> {
                    BarbermanScreen(
                        barbermen = barbermen,
                        onAddBarberman = { name, phone -> viewModel.addBarberman(name, phone) },
                        onEditBarberman = { entity -> viewModel.updateBarberman(entity) },
                        onDeleteBarberman = { id -> viewModel.deleteBarberman(id) }
                    )
                }
                MainNavigationTab.REPORT -> {
                    ReportScreen(
                        summary = dashboardSummary,
                        barbermanStats = barbermanStats,
                        dailyStats = dailyStats
                    )
                }
            }
        }
    }

    if (showAddTransactionDialog) {
        AddTransactionDialog(
            barbermen = if (activeBarbermen.isNotEmpty()) activeBarbermen else barbermen,
            onDismiss = { showAddTransactionDialog = false },
            onSave = { barbermanId, barbermanName, cutCount, pricePerCut, dateMillis, notes ->
                viewModel.addTransaction(
                    barbermanId = barbermanId,
                    barbermanName = barbermanName,
                    cutCount = cutCount,
                    pricePerCut = pricePerCut,
                    dateMillis = dateMillis,
                    notes = notes
                )
            },
            onNavigateToAddBarberman = {
                currentTab = MainNavigationTab.BARBERMAN
            }
        )
    }
}
