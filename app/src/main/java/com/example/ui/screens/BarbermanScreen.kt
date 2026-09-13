package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.local.BarbermanEntity
import com.example.ui.theme.BlackBackground
import com.example.ui.theme.BlackSurface
import com.example.ui.theme.BlackSurfaceBorder
import com.example.ui.theme.BlackSurfaceVariant
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.GoldVariant
import com.example.ui.theme.GrayText
import com.example.ui.theme.WhitePure

@Composable
fun BarbermanScreen(
    barbermen: List<BarbermanEntity>,
    onAddBarberman: (name: String, phone: String) -> Unit,
    onEditBarberman: (BarbermanEntity) -> Unit,
    onDeleteBarberman: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var barbermanToEdit by remember { mutableStateOf<BarbermanEntity?>(null) }
    var barbermanToDelete by remember { mutableStateOf<BarbermanEntity?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BlackBackground)
            .testTag("barberman_screen"),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Daftar Barberman",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = WhitePure
                        )
                    )
                    Text(
                        text = "Total ${barbermen.size} barberman terdaftar",
                        style = MaterialTheme.typography.bodySmall.copy(color = GrayText)
                    )
                }

                Button(
                    onClick = { showAddDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("add_barberman_button")
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

        // Empty state
        if (barbermen.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
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
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(GoldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Belum Ada Barberman",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = WhitePure
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tambahkan nama barberman yang bertugas untuk mulai mencatat transaksi.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = GrayText),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(barbermen, key = { it.id }) { barber ->
                BarbermanCardItem(
                    barber = barber,
                    onEdit = { barbermanToEdit = barber },
                    onDelete = { barbermanToDelete = barber }
                )
            }
        }
    }

    // Dialogs
    if (showAddDialog) {
        AddEditBarbermanDialog(
            initialBarberman = null,
            onDismiss = { showAddDialog = false },
            onSave = { name, phone, _ ->
                onAddBarberman(name, phone)
                showAddDialog = false
            }
        )
    }

    barbermanToEdit?.let { target ->
        AddEditBarbermanDialog(
            initialBarberman = target,
            onDismiss = { barbermanToEdit = null },
            onSave = { name, phone, isActive ->
                onEditBarberman(target.copy(name = name, phone = phone, isActive = isActive))
                barbermanToEdit = null
            }
        )
    }

    barbermanToDelete?.let { target ->
        AlertDialog(
            onDismissRequest = { barbermanToDelete = null },
            containerColor = BlackSurface,
            title = {
                Text(
                    text = "Hapus Barberman?",
                    color = WhitePure,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Apakah Anda yakin ingin menghapus barberman '${target.name}'? Riwayat transaksi sebelumnya tetap akan tersimpan.",
                    color = GrayText
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteBarberman(target.id)
                        barbermanToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Hapus", color = WhitePure)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { barbermanToDelete = null }
                ) {
                    Text("Batal", color = GrayText)
                }
            }
        )
    }
}

@Composable
fun BarbermanCardItem(
    barber: BarbermanEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
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
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (barber.isActive) GoldContainer else BlackSurfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = barber.name.take(1).uppercase(),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (barber.isActive) GoldPrimary else GrayText
                        )
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = barber.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = WhitePure
                            )
                        )
                        if (!barber.isActive) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = BlackSurfaceVariant
                            ) {
                                Text(
                                    text = "Nonaktif",
                                    color = GrayText,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    if (barber.phone.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null,
                                tint = GrayText,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = barber.phone,
                                style = MaterialTheme.typography.bodySmall.copy(color = GrayText)
                            )
                        }
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = GoldVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Hapus",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
