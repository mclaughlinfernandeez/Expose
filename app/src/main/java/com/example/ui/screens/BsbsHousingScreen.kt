package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.SolarPower
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MicrogridHousingSpec
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BsbsHousingScreen() {
    val context = LocalContext.current
    var houseCount by remember { mutableFloatStateOf(200f) } // Scaled down to 200 per user instructions
    var grantRequestedMillions by remember { mutableFloatStateOf(45f) } // $45M requested from DOT
    var showDraftSuit by remember { mutableStateOf(false) }

    val solarPerHome = 12.5
    val batteryPerHome = 25.0
    val totalSolarMw = (houseCount * solarPerHome) / 1000.0
    val totalStorageMwh = (houseCount * batteryPerHome) / 1000.0

    val draftComplaintText = remember(houseCount, grantRequestedMillions) {
        """
UNITED STATES DISTRICT COURT FOR THE DISTRICT OF MARYLAND
BALTIMORE DIVISION

CAUSTIN LEE MCLAUGHLIN & PSDLEF SOVEREIGN EXCHANGE TRUST,
    Plaintiffs,
v.                                      Civil Action No. 1:26-cv-08812
UNITED STATES DEPARTMENT OF TRANSPORTATION,
and SECRETARY OF TRANSPORTATION,
    Defendants.

VERIFIED COMPLAINT FOR MANDAMUS AND DECLARATORY RELIEF
PURSUANT TO IIJA § 11101 AND IRA § 60101 (CLEAN TRANSIT ELECTRIFICATION)

1. INTRODUCTION & JURISDICTION
Plaintiffs bring this action to enforce mandatory statutory allocation of infrastructure electrification funds under the Infrastructure Investment and Jobs Act (IIJA) and Inflation Reduction Act (IRA). Plaintiffs have established the Baltimore Solar & Battery Service (BSBS) and FSBS microgrid network to serve ${houseCount.toInt()} housing units in Baltimore City.

2. FACTUAL ALLEGATIONS & MICROGRID CAPABILITY
The PSDLEF 200-house clean energy microgrid combines ${String.format("%.2f", totalSolarMw)} MW of distributed solar photovoltaic generation and ${String.format("%.2f", totalStorageMwh)} MWh of battery energy storage, creating a resilient local grid for zero-emission transit and residential power.

3. CAUSE OF ACTION & PRAYER FOR RELIEF
Plaintiffs request an order compelling the U.S. Department of Transportation to disburse $${grantRequestedMillions.toInt()},000,000 USD from the Clean Transportation Infrastructure Grant Pool for immediate deployment of BSBS & FSBS municipal solar charging hubs.
        """.trimIndent()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Hero Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "MUNICIPAL CLEAN ENERGY & TRANSIT",
                        style = MaterialTheme.typography.labelMedium,
                        color = StatSuccessGreen,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "BSBS / PSDLEF 200 Housing Microgrid",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Baltimore Solar & Battery Service (BSBS) & FSBS clean energy infrastructure scaled to 200 sovereign trust housing units.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // 200 Houses Microgrid Spec Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth().testTag("psdlef_microgrid_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SolarPower, contentDescription = null, tint = SovereignGoldPrimary)
                        Spacer(Modifier.width(8.dp))
                        Text("BSBS & FSBS Microgrid Architecture", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("PSDLEF Housing Units: ${houseCount.toInt()} Houses", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Slider(
                        value = houseCount,
                        onValueChange = { houseCount = it },
                        valueRange = 50f..500f,
                        steps = 44,
                        colors = SliderDefaults.colors(thumbColor = SovereignGoldPrimary, activeTrackColor = SovereignGoldPrimary)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF0F1B18), RoundedCornerShape(12.dp))
                            .border(1.dp, StatSuccessGreen.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Total Solar PV", fontSize = 11.sp, color = Color.Gray)
                            Text(String.format("%.2f MW", totalSolarMw), fontWeight = FontWeight.ExtraBold, color = StatSuccessGreen, fontSize = 16.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Battery BESS", fontSize = 11.sp, color = Color.Gray)
                            Text(String.format("%.2f MWh", totalStorageMwh), fontWeight = FontWeight.ExtraBold, color = AnalyticsCyan, fontSize = 16.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Per Home Solar", fontSize = 11.sp, color = Color.Gray)
                            Text("${solarPerHome} kW", fontWeight = FontWeight.Bold, color = SovereignGoldPrimary, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        // Legal Suit to U.S. Dept of Transportation (DOT) Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth().testTag("dot_suit_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Gavel, contentDescription = null, tint = StatAlertRed)
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text("U.S. Dept. of Transportation Suit", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("IIJA & IRA Transit Infrastructure Mandate", fontSize = 11.sp, color = StatAlertRed)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Requested DOT Clean Electrification Grant: $${grantRequestedMillions.toInt()} Million", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Slider(
                        value = grantRequestedMillions,
                        onValueChange = { grantRequestedMillions = it },
                        valueRange = 10f..100f,
                        steps = 89,
                        colors = SliderDefaults.colors(thumbColor = StatAlertRed, activeTrackColor = StatAlertRed)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { showDraftSuit = !showDraftSuit },
                        colors = ButtonDefaults.buttonColors(containerColor = SovereignGoldPrimary, contentColor = Color.Black),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Gavel, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(if (showDraftSuit) "Hide Complaint Draft" else "Generate DOT Federal Complaint Draft")
                    }

                    AnimatedVisibility(visible = showDraftSuit) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Verified Federal Mandamus Complaint:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = AnalyticsCyan)
                                IconButton(onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("DOT Complaint", draftComplaintText)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Complaint copied to clipboard!", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = SovereignGoldPrimary)
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF0F172A), RoundedCornerShape(10.dp))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = draftComplaintText,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = Color.LightGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
