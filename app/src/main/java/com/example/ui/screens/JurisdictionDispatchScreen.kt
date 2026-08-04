package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.JurisdictionRepository
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun JurisdictionDispatchScreen() {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var zipInput by remember { mutableStateOf("21201") }
    var currentInfo by remember { mutableStateOf(JurisdictionRepository.getJurisdictionByZip("21201")) }
    var selectedTransmittalTab by remember { mutableIntStateOf(0) } // 0 = Congressional, 1 = MDEC Court

    // Update jurisdiction whenever zip changes to 5 valid digits
    LaunchedEffect(zipInput) {
        if (zipInput.trim().length >= 5) {
            currentInfo = JurisdictionRepository.getJurisdictionByZip(zipInput)
        }
    }

    val congressionalLetter = remember(currentInfo) {
        JurisdictionRepository.buildCongressionalTransmittalText(currentInfo)
    }

    val courtMdecNotice = remember(currentInfo) {
        JurisdictionRepository.buildMdecCourtNoticeText(currentInfo)
    }

    val activeText = if (selectedTransmittalTab == 0) congressionalLetter else courtMdecNotice

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkCanvasBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Screen Title Banner ---
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, SovereignGoldPrimary.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
            color = DarkSurfaceCard
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(SovereignGoldPrimary.copy(alpha = 0.15f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Gavel,
                            contentDescription = "Jurisdiction Finder",
                            tint = SovereignGoldPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Jurisdiction & District Dispatch",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = SovereignGoldLight
                            )
                        )
                        Text(
                            text = "Congressional & Federal District Court (MDEC/PACER) Finder",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextMutedSlate)
                        )
                    }
                }
                Text(
                    text = "Enter your local ZIP code to instantly locate your U.S. Federal District Court, PACER/MDEC case distribution intake email, and Member of Congress. Send authorized fund release proposals and legal case notices directly.",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
        }

        // --- ZIP Code Search Input ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
            shape = RoundedCornerShape(14.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkBorderOutline))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "ENTER LOCAL ZIP CODE",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = SovereignGoldPrimary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )

                OutlinedTextField(
                    value = zipInput,
                    onValueChange = { input ->
                        if (input.length <= 5 && input.all { it.isDigit() }) {
                            zipInput = input
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("zip_code_input"),
                    label = { Text("5-Digit US ZIP Code") },
                    placeholder = { Text("e.g. 21201, 20001, 10001") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = SovereignGoldPrimary
                        )
                    },
                    trailingIcon = {
                        if (zipInput.isNotEmpty()) {
                            IconButton(onClick = { zipInput = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = TextMutedSlate
                                )
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SovereignGoldPrimary,
                        unfocusedBorderColor = DarkBorderOutline,
                        focusedLabelColor = SovereignGoldLight
                    )
                )

                Text(
                    text = "Quick Select Sample ZIPs:",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextMutedSlate)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.testTag("quick_zip_chips")
                ) {
                    items(JurisdictionRepository.featuredZipCodes) { (zip, label) ->
                        FilterChip(
                            selected = zipInput == zip,
                            onClick = { zipInput = zip },
                            label = { Text(label, style = MaterialTheme.typography.labelMedium) },
                            leadingIcon = if (zipInput == zip) {
                                {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SovereignGoldPrimary.copy(alpha = 0.2f),
                                selectedLabelColor = SovereignGoldLight,
                                containerColor = DarkSurfaceCardVariant,
                                labelColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }
        }

        // --- Current Location & Jurisdiction Header ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
            shape = RoundedCornerShape(14.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${currentInfo.city}, ${currentInfo.state} (${currentInfo.zipCode})",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "Jurisdiction: ${currentInfo.county} • ${currentInfo.court.circuit}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = AnalyticsCyan)
                    )
                }
                Surface(
                    color = SovereignGoldPrimary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = currentInfo.court.pacerDistrictCode.uppercase(),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = SovereignGoldLight,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // --- Card 1: Federal District Court & MDEC / PACER Intake Details ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
            shape = RoundedCornerShape(14.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkBorderOutline))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = "Federal Court",
                        tint = SovereignGoldPrimary
                    )
                    Text(
                        text = "U.S. Federal District Court & MDEC Intake",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SovereignGoldLight
                        )
                    )
                }

                HorizontalDivider(color = DarkBorderOutline)

                // Court Name & Circuit
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = currentInfo.court.courtName,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "Circuit: ${currentInfo.court.circuit} • PACER Code: ${currentInfo.court.pacerDistrictCode}",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMutedSlate)
                    )
                }

                // ECF & MDEC Case Intake Distribution Email Section
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = DarkSurfaceCardVariant,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, SovereignGoldPrimary.copy(alpha = 0.25f))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email",
                                tint = AnalyticsCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "MDEC / ECF Case Distribution List Email:",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = AnalyticsCyan,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Text(
                            text = currentInfo.court.ecfMdecEmail,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.Monospace,
                                color = SovereignGoldLight,
                                fontWeight = FontWeight.SemiBold
                            )
                        )

                        Text(
                            text = "Intake Channel: ${currentInfo.court.intakeDistributionEmail}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                color = TextMutedSlate
                            )
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:${currentInfo.court.ecfMdecEmail}")
                                        putExtra(Intent.EXTRA_SUBJECT, "NOTICE OF FILING: MDEC Case Distribution Notice (${currentInfo.zipCode})")
                                        putExtra(Intent.EXTRA_TEXT, courtMdecNotice)
                                    }
                                    try {
                                        context.startActivity(Intent.createChooser(intent, "Email Court MDEC Intake"))
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "No email client found", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SovereignGoldPrimary),
                                modifier = Modifier.weight(1f).testTag("email_mdec_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Send Email",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Email MDEC Intake", style = MaterialTheme.typography.labelMedium)
                            }

                            OutlinedButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(currentInfo.court.ecfMdecEmail))
                                    Toast.makeText(context, "Copied MDEC Email!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.testTag("copy_mdec_email_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy Email",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                // Courthouse Address & Phone
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "COURTHOUSE ADDRESS",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextMutedSlate)
                        )
                        Text(
                            text = currentInfo.court.courthouseAddress,
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface)
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "CLERK PHONE",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextMutedSlate)
                        )
                        Text(
                            text = currentInfo.court.clerkPhone,
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface)
                        )
                    }
                }

                // Direct Links to USCourts.gov & PACER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AssistChip(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentInfo.court.usCourtsUrl))
                            context.startActivity(intent)
                        },
                        label = { Text("USCourts.gov Directory") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.OpenInNew, contentDescription = "Open", modifier = Modifier.size(16.dp))
                        },
                        modifier = Modifier.weight(1f)
                    )
                    AssistChip(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentInfo.court.pacerUrl))
                            context.startActivity(intent)
                        },
                        label = { Text("PACER Portal") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Language, contentDescription = "PACER", modifier = Modifier.size(16.dp))
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // --- Card 2: Congressional Representatives & U.S. Senators ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
            shape = RoundedCornerShape(14.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkBorderOutline))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = "Congress",
                        tint = SovereignGoldPrimary
                    )
                    Text(
                        text = "Congressional Delegation (${currentInfo.representation.houseDistrict})",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SovereignGoldLight
                        )
                    )
                }

                HorizontalDivider(color = DarkBorderOutline)

                // House Representative Details
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = DarkSurfaceCardVariant,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = currentInfo.representation.representativeName,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(
                                    text = "U.S. House of Representatives • ${currentInfo.representation.party}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextMutedSlate)
                                )
                            }
                            Surface(
                                color = SovereignGoldPrimary.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = currentInfo.representation.houseDistrict,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = SovereignGoldLight,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Text(
                            text = "Office: ${currentInfo.representation.capitolOffice}",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        Text(
                            text = "Phone: ${currentInfo.representation.phone}",
                            style = MaterialTheme.typography.bodySmall.copy(color = AnalyticsCyan)
                        )

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentInfo.representation.constituentPortalUrl))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SovereignGoldPrimary),
                            modifier = Modifier.fillMaxWidth().testTag("constituent_portal_button")
                        ) {
                            Icon(imageVector = Icons.Default.OpenInNew, contentDescription = "Portal", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Open Official Constituent Contact Portal", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }

                // U.S. Senators Section
                Text(
                    text = "U.S. SENATORS FOR ${currentInfo.state}",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextMutedSlate, fontWeight = FontWeight.Bold)
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    currentInfo.representation.senators.forEach { senator ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DarkSurfaceCardVariant, RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = senator.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(
                                    text = "${senator.party} • ${senator.phone}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextMutedSlate)
                                )
                            }
                            IconButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${senator.phone.replace(Regex("[^0-9]"), "")}"))
                                    context.startActivity(intent)
                                }
                            ) {
                                Icon(imageVector = Icons.Default.Phone, contentDescription = "Call", tint = SovereignGoldPrimary)
                            }
                        }
                    }
                }
            }
        }

        // --- Card 3: Transmittal Letter & Legal Case Dispatch Composer ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
            shape = RoundedCornerShape(14.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkBorderOutline))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = "Transmittal Composer",
                        tint = SovereignGoldPrimary
                    )
                    Text(
                        text = "Transmittal & Case Dispatch Composer",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SovereignGoldLight
                        )
                    )
                }

                // Tab Switcher between Congressional Letter & MDEC Court Notice
                TabRow(
                    selectedTabIndex = selectedTransmittalTab,
                    containerColor = DarkSurfaceCardVariant,
                    contentColor = SovereignGoldPrimary,
                    modifier = Modifier.clip(RoundedCornerShape(8.dp))
                ) {
                    Tab(
                        selected = selectedTransmittalTab == 0,
                        onClick = { selectedTransmittalTab = 0 },
                        modifier = Modifier.testTag("tab_congressional_proposal"),
                        text = {
                            Text(
                                "Congressional Proposal",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    )
                    Tab(
                        selected = selectedTransmittalTab == 1,
                        onClick = { selectedTransmittalTab = 1 },
                        modifier = Modifier.testTag("tab_mdec_notice"),
                        text = {
                            Text(
                                "MDEC Court Notice",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    )
                }

                // Pre-formatted text box
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 180.dp, max = 280.dp),
                    color = DarkCanvasBg,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, DarkBorderOutline)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = activeText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }

                // Dispatch Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            val recipient = if (selectedTransmittalTab == 0) {
                                currentInfo.representation.constituentPortalUrl
                            } else {
                                currentInfo.court.ecfMdecEmail
                            }
                            val subject = if (selectedTransmittalTab == 0) {
                                "CONSTITUENT PETITION: Sovereign Fund Dispersal Request (${currentInfo.zipCode})"
                            } else {
                                "NOTICE OF FILING: MDEC Case Intake Notice (${currentInfo.zipCode})"
                            }
                            
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:${if (selectedTransmittalTab == 1) currentInfo.court.ecfMdecEmail else ""}")
                                putExtra(Intent.EXTRA_SUBJECT, subject)
                                putExtra(Intent.EXTRA_TEXT, activeText)
                            }

                            try {
                                context.startActivity(Intent.createChooser(intent, "Dispatch via Email Client"))
                            } catch (e: Exception) {
                                Toast.makeText(context, "No email app found on device", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SovereignGoldPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dispatch_email_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send Email",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (selectedTransmittalTab == 0) "Email Rep" else "Email MDEC List",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(activeText))
                            Toast.makeText(context, "Transmittal letter copied to clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("copy_transmittal_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Text",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Copy Letter", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}
