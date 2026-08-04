package com.example.data.repository

import com.example.data.model.*

object SovereignRepository {

    /**
     * Panel A: Deaths While Pending vs. Adjudication Delay (2017–2023)
     */
    val delayVsDeathData = listOf(
        DelayVsDeathPoint(2017, 111, 10000),
        DelayVsDeathPoint(2018, 128, 13200),
        DelayVsDeathPoint(2019, 145, 16500),
        DelayVsDeathPoint(2020, 162, 20100),
        DelayVsDeathPoint(2021, 180, 23800),
        DelayVsDeathPoint(2022, 198, 27200),
        DelayVsDeathPoint(2023, 217, 30000)
    )

    /**
     * Panel B: FY2025 Disability Decision Waterfall
     */
    val waterfallStages = listOf(
        WaterfallStage(
            stageName = "Initial Application",
            denialRate = 0.64,
            approvalRate = 0.36,
            volumeText = "2.1M Applicants",
            avgWaitDays = 220,
            description = "64% of applicants receive initial denial, forcing appeal into reconsideration."
        ),
        WaterfallStage(
            stageName = "Reconsideration Appeal",
            denialRate = 0.84,
            approvalRate = 0.16,
            volumeText = "1.34M Applicants",
            avgWaitDays = 210,
            description = "84% of reconsiderations are denied, causing administrative bottleneck."
        ),
        WaterfallStage(
            stageName = "ALJ Hearing Hearing",
            denialRate = 0.50,
            approvalRate = 0.50,
            volumeText = "550K Applicants",
            avgWaitDays = 480,
            description = "50% approved only after an additional 1+ year wait time (700+ cumulative days)."
        )
    )

    /**
     * Panel C: SSI Removal -> Criminal Justice Outcomes (Deshpande & Mueller-Smith 2021)
     */
    val criminalJusticeImpacts = listOf(
        CriminalJusticeImpact(
            outcomeName = "Criminal Charges",
            percentIncrease = 20,
            category = "Overall Recidivism",
            description = "Removal of SSI benefits at age 18 increases criminal charges by 20% over 20 years."
        ),
        CriminalJusticeImpact(
            outcomeName = "Income-Generating Offenses",
            percentIncrease = 60,
            category = "Economic Survival Offenses",
            description = "The loss of cash assistance drives a 60% increase in theft, burglary, and illicit trade."
        ),
        CriminalJusticeImpact(
            outcomeName = "Incarceration Rate",
            percentIncrease = 60,
            category = "Institutional Confinement",
            description = "Causal increase in long-term prison time directly tied to benefit elimination."
        )
    )

    /**
     * Panel D: DI Income Effect on Mortality (Gelber et al. 2023)
     */
    val incomeMortalityKink = IncomeMortalityKink(
        incomeIncreaseUsd = 1000,
        mortalityReductionMinPct = 0.18,
        mortalityReductionMaxPct = 0.35,
        keyInsight = "Each $1,000 increase in annual Disability Insurance benefits leads to a statistically significant 0.18–0.35 percentage point decrease in annual beneficiary mortality."
    )

