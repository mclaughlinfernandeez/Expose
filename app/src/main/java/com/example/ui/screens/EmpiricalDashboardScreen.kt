package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.SovereignRepository
import com.example.ui.theme.*
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpiricalDashboardScreen() {
    var selectedPanelTab by remember { mutableIntStateOf(0) } // 0: All, 1: Panel A, 2: Panel B, 3: Panel C, 4: Panel D

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Hero Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "EMPIRICAL RESEARCH SUITE",
                            style = MaterialTheme.typography.labelMedium,
                            color = SovereignGoldPrimary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Surface(
                            color = StatAlertRed.copy(alpha = 0.2f),
                            shape = CircleShape
                        ) {
                            Text(
                                text = "CRITICAL INSIGHTS",
                                color = StatAlertRed,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Disability Claims & Policy Analytics",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Causal economic and mortality impact analysis across SSA adjudication bottlenecks, post-removal criminality, and benefit income regression kinks.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Filter Chips
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val tabs = listOf("Overview", "Panel A", "Panel B", "Panel C", "Panel D")
                        tabs.forEachIndexed { index, label ->
                            FilterChip(
                                selected = selectedPanelTab == index,
                                onClick = { selectedPanelTab = index },
                                label = { Text(label, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SovereignGoldPrimary,
                                    selectedLabelColor = Color.Black
                                )
                            )
                        }
                    }
                }
            }
        }

        // Panel A: Deaths While Pending vs Adjudication Delay (2017-2023)
        if (selectedPanelTab == 0 || selectedPanelTab == 1) {
            item { PanelACard() }
        }

        // Panel B: FY2025 Disability Decision Waterfall
        if (selectedPanelTab == 0 || selectedPanelTab == 2) {
            item { PanelBCard() }
        }

        // Panel C: SSI Removal -> Criminal Justice Outcomes
        if (selectedPanelTab == 0 || selectedPanelTab == 3) {
            item { PanelCCard() }
        }

        // Panel D: DI Income Effect on Mortality
        if (selectedPanelTab == 0 || selectedPanelTab == 4) {
            item { PanelDCard() }
        }
    }
}

