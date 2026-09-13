package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.BarbermanEntity
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddTransactionDialog(
    barbermen: List<BarbermanEntity>,
    onDismiss: () -> Unit,
    onSave: (barbermanId: Long, barbermanName: String, cutCount: Int, pricePerCut: Long, dateMillis: Long, notes: String) -> Unit,
    onNavigateToAddBarberman: () -> Unit
) {
    var selectedBarberman by remember(barbermen) {
        mutableStateOf(barbermen.firstOrNull())
    }
    var cutCount by remember { mutableIntStateOf(1) }
    var pricePerCut by remember { mutableLongStateOf(30000L) }
    var customPriceInput by remember { mutableStateOf("30000") }
    var notes by remember { mutableStateOf("") }
    val dateMillis = remember { System.currentTimeMillis() }

    val totalAmount = cutCount.toLong() * pricePerCut

    val presetPrices = listOf(25000L, 30000L, 35000L, 40000L, 50000L)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("add_transaction_dialog"),
            shape = RoundedCornerShape(24.dp),
            color = BlackSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(GoldContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCut,
                            contentDescription = "Potong",
                            tint = GoldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Tambah Transaksi",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = WhitePure
                            )
                        )
                        Text(
                            text = DateUtils.formatDate(dateMillis),
                            style = MaterialTheme.typography.bodySmall.copy(color = GrayText)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Section 1: Pilih Barberman
                Text(
                    text = "1. Pilih Barberman",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = GoldVariant
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (barbermen.isEmpty()) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = BlackSurfaceVariant),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Belum ada data barberman.",
                                color = GrayText,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    onDismiss()
                                    onNavigateToAddBarberman()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                            ) {
                                Text("Tambah Barberman Baru", color = BlackBackground)
                            }
                        }
                    }
                } else {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        barbermen.forEach { barber ->
                            val isSelected = selectedBarberman?.id == barber.id
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { selectedBarberman = barber }
                                    .border(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) GoldPrimary else BlackSurfaceBorder,
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                color = if (isSelected) GoldContainer else BlackSurfaceVariant,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isSelected) Icons.Default.Check else Icons.Default.Person,
                                        contentDescription = null,
                                        tint = if (isSelected) GoldPrimary else GrayText,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = barber.name,
                                        color = if (isSelected) GoldPrimary else WhitePure,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Section 2: Jumlah Potong Rambut
                Text(
                    text = "2. Jumlah Potong Rambut",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = GoldVariant
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = BlackSurfaceVariant),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BlackSurfaceBorder)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            IconButton(
                                onClick = { if (cutCount > 1) cutCount-- },
                                enabled = cutCount > 1
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Kurang",
                                    tint = if (cutCount > 1) WhitePure else GrayText
                                )
                            }

                            Text(
                                text = "$cutCount",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary
                                ),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            IconButton(
                                onClick = { cutCount++ }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Tambah",
                                    tint = WhitePure
                                )
                            }
                        }
                    }

                    Text(
                        text = "Orang / Pelanggan",
                        style = MaterialTheme.typography.bodyMedium.copy(color = GrayText)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Section 3: Nominal Harga per Potong
                Text(
                    text = "3. Nominal Harga / Potong",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = GoldVariant
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Quick presets
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    presetPrices.forEach { price ->
                        val isSelected = pricePerCut == price
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    pricePerCut = price
                                    customPriceInput = price.toString()
                                }
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) GoldPrimary else BlackSurfaceBorder,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            color = if (isSelected) GoldContainer else BlackSurfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = CurrencyUtils.formatRupiah(price),
                                color = if (isSelected) GoldPrimary else WhitePure,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = customPriceInput,
                    onValueChange = { input ->
                        val cleanDigits = input.filter { it.isDigit() }
                        customPriceInput = cleanDigits
                        val parsed = cleanDigits.toLongOrNull() ?: 0L
                        pricePerCut = parsed
                    },
                    label = { Text("Nominal Khusus (Rp)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = BlackSurfaceBorder,
                        focusedLabelColor = GoldPrimary,
                        unfocusedLabelColor = GrayText,
                        focusedTextColor = WhitePure,
                        unfocusedTextColor = WhitePure
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Catatan / Keterangan (Opsional)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = BlackSurfaceBorder,
                        focusedLabelColor = GoldPrimary,
                        unfocusedLabelColor = GrayText,
                        focusedTextColor = WhitePure,
                        unfocusedTextColor = WhitePure
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Otomatis Hitung Total Pendapatan
                Card(
                    colors = CardDefaults.cardColors(containerColor = GoldContainer),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "TOTAL PENDAPATAN (OTOMATIS)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GoldVariant,
                                letterSpacing = 1.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = CurrencyUtils.formatRupiah(totalAmount),
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = GoldPrimary
                                )
                            )
                            Text(
                                text = "$cutCount × ${CurrencyUtils.formatRupiah(pricePerCut)}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = GrayText
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BlackSurfaceBorder)
                    ) {
                        Text("Batal", color = GrayText)
                    }

                    Button(
                        onClick = {
                            val barber = selectedBarberman
                            if (barber != null && cutCount > 0 && pricePerCut > 0) {
                                onSave(
                                    barber.id,
                                    barber.name,
                                    cutCount,
                                    pricePerCut,
                                    dateMillis,
                                    notes
                                )
                                onDismiss()
                            }
                        },
                        enabled = selectedBarberman != null && cutCount > 0 && pricePerCut > 0,
                        modifier = Modifier
                            .weight(1.5f)
                            .height(48.dp)
                            .testTag("save_transaction_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            disabledContainerColor = BlackSurfaceVariant
                        )
                    ) {
                        Text(
                            "Simpan Transaksi",
                            color = BlackBackground,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
