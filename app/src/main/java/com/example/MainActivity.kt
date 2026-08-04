package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.screens.*
import com.example.ui.theme.SovereignAnalyticsTheme

enum class MainNavDestination(
    val title: String,
    val icon: ImageVector,
    val tag: String
) {
    EMPIRICAL("Empirical", Icons.AutoMirrored.Filled.TrendingUp, "nav_empirical"),
    GRAPH("Graph", Icons.Default.Hub, "nav_graph"),
    EXHIBITS("Exhibits", Icons.Default.Category, "nav_exhibits"),
    BONDS("Bonds", Icons.Default.AccountBalance, "nav_bonds"),
    HOUSING("BSBS & DOT", Icons.Default.SolarPower, "nav_housing"),
    DISPATCH("Dispatch", Icons.Default.Gavel, "nav_dispatch")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SovereignAnalyticsTheme {
                var currentDestination by remember { mutableStateOf(MainNavDestination.EMPIRICAL) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier.testTag("main_bottom_navigation")
                        ) {
                            MainNavDestination.entries.forEach { destination ->
                                NavigationBarItem(
                                    selected = currentDestination == destination,
                                    onClick = { currentDestination = destination },
                                    icon = {
                                        Icon(
                                            imageVector = destination.icon,
                                            contentDescription = destination.title
                                        )
                                    },
                                    label = { Text(destination.title) },
                                    modifier = Modifier.testTag(destination.tag)
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
                        when (currentDestination) {
                            MainNavDestination.EMPIRICAL -> EmpiricalDashboardScreen()
                            MainNavDestination.GRAPH -> KnowledgeGraphScreen()
                            MainNavDestination.EXHIBITS -> ExhibitIndexScreen()
                            MainNavDestination.BONDS -> BondRedemptionScreen()
                            MainNavDestination.HOUSING -> BsbsHousingScreen()
                            MainNavDestination.DISPATCH -> JurisdictionDispatchScreen()
                        }
                    }
                }
            }
        }
    }
}
