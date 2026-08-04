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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CypherQueryItem
import com.example.data.model.GraphNode
import com.example.data.model.NodeType
import com.example.data.repository.SovereignRepository
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KnowledgeGraphScreen() {
    var activeTab by remember { mutableIntStateOf(0) } // 0: Topology Graph, 1: Live Ingestion Pipeline, 2: Cypher Query Suite
    var selectedNode by remember { mutableStateOf<GraphNode?>(null) }
    var selectedQueryIndex by remember { mutableIntStateOf(0) }
    var isQueryExecuting by remember { mutableStateOf(false) }
    var queryOutput by remember { mutableStateOf<List<Map<String, String>>?>(null) }

    // Live Ingestion State
    var isIngesting by remember { mutableStateOf(false) }
    var ingestedCount by remember { mutableIntStateOf(0) }
    var currentIngestPath by remember { mutableStateOf("/workspace/root") }
    val scope = rememberCoroutineScope()

    val nodes = SovereignRepository.graphNodes
    val edges = SovereignRepository.graphEdges
    val cypherQueries = SovereignRepository.cypherQueries

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Title Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "KNOWLEDGE GRAPH ARCHITECTURE",
                        style = MaterialTheme.typography.labelMedium,
                        color = AnalyticsCyan,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Graph Ingestion & Cypher Verification",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Recursive document & source code parsing into deterministic Cypher/Neo4j graph nodes with directed relational edge synthesis.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TabRow(
                        selectedTabIndex = activeTab,
                        containerColor = Color.Transparent,
                        contentColor = SovereignGoldPrimary
                    ) {
                        Tab(
                            selected = activeTab == 0,
                            onClick = { activeTab = 0 },
                            text = { Text("Topology", fontSize = 12.sp) }
                        )
                        Tab(
                            selected = activeTab == 1,
                            onClick = { activeTab = 1 },
                            text = { Text("Ingestion Pipeline", fontSize = 12.sp) }
                        )
                        Tab(
                            selected = activeTab == 2,
                            onClick = { activeTab = 2 },
                            text = { Text("Cypher Engine", fontSize = 12.sp) }
                        )
                    }
                }
            }
        }

        // Tab 0: Interactive Visual Topology Canvas
        if (activeTab == 0) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder(),
                    modifier = Modifier.fillMaxWidth().testTag("graph_topology_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Structural Graph Topology Canvas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tap any node to view relational lineage",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Interactive Canvas Rendering Nodes & Edges
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                                .background(Color(0xFF070B14), RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(12.dp))
                                .padding(8.dp)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val w = size.width
                                val h = size.height

                                // Pre-calculated coordinates for visual layout
                                val nodeCoords = mapOf(
                                    "PAT-001" to Offset(w * 0.5f, h * 0.15f),
                                    "CLM-001.12" to Offset(w * 0.2f, h * 0.40f),
                                    "WP-001" to Offset(w * 0.5f, h * 0.40f),
                                    "RST-001" to Offset(w * 0.8f, h * 0.40f),
                                    "FN-210" to Offset(w * 0.12f, h * 0.65f),
                                    "FIG-072" to Offset(w * 0.28f, h * 0.65f),
                                    "EXH-001" to Offset(w * 0.42f, h * 0.65f),
                                    "EXH-004" to Offset(w * 0.58f, h * 0.65f),
                                    "SRC-142" to Offset(w * 0.72f, h * 0.65f),
                                    "TEST-042" to Offset(w * 0.88f, h * 0.65f),
                                    "DATA-003" to Offset(w * 0.28f, h * 0.88f),
                                    "Case D-01" to Offset(w * 0.42f, h * 0.88f),
                                    "Motion Dismiss" to Offset(w * 0.58f, h * 0.88f)
                                )

                                // Draw Edges
                                edges.forEach { edge ->
                                    val start = nodeCoords[edge.sourceId]
                                    val end = nodeCoords[edge.targetId]
                                    if (start != null && end != null) {
                                        val isSelected = selectedNode?.id == edge.sourceId || selectedNode?.id == edge.targetId
                                        val lineColor = if (isSelected) SovereignGoldPrimary else Color(0xFF334155)
                                        val strokeWidth = if (isSelected) 3.dp.toPx() else 1.5.dp.toPx()

                                        drawLine(
                                            color = lineColor,
                                            start = start,
                                            end = end,
                                            strokeWidth = strokeWidth
                                        )
                                    }
                                }

                                // Draw Nodes
                                nodeCoords.forEach { (nodeId, coord) ->
                                    val isSel = selectedNode?.id == nodeId
                                    val radius = if (isSel) 14.dp.toPx() else 10.dp.toPx()
                                    val nodeObj = nodes.find { it.id == nodeId }
                                    val nodeColor = when (nodeObj?.type) {
                                        NodeType.CLAIM -> SovereignGoldPrimary
                                        NodeType.CODE -> AnalyticsCyan
                                        NodeType.EXHIBIT -> StatAlertRed
                                        NodeType.WHITE_PAPER -> Color(0xFFA855F7)
                                        NodeType.DIAGRAM -> Color(0xFFEC4899)
                                        else -> Color(0xFF64748B)
                                    }

                                    drawCircle(color = nodeColor, radius = radius, center = coord)
                                    if (isSel) {
                                        drawCircle(color = Color.White, radius = radius + 3.dp.toPx(), center = coord, style = Stroke(width = 2.dp.toPx()))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Node Chips for Selection
                        Text("Interactive Graph Nodes:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            nodes.take(6).forEach { node ->
                                FilterChip(
                                    selected = selectedNode?.id == node.id,
                                    onClick = { selectedNode = if (selectedNode?.id == node.id) null else node },
                                    label = { Text(node.id, fontSize = 10.sp) }
                                )
                            }
                        }

                        // Selected Node Detail View
                        selectedNode?.let { node ->
                            Spacer(modifier = Modifier.height(12.dp))
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(node.id, fontWeight = FontWeight.ExtraBold, color = SovereignGoldPrimary)
                                        Text(node.type.name, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Text(node.label, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(node.parentContext, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                                    val connectedEdges = edges.filter { it.sourceId == node.id || it.targetId == node.id }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("Directed Connections (${connectedEdges.size}):", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    connectedEdges.forEach { edge ->
                                        Text(
                                            "• ${edge.sourceId} -[${edge.relationType}]-> ${edge.targetId}",
                                            fontSize = 11.sp,
                                            fontFamily = FontFamily.Monospace,
                                            color = AnalyticsCyan
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Tab 1: Algorithm 1 Ingestion Pipeline Simulator
        if (activeTab == 1) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder(),
                    modifier = Modifier.fillMaxWidth().testTag("ingestion_pipeline_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Algorithm 1: Recursive Workspace Ingestion",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Walks directories, docstores, Rust crates, VCF files, and PDFs to extract nodes and relation edges.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                isIngesting = true
                                scope.launch {
                                    val paths = listOf(
                                        "/workspace/src/lib.rs",
                                        "/workspace/docs/EXH-001_PoliceReport.pdf",
                                        "/workspace/docs/EXH-015_GenomicLoci.vcf",
                                        "/workspace/docs/EXH-024_HSPA_Spec.docx",
                                        "/workspace/contracts/TrustCharter.md"
                                    )
                                    ingestedCount = 0
                                    paths.forEach { path ->
                                        currentIngestPath = path
                                        delay(500)
                                        ingestedCount += 7
                                    }
                                    isIngesting = false
                                }
                            },
                            enabled = !isIngesting,
                            colors = ButtonDefaults.buttonColors(containerColor = SovereignGoldPrimary, contentColor = Color.Black),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text(if (isIngesting) "Ingesting Directories..." else "Execute Recursive Ingestion Pipeline")
                        }

                        if (isIngesting || ingestedCount > 0) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF0F172A), RoundedCornerShape(10.dp))
                                    .padding(12.dp)
                            ) {
                                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    Text("Pipeline Status:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AnalyticsCyan)
                                    Text(if (isIngesting) "SCANNING..." else "COMPLETE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StatSuccessGreen)
                                }
                                Text("Path: $currentIngestPath", fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = Color.LightGray)
                                LinearProgressIndicator(
                                    progress = { if (isIngesting) 0.6f else 1.0f },
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                                    color = SovereignGoldPrimary,
                                )
                                Text("Extracted Graph Entities: $ingestedCount Nodes / ${ingestedCount * 2} Edges", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Tab 2: Cypher Query Engine
        if (activeTab == 2) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder(),
                    modifier = Modifier.fillMaxWidth().testTag("cypher_engine_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Technical Cypher Query Suite",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Select and execute graph verification queries against Neo4j / Amazon Neptune instance.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Query Selector Chips
                        cypherQueries.forEachIndexed { idx, q ->
                            FilterChip(
                                selected = selectedQueryIndex == idx,
                                onClick = {
                                    selectedQueryIndex = idx
                                    queryOutput = null
                                },
                                label = { Text(q.title, fontSize = 11.sp) },
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }

                        val activeQuery = cypherQueries[selectedQueryIndex]

                        Spacer(modifier = Modifier.height(12.dp))

                        // Cypher Code Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF0F172A), RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = activeQuery.query,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = SovereignGoldPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                isQueryExecuting = true
                                scope.launch {
                                    delay(600)
                                    queryOutput = when (selectedQueryIndex) {
                                        0 -> listOf(
                                            mapOf("Claim" to "CLM-001.12", "Function" to "fn_verify_loci", "SourceCode" to "src/lib.rs", "Diagram" to "FIG-072", "Paper" to "WP-005", "Evidence" to "EXH-015"),
                                            mapOf("Claim" to "CLM-001.12", "Function" to "fn_pqc_sign", "SourceCode" to "src/pqc.rs", "Diagram" to "FIG-073", "Paper" to "WP-005", "Evidence" to "EXH-026")
                                        )
                                        1 -> listOf(
                                            mapOf("ExhibitID" to "EXH-001", "Description" to "Police Report J/86", "Jurisdiction" to "Case D-01-CR-25-011312"),
                                            mapOf("ExhibitID" to "EXH-004", "Description" to "Medical X-Ray Records", "Jurisdiction" to "Case D-01-CR-25-011312"),
                                            mapOf("ExhibitID" to "EXH-032", "Description" to "Plaintiff Federal Complaint", "Jurisdiction" to "1:25-cv-04301")
                                        )
                                        else -> listOf(
                                            mapOf("ReleaseVersion" to "VER-2.5.0", "ArtifactType" to "PatentClaim", "ArtifactID" to "CLM-001.12", "ArtifactName" to "Genomic Claim"),
                                            mapOf("ReleaseVersion" to "VER-2.5.0", "ArtifactType" to "PQCModule", "ArtifactID" to "RST-001", "ArtifactName" to "Kyber-768 KEM")
                                        )
                                    }
                                    isQueryExecuting = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AnalyticsCyan, contentColor = Color.Black),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text(if (isQueryExecuting) "Executing Query..." else "Run Cypher Query")
                        }

                        // Query Result Table
                        queryOutput?.let { results ->
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Query Execution Output (${results.size} rows returned):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StatSuccessGreen)
                            Spacer(modifier = Modifier.height(6.dp))
                            results.forEach { row ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .background(Color(0xFF1E293B), RoundedCornerShape(8.dp))
                                        .padding(10.dp)
                                ) {
                                    row.forEach { (k, v) ->
                                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                            Text("$k:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                                            Text(v, fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