    /**
     * Master Exhibit List Index (EXH-001 .. EXH-034)
     */
    val exhibitList = listOf(
        ExhibitItem(
            id = "EXH-001",
            legacyRef = "Exhibit A",
            description = "Police Report J/86 (P/O J. Marty #1286) – 'Loopers' Incident (Aug 13, 2024)",
            parentContext = "Master Legal Portfolio (Case D-01-CR-25-011312)",
            relationships = listOf("SUPPORTED_BY ► WP-001", "CONTRADICTS ► Allegation")
        ),
        ExhibitItem(
            id = "EXH-002",
            legacyRef = "Exhibit B",
            description = "Police Report #250000987 – Basement Security Door Destruction (Jan 3, 2025)",
            parentContext = "Master Legal Portfolio (Case D-01-CR-25-011312)",
            relationships = listOf("SUPPORTED_BY ► WP-001", "VERIFIED_BY ► Incident_Log")
        ),
        ExhibitItem(
            id = "EXH-003",
            legacyRef = "Exhibit C",
            description = "Police Report #1693 (P/O T. Oliver #1870) – De-escalation Log (Oct 12, 2024)",
            parentContext = "Master Legal Portfolio (Case D-01-CR-25-011312)",
            relationships = listOf("SUPPORTED_BY ► WP-001", "DOCUMENTED_IN ► Case_File")
        ),
        ExhibitItem(
            id = "EXH-004",
            legacyRef = "Exhibit D",
            description = "Medical Documentation – Nov 12, 2025 Unfounded Injury X-Ray Records",
            parentContext = "Master Legal Portfolio (Case D-01-CR-25-011312)",
            relationships = listOf("CONTRADICTS ► Allegation", "SUPPORTS ► Motion_To_Dismiss")
        ),
        ExhibitItem(
            id = "EXH-005",
            legacyRef = "Exhibit E",
            description = "Bench Warrant Verification Log – Active Warrants for J. Callicutt (Nov 16, 2025)",
            parentContext = "Master Legal Portfolio (Case D-01-CR-25-011312)",
            relationships = listOf("VERIFIES ► Status", "CITES ► Maryland_Judicial_Database")
        ),
        ExhibitItem(
            id = "EXH-006",
            legacyRef = "Exhibit F",
            description = "Stamped Copy of Federal Complaint & Temporary Restraining Order (TRO)",
            parentContext = "Master Legal Portfolio (Case D-01-CR-25-011312)",
            relationships = listOf("DOCUMENTED_IN ► Fed_Docket", "SUPPORTS ► Constitutional_Petition")
        ),
        ExhibitItem(
            id = "EXH-007",
            legacyRef = "Exhibit G",
            description = "Log of Video Evidence – Physical Altercation Footage (Feb–May 2025)",
            parentContext = "Master Legal Portfolio (Case D-01-CR-25-011312)",
            relationships = listOf("ILLUSTRATED_BY ► Video_Stream", "REFERENCES ► DATA-004")
        ),
        ExhibitItem(
            id = "EXH-008",
            legacyRef = "Exhibit A-1..A-3",
            description = "Rule 18 Appeal File – Notice of Appeal, Expulsion Order & Hearing Transcripts",
            parentContext = "SCOTUS Master Biocybernetic Portfolio (Vol I)",
            relationships = listOf("APPEARS_IN ► WP-002", "CONTAINS ► SEC-001")
        ),
        ExhibitItem(
            id = "EXH-009",
            legacyRef = "Exhibit A-5",
            description = "Student Neural Activity Report – Raw EEG Classroom Monitoring Data",
            parentContext = "Doe v. Ed. Board of Cognitive Compliance",
            relationships = listOf("REFERENCES ► DATA-001", "SUPPORTS ► CLM-001.12")
        ),
        ExhibitItem(
            id = "EXH-010",
            legacyRef = "Exhibit A-6",
            description = "Expert Witness Report – Attention Deficit Interpretation as Cognitive Liberty Violation",
            parentContext = "Doe v. Ed. Board of Cognitive Compliance",
            relationships = listOf("SUPPORTS ► CLM-001", "CITES ► CIT-104")
        ),
        ExhibitItem(
            id = "EXH-011",
            legacyRef = "Exhibit A-9",
            description = "Technical Blueprint – Classroom Neuro-Focus Systems & BCI Architecture",
            parentContext = "Doe v. Ed. Board of Cognitive Compliance",
            relationships = listOf("ILLUSTRATED_BY ► FIG-001", "IMPLEMENTED_BY ► RST-003")
        ),
        ExhibitItem(
            id = "EXH-012",
            legacyRef = "Exhibit A-17",
            description = "Statistical Error Analysis – False Positives in Classroom Neuro-Focus Algorithms",
            parentContext = "Doe v. Ed. Board of Cognitive Compliance",
            relationships = listOf("VERIFIED_BY ► TEST-001", "SUPPORTS ► Motion_For_Injunction")
        ),
        ExhibitItem(
            id = "EXH-013",
            legacyRef = "Exhibit A-18..A-20",
            description = "Interrogatories & Discovery – Algorithmic Black Boxes & Neuro-Profiling Evidence",
            parentContext = "Doe v. Ed. Board of Cognitive Compliance",
            relationships = listOf("DOCUMENTED_IN ► SEC-001", "CONTAINS ► SEC-002")
        ),
        ExhibitItem(
            id = "EXH-014",
            legacyRef = "Exhibit B",
            description = "Sworn Declaration of Caustin Lee McLaughlin – Genomic Extraction & Chain of Custody",
            parentContext = "Genomic Data Evidentiary Affidavit",
            relationships = listOf("VERIFIES ► DATA-002", "TAGS ► SHA-256")
        ),
        ExhibitItem(
            id = "EXH-015",
            legacyRef = "Exhibit B Loci Table",
            description = "Neurogenetic Marker Extraction Record (COMT, DRD2, DRD4, SLC6A3, FKBP5, BDNF, ADRA2A, HTR1B)",
            parentContext = "Genomic Data Evidentiary Affidavit",
            relationships = listOf("SUPPORTS ► CLM-002", "REFERENCES ► DATA-002")
        ),
        ExhibitItem(
            id = "EXH-016",
            legacyRef = "Exhibit A (Dissert.)",
            description = "Chromosomal Map of ADHD-Associated Loci (GRCh38 Manhattan Plot)",
            parentContext = "Consolidated Dissertation Brief",
            relationships = listOf("ILLUSTRATED_BY ► FIG-072", "APPEARS_IN ► WP-005")
        ),
        ExhibitItem(
            id = "EXH-017",
            legacyRef = "Exhibit B (Dissert.)",
            description = "Synaptic Cleft & Peripheral Modulators Diagram (COMT/DRD2 Pathways)",
            parentContext = "Consolidated Dissertation Brief",
            relationships = listOf("ILLUSTRATED_BY ► FIG-073", "APPEARS_IN ► WP-005")
        ),
        ExhibitItem(
            id = "EXH-018",
            legacyRef = "Exhibit C (Dissert.)",
            description = "GWAS Meta-Analysis Excerpt of ADHD (Nature Genetics, Feb 2023)",
            parentContext = "Consolidated Dissertation Brief",
            relationships = listOf("CITES ► CIT-398", "SUPPORTED_BY ► DATA-003")
        ),
        ExhibitItem(
            id = "EXH-019",
            legacyRef = "Exhibit D (Dissert.)",
            description = "Baltimore Solar & Battery Service (BSBS) Municipal Presentation & Technical Spec",
            parentContext = "Consolidated Dissertation Brief",
            relationships = listOf("SUPPORTED_BY ► WP-003", "IMPLEMENTED_BY ► RST-005")
        ),
        ExhibitItem(
            id = "EXH-020",
            legacyRef = "Exhibit E (Dissert.)",
            description = "Post-Quantum Secure Architecture Technical Addendum for Gov-LLM",
            parentContext = "Consolidated Dissertation Brief",
            relationships = listOf("IMPLEMENTED_BY ► RST-001", "VERIFIED_BY ► TEST-042")
        ),
        ExhibitItem(
            id = "EXH-021",
            legacyRef = "Exhibit A (Matrix)",
            description = "Comprehensive Procedural Timeline & Multi-Docket Index (USDC, USCA4, SCOTUS)",
            parentContext = "Procedural & Accommodation Brief",
            relationships = listOf("CONTAINS ► SEC-002", "PRECEDES ► EXH-022")
        ),
        ExhibitItem(
            id = "EXH-022",
            legacyRef = "Exhibit B (Matrix)",
            description = "Formal Request for ADA Title II / Sec 504 Accommodation (GovLLM / HARMONI-X)",
            parentContext = "Procedural & Accommodation Brief",
            relationships = listOf("SUPPORTS ► REQ-112", "DOCUMENTED_IN ► SEC-005")
        ),
        ExhibitItem(
            id = "EXH-023",
            legacyRef = "Exhibit C (Matrix)",
            description = "Neurogenetic Polygenic Risk Score Diagnostic Report (PRS = 0.92)",
            parentContext = "Procedural & Accommodation Brief",
            relationships = listOf("REFERENCES ► DATA-003", "SUPPORTS ► REQ-112")
        ),
        ExhibitItem(
            id = "EXH-024",
            legacyRef = "Exhibit D (Matrix)",
            description = "Hybrid Semantic Processing Architecture (HSPA) & RIGOR Specification Whitepaper",
            parentContext = "Procedural & Accommodation Brief",
            relationships = listOf("SUPPORTED_BY ► WP-005", "HAS_VERSION ► VER-2.4.1")
        ),
        ExhibitItem(
            id = "EXH-025",
            legacyRef = "Exhibit E (Matrix)",
            description = "State Court Peace Order & Criminal Statement of Charges Dossier",
            parentContext = "Procedural & Accommodation Brief",
            relationships = listOf("DOCUMENTED_IN ► Case_File", "SUPPORTS ► EXH-032")
        ),
        ExhibitItem(
            id = "EXH-026",
            legacyRef = "Exhibit F (Matrix)",
            description = "Post-Quantum Cryptographic Evidence Ledger & Audit Trail Manifests (NIST FIPS 203/204)",
            parentContext = "Procedural & Accommodation Brief",
            relationships = listOf("TAGS ► SHA-256", "IMPLEMENTED_BY ► RST-001")
        ),
        ExhibitItem(
            id = "EXH-027",
            legacyRef = "Trust Ref XI-1",
            description = "Comprehensive Neurophysiological & Genetic Phenotypic Report (July 2026)",
            parentContext = "Sovereign Trust & Academic Portfolio",
            relationships = listOf("SUPPORTS ► CLM-003", "REFERENCES ► DATA-003")
        ),
        ExhibitItem(
            id = "EXH-028",
            legacyRef = "Trust Ref XI-2",
            description = "Expanded Neurogenetic Architecture of ADHD / SSA Blue-Book Crosswalk",
            parentContext = "Sovereign Trust & Academic Portfolio",
            relationships = listOf("DOCUMENTED_IN ► SEC-004", "SUPPORTS ► REQ-115")
        ),
        ExhibitItem(
            id = "EXH-029",
            legacyRef = "Trust Ref XI-4",
            description = "FDA 510(k) Premarket Notification K220456 Documentation (ADHD PRS Kit)",
            parentContext = "Sovereign Trust & Academic Portfolio",
            relationships = listOf("CITES ► CIT-399", "VERIFIED_BY ► TEST-018")
        ),
        ExhibitItem(
            id = "EXH-030",
            legacyRef = "Trust Ref XI-5",
            description = "Functional Magnetic Resonance Imaging (fMRI) Brain Scan Diagnostics Report",
            parentContext = "Sovereign Trust & Academic Portfolio",
            relationships = listOf("ILLUSTRATED_BY ► FIG-074", "SUPPORTS ► CLM-003")
        ),
        ExhibitItem(
            id = "EXH-031",
            legacyRef = "Trust Ref XI-7",
            description = "Notice of Judgment, Case No. D-01-CR-25-011146 (Nolle Prosequi, Jan 30, 2026)",
            parentContext = "Sovereign Trust & Academic Portfolio",
            relationships = listOf("VERIFIES ► Dismissal", "SUPPORTS ► EXH-032")
        ),
        ExhibitItem(
            id = "EXH-032",
            legacyRef = "Trust Ref XI-8",
            description = "Plaintiff Complaint, McLaughlin v. State of Maryland, No. 1:25-cv-04301 (D. Md.)",
            parentContext = "Sovereign Trust & Academic Portfolio",
            relationships = listOf("HAS_EXHIBIT ► EXH-025", "DOCUMENTED_IN ► Fed_Docket")
        ),
        ExhibitItem(
            id = "EXH-033",
            legacyRef = "Trust Ref XI-9",
            description = "Memorandum for Motion for Summary Judgment, McLaughlin v. Comm'r Soc. Sec., No. 25-1522",
            parentContext = "Sovereign Trust & Academic Portfolio",
            relationships = listOf("SUPPORTS ► CLM-004", "CITES ► CIT-401")
        ),
        ExhibitItem(
            id = "EXH-034",
            legacyRef = "Trust Ref XI-13",
            description = "PSDLEF Sovereign Exchange Trust Deed, Trust Indenture, and Corporate Charter",
            parentContext = "Sovereign Trust & Academic Portfolio",
            relationships = listOf("VERSIONED_AS ► VER-1.0", "RELEASES ► Trust_Artifacts")
        )
    )

