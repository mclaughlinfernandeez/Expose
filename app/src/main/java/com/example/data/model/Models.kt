package com.example.data.model

/**
 * Empirical Panel Data Models
 */
data class DelayVsDeathPoint(
    val year: Int,
    val waitTimeDays: Int,
    val deathsPending: Int
)

data class WaterfallStage(
    val stageName: String,
    val denialRate: Double,
    val approvalRate: Double,
    val volumeText: String,
    val avgWaitDays: Int,
    val description: String
)

data class CriminalJusticeImpact(
    val outcomeName: String,
    val percentIncrease: Int,
    val category: String,
    val description: String
)

data class IncomeMortalityKink(
    val incomeIncreaseUsd: Int,
    val mortalityReductionMinPct: Double,
    val mortalityReductionMaxPct: Double,
    val keyInsight: String
)

/**
 * Master Exhibit List Model
 */
data class ExhibitItem(
    val id: String,
    val legacyRef: String,
    val description: String,
    val parentContext: String,
    val relationships: List<String>
)

/**
 * Knowledge Graph Models
 */
enum class NodeType {
    EXHIBIT, CLAIM, WHITE_PAPER, CODE, DIAGRAM, DATASET, VERSION, CASE
}

data class GraphNode(
    val id: String,
    val label: String,
    val type: NodeType,
    val parentContext: String,
    val x: Float = 0f,
    val y: Float = 0f
)

data class DirectedEdge(
    val sourceId: String,
    val relationType: String,
    val targetId: String
)

data class CypherQueryItem(
    val title: String,
    val query: String,
    val description: String
)

/**
 * Financial Redemption & Bond Model
 */
data class BondRedemptionSpec(
    val principalAmount: Double = 500000000.0,
    val noteTranches: List<NoteTranche> = listOf(
        NoteTranche("6.625% Senior Notes due 2006", 350000000.0, 0.06625, 2006),
        NoteTranche("6.75% Senior Notes due 2009", 150000000.0, 0.0675, 2009)
    )
)

data class NoteTranche(
    val name: String,
    val principal: Double,
    val couponRate: Double,
    val maturityYear: Int
)

data class AssetMappingRow(
    val originalInstrument: String,
    val reorganizationCategory: String,
    val successorAsset: String,
    val clearedVenue: String
)

/**
 * PSDLEF & BSBS Housing & DOT Lawsuit Model
 */
data class MicrogridHousingSpec(
    val housingUnits: Int = 200, // Scaled down to 200 per user instructions
    val solarKwPerHome: Double = 12.5,
    val batteryKwhPerHome: Double = 25.0,
    val totalSolarMw: Double = 2.5,
    val totalStorageMwh: Double = 5.0,
    val dotGrantRequested: Double = 45000000.0
)

/**
 * Local Jurisdiction & Congressional Representation Model
 */
data class CourtDetails(
    val courtName: String,
    val circuit: String,
    val pacerDistrictCode: String,
    val ecfMdecEmail: String,
    val intakeDistributionEmail: String,
    val courthouseAddress: String,
    val clerkPhone: String,
    val usCourtsUrl: String,
    val pacerUrl: String
)

data class SenatorDetails(
    val name: String,
    val party: String,
    val office: String,
    val phone: String
)

data class RepresentativeDetails(
    val houseDistrict: String,
    val representativeName: String,
    val party: String,
    val capitolOffice: String,
    val phone: String,
    val constituentPortalUrl: String,
    val senators: List<SenatorDetails>
)

data class JurisdictionInfo(
    val zipCode: String,
    val city: String,
    val state: String,
    val county: String,
    val court: CourtDetails,
    val representation: RepresentativeDetails
)
