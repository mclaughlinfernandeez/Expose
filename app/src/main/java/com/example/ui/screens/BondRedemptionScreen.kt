package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Description
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
import com.example.data.repository.SovereignRepository
import com.example.ui.theme.*
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BondRedemptionScreen() {
    var treasuryRatePct by remember { mutableFloatStateOf(4.25f) } // 4.25% Treasury Rate
    var basisPointSpreadBps by remember { mutableFloatStateOf(50f) } // 50 bps spread
    var showFullCoverLetter by remember { mutableStateOf(false) }

    val mappings = SovereignRepository.assetMappings

    // Make-whole continuous calculation
    val rFree = treasuryRatePct / 100.0
    val delta = basisPointSpreadBps / 10000.0
    val discountRate = rFree + delta

    // $500M Face Value, 6.75% coupon, 5 years maturity remaining
    val faceValue = 500000000.0
    val couponAnnual = faceValue * 0.0675
    val periods = 10 // 5 years * 2 semi-annual

    var pvSum = 0.0
    for (t in 1..periods) {
        val semiCoupon = couponAnnual / 2.0
        pvSum += semiCoupon / (1.0 + discountRate / 2.0).pow(t.toDouble())
    }
    pvSum += faceValue / (1.0 + discountRate / 2.0).pow(periods.toDouble())

    val makeWholePremium = (pvSum - faceValue).coerceAtLeast(0.0)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Hero Header Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "FINANCIAL REDEMPTION & ASSET CLAIMS",
                        style = MaterialTheme.typography.labelMedium,
                        color = SovereignGoldPrimary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "USEC Inc. Restructured Debt Obligations",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "1999 Senior Notes ($500M) redemption presentment & Centrus Energy Corp. (NYSE: LEU) institutional clearing.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Institutional Cover Letter Summary Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth().testTag("cover_letter_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Description, contentDescription = null, tint = SovereignGoldPrimary)
                            Spacer(Modifier.width(8.dp))
                            Text("Institutional Cover Letter", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        TextButton(onClick = { showFullCoverLetter = !showFullCoverLetter }) {
                            Text(if (showFullCoverLetter) "Collapse" else "Expand")
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text("Date: May 30, 2026", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SovereignGoldPrimary)
                    Text("To: Computershare Trust Company, N.A. (Successor Trustee)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text("Clearing Venue: Banco Santander, S.A. (Global Corporate & Investment Banking)", fontSize = 12.sp, color = AnalyticsCyan)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Notice of Presentment, Accounting Demand, and Intent for Redemption/Settlement of USEC Inc. 6.75% Senior Notes due 2009 / Restructured Centrus Energy Corp. Equity Allocations.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    AnimatedVisibility(visible = showFullCoverLetter) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .background(Color(0xFF0F172A), RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Text("Formal Presentment & Mandate:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SovereignGoldPrimary)
                            Text(
                                "The undersigned Beneficiary hereby submits this formal presentment and absolute demand for accounting, valuation, and immediate settlement. We demand that the trustee reconcile the multi-tranche institutional defeasance block and clear the resultant asset values under clean DTC settlement parameters directly to Banco Santander, S.A.",
                                fontSize = 11.sp,
                                color = Color.LightGray
                            )
                        }
                    }
                }
            }
        }

        // Financial & Make-Whole Calculator Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth().testTag("make_whole_calc_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Calculate, contentDescription = null, tint = AnalyticsCyan)
                        Spacer(Modifier.width(8.dp))
                        Text("Make-Whole Premium & Yield Valuation", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Yield Inputs
                    Text("US Treasury Risk-Free Yield (r_f): ${String.format("%.2f", treasuryRatePct)}%", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Slider(
                        value = treasuryRatePct,
                        onValueChange = { treasuryRatePct = it },
                        valueRange = 1.0f..8.0f,
                        colors = SliderDefaults.colors(thumbColor = AnalyticsCyan, activeTrackColor = AnalyticsCyan)
                    )

                    Text("Contract Basis Point Spread (Δ): ${basisPointSpreadBps.toInt()} bps (0.50%)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Slider(
                        value = basisPointSpreadBps,
                        onValueChange = { basisPointSpreadBps = it },
                        valueRange = 10f..200f,
                        colors = SliderDefaults.colors(thumbColor = SovereignGoldPrimary, activeTrackColor = SovereignGoldPrimary)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Calculation Results
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF0D182B), RoundedCornerShape(12.dp))
                            .border(1.dp, AnalyticsCyan.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Face Value:", fontSize = 12.sp, color = Color.Gray)
                            Text("$500,000,000 USD", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Calculated Present Value (PV):", fontSize = 12.sp, color = Color.Gray)
                            Text(String.format("$%,.2f USD", pvSum), fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = AnalyticsCyan)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Make-Whole Premium:", fontSize = 12.sp, color = Color.Gray)
                            Text(String.format("$%,.2f USD", makeWholePremium), fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = SovereignGoldPrimary)
                        }
                    }
                }
            }
        }

        // Asset Mapping Matrix Table
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth().testTag("asset_mapping_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccountBalance, contentDescription = null, tint = SovereignGoldPrimary)
                        Spacer(Modifier.width(8.dp))
                        Text("Reorganization Asset Mapping Matrix", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    mappings.forEach { row ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(Color(0xFF1E293B), RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Text(row.originalInstrument, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SovereignGoldPrimary)
                            Text("Reorg Treatment: ${row.reorganizationCategory}", fontSize = 11.sp, color = Color.LightGray)
                            Text("Successor Asset: ${row.successorAsset}", fontSize = 11.sp, color = AnalyticsCyan)
                            Text("Cleared Venue: ${row.clearedVenue}", fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = StatSuccessGreen)
                        }
                    }
                }
            }
        }
    }
}