    /**
     * Graph Nodes & Directed Edges for Topology Canvas
     */
    val graphNodes = listOf(
        GraphNode("PAT-001", "Hybrid Semantic Arch", NodeType.WHITE_PAPER, "PAT-001 / WP-005 / EXH-024"),
        GraphNode("CLM-001.12", "Genomic Claim", NodeType.CLAIM, "Patent Loci Claim"),
        GraphNode("WP-001", "Legal Dossier", NodeType.WHITE_PAPER, "Master Legal Portfolio"),
        GraphNode("RST-001", "PQC Cryptography", NodeType.CODE, "NIST FIPS 203/204"),
        GraphNode("FN-210", "Verify Engine", NodeType.CODE, "Rust Semantic Engine"),
        GraphNode("FIG-072", "GRCh38 Plot", NodeType.DIAGRAM, "Manhattan Plot Loci"),
        GraphNode("EXH-001", "Police Report", NodeType.EXHIBIT, "Loopers Incident"),
        GraphNode("EXH-004", "Medical Record", NodeType.EXHIBIT, "X-Ray Records"),
        GraphNode("SRC-142", "Rust Hash Chain", NodeType.CODE, "PQC Ledger"),
        GraphNode("TEST-042", "PQC Test Suite", NodeType.CODE, "Verification Test"),
        GraphNode("DATA-003", "PRS Score 0.92", NodeType.DATASET, "Polygenic Risk Score"),
        GraphNode("Case D-01", "District Court Case", NodeType.CASE, "Case D-01-CR-25-011312"),
        GraphNode("Motion Dismiss", "Motion to Dismiss", NodeType.CLAIM, "Constitutional Motion")
    )

