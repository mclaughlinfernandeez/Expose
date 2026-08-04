package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExhibitItem
import com.example.data.repository.SovereignRepository
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExhibitIndexScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedExhibitDetail by remember { mutableStateOf<ExhibitItem?>(null) }

    val allExhibits = SovereignRepository.exhibitList

    val categoryOptions = listOf(
        "All",
        "Master Legal Portfolio",
        "Cognitive Compliance",
        "Dissertation Brief",
        "Procedural & Accommodation",
        "Sovereign Trust"
    )

    val filteredExhibits = remember(searchQuery, selectedCategory) {
        allExhibits.filter { exh ->
            val matchesCategory = if (selectedCategory == "All") true else exh.parentContext.contains(selectedCategory, ignoreCase = true)
            val matchesSearch = searchQuery.isBlank() ||
                    exh.id.contains(searchQuery, ignoreCase = true) ||
                    exh.legacyRef.contains(searchQuery, ignoreCase = true) ||
                    exh.description.contains(searchQuery, ignoreCase = true) ||
                    exh.parentContext.contains(searchQuery, ignoreCase = true)

            matchesCategory && matchesSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Title Header Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "MULTI-DOMAIN EXHIBIT MASTER INDEX",
                    style = MaterialTheme.typography.labelMedium,
                    color = SovereignGoldPrimary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Exhibit Scanned Index (EXH-001..EXH-034)",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Deterministic mapping of physical, digital, scientific, and statutory evidence into first-class nodes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search exhibits (e.g., EXH-015, COMT, Police, EEG)...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("exhibit_search_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Filter Chips Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            categoryOptions.take(3).forEach { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text(if (cat == "All") "All (34)" else cat.take(16) + "...", fontSize = 11.sp) }
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            categoryOptions.drop(3).forEach { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat.take(18) + "...", fontSize = 11.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Showing ${filteredExhibits.size} Exhibits:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Exhibit Cards List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 32.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(filteredExhibits, key = { it.id }) { item ->
                ExhibitItemCard(
                    exhibit = item,
                    onSelect = { selectedExhibitDetail = item }
                )
            }
        }
    }

    // Exhibit Detail Modal Dialog
    selectedExhibitDetail?.let { exhibit ->
        AlertDialog(
            onDismissRequest = { selectedExhibitDetail = null },
            confirmButton = {
                TextButton(onClick = { selectedExhibitDetail = null }) {
                    Text("Close", fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Description, contentDescription = null, tint = SovereignGoldPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${exhibit.id} (${exhibit.legacyRef})", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Description & Abstract:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(exhibit.description, fontSize = 13.sp)

                    Text("Parent Context / Dossier:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(exhibit.parentContext, fontSize = 12.sp, color = AnalyticsCyan)

                    Text("Graph Relationships & Schema Edges:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    exhibit.relationships.forEach { rel ->
                        Text("• $rel", fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = SovereignGoldPrimary)
                    }
                }
            }
        )
    }
}

@Composable
private fun ExhibitItemCard(
    exhibit: ExhibitItem,
    onSelect: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("exhibit_card_${exhibit.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = SovereignGoldPrimary.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = exhibit.id,
                        color = SovereignGoldPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = "Ref: ${exhibit.legacyRef}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = exhibit.description,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Context: ${exhibit.parentContext}",
                fontSize = 11.sp,
                color = AnalyticsCyan
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Graph Relationship Chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                exhibit.relationships.forEach { rel ->
                    Surface(
                        color = Color(0xFF1E293B),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = rel,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color.LightGray,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