@Composable
private fun PanelACard() {
    val dataPoints = SovereignRepository.delayVsDeathData
    var selectedYearIndex by remember { mutableFloatStateOf(6f) }
    val currentPoint = dataPoints[selectedYearIndex.roundToInt().coerceIn(0, dataPoints.size - 1)]

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("panel_a_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = "Panel A",
                    tint = StatAlertRed,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "PANEL A: DEATHS WHILE PENDING VS. DELAY",
                        style = MaterialTheme.typography.labelLarge,
                        color = StatAlertRed,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "3× Increase in Applicant Deaths (2017–2023)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Canvas Line Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFF0D1322), shape = RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFF1E293B), shape = RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    val minDelay = 100f
                    val maxDelay = 230f
                    val minDeaths = 8000f
                    val maxDeaths = 32000f

                    val pathDelay = Path()
                    val pathDeaths = Path()

                    dataPoints.forEachIndexed { idx, pt ->
                        val x = (idx.toFloat() / (dataPoints.size - 1)) * width
                        val yDelay = height - ((pt.waitTimeDays - minDelay) / (maxDelay - minDelay)) * height
                        val yDeaths = height - ((pt.deathsPending - minDeaths) / (maxDeaths - minDeaths)) * height

                        if (idx == 0) {
                            pathDelay.moveTo(x, yDelay)
                            pathDeaths.moveTo(x, yDeaths)
                        } else {
                            pathDelay.lineTo(x, yDelay)
                            pathDeaths.lineTo(x, yDeaths)
                        }

                        // Draw Point Indicators
                        drawCircle(color = AnalyticsCyan, radius = 4.dp.toPx(), center = Offset(x, yDelay))
                        drawCircle(color = StatAlertRed, radius = 4.dp.toPx(), center = Offset(x, yDeaths))
                    }

                    // Stroke Lines
                    drawPath(path = pathDelay, color = AnalyticsCyan, style = Stroke(width = 3.dp.toPx()))
                    drawPath(path = pathDeaths, color = StatAlertRed, style = Stroke(width = 3.dp.toPx()))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Chart Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).background(AnalyticsCyan, CircleShape))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Adjudication Delay (Days)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).background(StatAlertRed, CircleShape))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Deaths Pending (Annual)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Year Selector Slider
            Text(
                text = "Interactive Year Explorer: ${currentPoint.year}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Slider(
                value = selectedYearIndex,
                onValueChange = { selectedYearIndex = it },
                valueRange = 0f..(dataPoints.size - 1).toFloat(),
                steps = dataPoints.size - 2,
                colors = SliderDefaults.colors(thumbColor = SovereignGoldPrimary, activeTrackColor = SovereignGoldPrimary)
            )

            // Dynamic Stats Readout Card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Average Wait Time", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${currentPoint.waitTimeDays} Days", fontWeight = FontWeight.Bold, color = AnalyticsCyan, fontSize = 18.sp)
                }
                Column {
                    Text("Deaths While Pending", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${currentPoint.deathsPending}", fontWeight = FontWeight.Bold, color = StatAlertRed, fontSize = 18.sp)
                }
                Column {
                    Text("Mortality Multiplier", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    val mult = String.format("%.2fx", currentPoint.deathsPending / 10000.0)
                    Text(mult, fontWeight = FontWeight.Bold, color = SovereignGoldPrimary, fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
private fun PanelBCard() {
    val stages = SovereignRepository.waterfallStages
    var applicantCohort by remember { mutableFloatStateOf(10000f) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("panel_b_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Panel B",
                    tint = AnalyticsCyan,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "PANEL B: FY2025 DECISION WATERFALL",
                        style = MaterialTheme.typography.labelLarge,
                        color = AnalyticsCyan,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Adjudication Bottleneck & Denial Cascades",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stage Visualizer
            stages.forEachIndexed { index, stage ->
                val remainingRatio = when (index) {
                    0 -> 1.0
                    1 -> 0.64
                    else -> 0.64 * 0.84
                }
                val stageApplicants = (applicantCohort * remainingRatio).roundToInt()
                val approvedApplicants = (stageApplicants * stage.approvalRate).roundToInt()
                val deniedApplicants = stageApplicants - approvedApplicants

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .background(Color(0xFF0F172A), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${index + 1}. ${stage.stageName}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Surface(
                            color = SovereignGoldPrimary.copy(alpha = 0.2f),
                            shape = CircleShape
                        ) {
                            Text(
                                text = "Avg ${stage.avgWaitDays} Days",
                                color = SovereignGoldPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Waterfall Bar Graph
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .clip(RoundedCornerShape(6.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(stage.approvalRate.toFloat().coerceAtLeast(0.05f))
                                .fillMaxHeight()
                                .background(StatSuccessGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "${(stage.approvalRate * 100).toInt()}% Pass",
                                fontSize = 10.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(stage.denialRate.toFloat().coerceAtLeast(0.05f))
                                .fillMaxHeight()
                                .background(StatAlertRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "${(stage.denialRate * 100).toInt()}% Denied",
                                fontSize = 10.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Cohort Flow: $stageApplicants total entering stage ➔ $approvedApplicants approved, $deniedApplicants forced into next bottleneck",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Applicant Simulator Slider
            Text(
                text = "Simulate Applicant Cohort: ${applicantCohort.toInt()} People",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Slider(
                value = applicantCohort,
                onValueChange = { applicantCohort = it },
                valueRange = 1000f..50000f,
                steps = 49,
                colors = SliderDefaults.colors(thumbColor = AnalyticsCyan, activeTrackColor = AnalyticsCyan)
            )
        }
    }
}

@Composable
private fun PanelCCard() {
    val impacts = SovereignRepository.criminalJusticeImpacts
    var cohortSize by remember { mutableFloatStateOf(10000f) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("panel_c_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Gavel,
                    contentDescription = "Panel C",
                    tint = SovereignGoldPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "PANEL C: SSI REMOVAL ➔ CRIMINAL OUTCOMES",
                        style = MaterialTheme.typography.labelLarge,
                        color = SovereignGoldPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Deshpande & Mueller-Smith (2021) Causal Model",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Impact Grid Cards
            impacts.forEach { impact ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color(0xFF1E1B2E), RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = impact.outcomeName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = impact.description,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        color = StatAlertRed.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "+${impact.percentIncrease}%",
                            color = StatAlertRed,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Causal Estimator Box
            Text(
                text = "Causal Impact Estimator (Age-18 SSI Removal Cohort): ${cohortSize.toInt()} Youth",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Slider(
                value = cohortSize,
                onValueChange = { cohortSize = it },
                valueRange = 1000f..50000f,
                steps = 49,
                colors = SliderDefaults.colors(thumbColor = SovereignGoldPrimary, activeTrackColor = SovereignGoldPrimary)
            )

            val addCharges = (cohortSize * 0.20).toInt()
            val addOffenses = (cohortSize * 0.60).toInt()
            val addIncarcerated = (cohortSize * 0.60).toInt()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("+Criminal Charges", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("+$addCharges", fontWeight = FontWeight.Bold, color = StatAlertRed, fontSize = 16.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("+Theft/Property Offenses", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("+$addOffenses", fontWeight = FontWeight.Bold, color = SovereignGoldPrimary, fontSize = 16.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("+Incarcerations", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("+$addIncarcerated", fontWeight = FontWeight.Bold, color = StatAlertRed, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun PanelDCard() {
    val kink = SovereignRepository.incomeMortalityKink
    var addedIncome by remember { mutableFloatStateOf(1000f) }

    val estReductionMinPct = (addedIncome / 1000.0) * kink.mortalityReductionMinPct
    val estReductionMaxPct = (addedIncome / 1000.0) * kink.mortalityReductionMaxPct
    val savedPer100kMin = (estReductionMinPct * 1000).toInt()
    val savedPer100kMax = (estReductionMaxPct * 1000).toInt()

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("panel_d_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Panel D",
                    tint = StatSuccessGreen,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "PANEL D: DI INCOME EFFECT ON MORTALITY",
                        style = MaterialTheme.typography.labelLarge,
                        color = StatSuccessGreen,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Gelber et al. (2023) Regression Kink Model",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = kink.keyInsight,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Regression Kink Interactive Calculator
            Text(
                text = "Simulated Income Increase: $${addedIncome.toInt()} / year",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Slider(
                value = addedIncome,
                onValueChange = { addedIncome = it },
                valueRange = 500f..5000f,
                steps = 44,
                colors = SliderDefaults.colors(thumbColor = StatSuccessGreen, activeTrackColor = StatSuccessGreen)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0F1B18), RoundedCornerShape(12.dp))
                    .border(1.dp, StatSuccessGreen.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Mortality Reduction Rate:", color = Color.White, fontWeight = FontWeight.Bold)
                    Text(
                        String.format("%.2f%% – %.2f%%", estReductionMinPct, estReductionMaxPct),
                        color = StatSuccessGreen,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Estimated Lives Saved per 100k:", color = Color.White, fontSize = 13.sp)
                    Text(
                        "$savedPer100kMin – $savedPer100kMax Lives",
                        color = SovereignGoldPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