    val graphEdges = listOf(
        DirectedEdge("PAT-001", "IMPLEMENTED_BY", "CLM-001.12"),
        DirectedEdge("PAT-001", "IMPLEMENTED_BY", "WP-001"),
        DirectedEdge("PAT-001", "IMPLEMENTED_BY", "RST-001"),
        DirectedEdge("CLM-001.12", "IMPLEMENTED_BY", "FN-210"),
        DirectedEdge("CLM-001.12", "ILLUSTRATED_BY", "FIG-072"),
        DirectedEdge("WP-001", "SUPPORTED_BY", "EXH-001"),
        DirectedEdge("WP-001", "SUPPORTED_BY", "EXH-004"),
        DirectedEdge("RST-001", "DEFINED_IN", "SRC-142"),
        DirectedEdge("RST-001", "VERIFIED_BY", "TEST-042"),
        DirectedEdge("FN-210", "DEFINED_IN", "SRC-142"),
        DirectedEdge("FIG-072", "SUPPORTED_BY", "DATA-003"),
        DirectedEdge("EXH-001", "DOCUMENTED_IN", "Case D-01"),
        DirectedEdge("EXH-004", "SUPPORTS", "Motion Dismiss")
    )

    /**
     * Cypher Queries
     */
    val cypherQueries = listOf(
        CypherQueryItem(
            title = "1. End-to-End Patent Claim to Rust & Evidence",
            query = """MATCH (claim:PatentClaim {id: "CLM-001.12"})-[:IMPLEMENTED_BY]->(fn:Function)-[:DEFINED_IN]->(src:RustFile)
MATCH (claim)-[:ILLUSTRATED_BY]->(fig:Figure)-[:APPEARS_IN]->(wp:WhitePaper)
MATCH (claim)-[:SUPPORTED_BY]->(exh:Exhibit)
RETURN claim.id AS Claim, fn.title AS Function, src.path AS SourceCode, fig.caption AS Diagram, wp.title AS Paper, exh.id AS Evidence""",
            description = "Traces full lineage from genomic patent claim down to compiled Rust verification source, diagram, whitepaper, and physical evidence exhibit."
        ),
        CypherQueryItem(
            title = "2. Exhibits Supporting Active Constitutional Complaints",
            query = """MATCH (exh:Exhibit)-[:DOCUMENTED_IN|SUPPORTS]->(caseNode)
WHERE caseNode.id IN ["Case D-01-CR-25-011312", "1:25-cv-04301"]
RETURN exh.id AS ExhibitID, exh.title AS Description, caseNode.id AS Jurisdiction
ORDER BY exh.id ASC""",
            description = "Filters all physical and digital exhibits attached to active dockets in District Court and Federal Civil Rights filings."
        ),
        CypherQueryItem(
            title = "3. Track Modifications Across Portfolio Release (VER-2.5.0)",
            query = """MATCH (v:Version {id: "VER-2.5.0"})-[:MODIFIED|UPDATED|ADDED]->(artifact)
RETURN v.id AS ReleaseVersion, labels(artifact) AS ArtifactType, artifact.id AS ArtifactID, artifact.title AS ArtifactName""",
            description = "Tracks all mutated legal nodes, cryptographic modules, and exhibits modified under the latest system version."
        )
    )

    /**
     * Asset Mapping Table (USEC Inc -> Centrus Energy LEU)
     */
    val assetMappings = listOf(
        AssetMappingRow(
            originalInstrument = "USEC Inc. 1999 Senior Notes ($500M)",
            reorganizationCategory = "Class 4 Allowed Unsecured Claim",
            successorAsset = "Reorganized Common Stock (LEU) / Pro-Rata Senior Debt",
            clearedVenue = "Banco Santander, S.A. (DTC Participant Code / Escrow)"
        ),
        AssetMappingRow(
            originalInstrument = "USEC Proprietary Equities",
            reorganizationCategory = "Cancelled / Extinguished",
            successorAsset = "Warrants / Diluted Equity Pools",
            clearedVenue = "Centrus Energy Corp. Corporate Ledger"
        )
    )
}
