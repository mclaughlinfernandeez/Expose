package com.example.data.repository

import com.example.data.model.*

object JurisdictionRepository {

    // Featured quick ZIP codes for demo & rapid testing
    val featuredZipCodes = listOf(
        "21201" to "Baltimore, MD (D. Md.)",
        "20001" to "Washington, DC (D.D.C.)",
        "10001" to "New York, NY (S.D.N.Y.)",
        "60601" to "Chicago, IL (N.D. Ill.)",
        "90001" to "Los Angeles, CA (C.D. Cal.)",
        "75001" to "Dallas, TX (N.D. Tex.)",
        "30301" to "Atlanta, GA (N.D. Ga.)",
        "98101" to "Seattle, WA (W.D. Wash.)"
    )

    private val presetDatabase = mapOf(
        "21201" to JurisdictionInfo(
            zipCode = "21201",
            city = "Baltimore",
            state = "MD",
            county = "Baltimore City",
            court = CourtDetails(
                courtName = "U.S. District Court for the District of Maryland",
                circuit = "4th Circuit Court of Appeals",
                pacerDistrictCode = "mdd",
                ecfMdecEmail = "mdec_cases@mdd.uscourts.gov",
                intakeDistributionEmail = "case_intake_distribution@mdd.uscourts.gov",
                courthouseAddress = "101 W. Lombard Street, Chamber 4A, Baltimore, MD 21201",
                clerkPhone = "(410) 962-2600",
                usCourtsUrl = "https://www.mdd.uscourts.gov",
                pacerUrl = "https://pacer.uscourts.gov"
            ),
            representation = RepresentativeDetails(
                houseDistrict = "MD-07",
                representativeName = "Rep. Kweisi Mfume",
                party = "Democrat",
                capitolOffice = "2263 Rayburn House Office Building, Washington, DC 20515",
                phone = "(202) 225-4741",
                constituentPortalUrl = "https://mfume.house.gov/contact",
                senators = listOf(
                    SenatorDetails("Sen. Ben Cardin", "Democrat", "509 Hart Senate Office Bldg, Washington, DC 20510", "(202) 224-4524"),
                    SenatorDetails("Sen. Chris Van Hollen", "Democrat", "110 Hart Senate Office Bldg, Washington, DC 20510", "(202) 224-4654")
                )
            )
        ),
        "20001" to JurisdictionInfo(
            zipCode = "20001",
            city = "Washington",
            state = "DC",
            county = "District of Columbia",
            court = CourtDetails(
                courtName = "U.S. District Court for the District of Columbia",
                circuit = "D.C. Circuit Court of Appeals",
                pacerDistrictCode = "dcd",
                ecfMdecEmail = "dcd_ecf_filings@dcd.uscourts.gov",
                intakeDistributionEmail = "case_intake_notice@dcd.uscourts.gov",
                courthouseAddress = "E. Barrett Prettyman Courthouse, 333 Constitution Ave NW, Washington, DC 20001",
                clerkPhone = "(202) 354-3000",
                usCourtsUrl = "https://www.dcd.uscourts.gov",
                pacerUrl = "https://pacer.uscourts.gov"
            ),
            representation = RepresentativeDetails(
                houseDistrict = "DC-AL",
                representativeName = "Del. Eleanor Holmes Norton",
                party = "Democrat",
                capitolOffice = "2135 Rayburn House Office Building, Washington, DC 20515",
                phone = "(202) 225-8050",
                constituentPortalUrl = "https://norton.house.gov/contact",
                senators = listOf(
                    SenatorDetails("Shadow Sen. Paul Strauss", "Democrat", "1350 Pennsylvania Ave NW, Washington, DC 20004", "(202) 727-7890"),
                    SenatorDetails("Shadow Sen. Michael D. Brown", "Democrat", "1350 Pennsylvania Ave NW, Washington, DC 20004", "(202) 727-7890")
                )
            )
        ),
        "10001" to JurisdictionInfo(
            zipCode = "10001",
            city = "New York",
            state = "NY",
            county = "New York County (Manhattan)",
            court = CourtDetails(
                courtName = "U.S. District Court for the Southern District of New York (SDNY)",
                circuit = "2nd Circuit Court of Appeals",
                pacerDistrictCode = "nysd",
                ecfMdecEmail = "ecf_cases@nysd.uscourts.gov",
                intakeDistributionEmail = "case_distribution_list@nysd.uscourts.gov",
                courthouseAddress = "500 Pearl Street, Daniel Patrick Moynihan Courthouse, New York, NY 10007",
                clerkPhone = "(212) 805-0136",
                usCourtsUrl = "https://www.nysd.uscourts.gov",
                pacerUrl = "https://pacer.uscourts.gov"
            ),
            representation = RepresentativeDetails(
                houseDistrict = "NY-12",
                representativeName = "Rep. Jerrold Nadler",
                party = "Democrat",
                capitolOffice = "2132 Rayburn House Office Building, Washington, DC 20515",
                phone = "(202) 225-5635",
                constituentPortalUrl = "https://nadler.house.gov/contact",
                senators = listOf(
                    SenatorDetails("Sen. Chuck Schumer", "Democrat", "322 Hart Senate Office Bldg, Washington, DC 20510", "(202) 224-6542"),
                    SenatorDetails("Sen. Kirsten Gillibrand", "Democrat", "478 Russell Senate Office Bldg, Washington, DC 20510", "(202) 224-4451")
                )
            )
        )
    )

    fun getJurisdictionByZip(inputZip: String): JurisdictionInfo {
        val cleanZip = inputZip.trim().take(5)
        if (cleanZip in presetDatabase) {
            return presetDatabase[cleanZip]!!
        }

        // Dynamic lookup based on ZIP code prefix
        val prefix = cleanZip.take(2).toIntOrNull() ?: 21
        val (state, city, distName, pacerCode, circuit) = resolveStateByZipPrefix(prefix)

        return JurisdictionInfo(
            zipCode = if (cleanZip.length == 5) cleanZip else "21201",
            city = city,
            state = state,
            county = "$city Jurisdiction",
            court = CourtDetails(
                courtName = "U.S. District Court for the $distName",
                circuit = circuit,
                pacerDistrictCode = pacerCode,
                ecfMdecEmail = "mdec_cases@${pacerCode}.uscourts.gov",
                intakeDistributionEmail = "case_intake_distribution@${pacerCode}.uscourts.gov",
                courthouseAddress = "Federal Courthouse & Post Office Bldg, $city, $state $cleanZip",
                clerkPhone = "(800) 555-USCT",
                usCourtsUrl = "https://www.${pacerCode}.uscourts.gov",
                pacerUrl = "https://pacer.uscourts.gov"
            ),
            representation = RepresentativeDetails(
                houseDistrict = "$state-01",
                representativeName = "Member of Congress ($state 1st District)",
                party = "U.S. House of Representatives",
                capitolOffice = "House Office Building, Washington, DC 20515",
                phone = "(202) 225-3121 (Capitol Switchboard)",
                constituentPortalUrl = "https://www.house.gov/representatives/find-your-representative",
                senators = listOf(
                    SenatorDetails("Senior Senator ($state)", "U.S. Senate", "Senate Office Bldg, Washington, DC 20510", "(202) 224-3121"),
                    SenatorDetails("Junior Senator ($state)", "U.S. Senate", "Senate Office Bldg, Washington, DC 20510", "(202) 224-3121")
                )
            )
        )
    }

    private fun resolveStateByZipPrefix(prefix: Int): TupleState {
        return when (prefix) {
            in 0..2 -> TupleState("MA", "Boston", "District of Massachusetts", "mad", "1st Circuit")
            in 3..4 -> TupleState("ME", "Portland", "District of Maine", "med", "1st Circuit")
            in 5..6 -> TupleState("VT", "Burlington", "District of Vermont", "vtd", "2nd Circuit")
            in 7..8 -> TupleState("NJ", "Newark", "District of New Jersey", "njd", "3rd Circuit")
            in 10..14 -> TupleState("NY", "New York", "Southern District of New York", "nysd", "2nd Circuit")
            in 15..19 -> TupleState("PA", "Philadelphia", "Eastern District of Pennsylvania", "paed", "3rd Circuit")
            in 20..21 -> TupleState("MD", "Baltimore", "District of Maryland", "mdd", "4th Circuit")
            in 22..24 -> TupleState("VA", "Richmond", "Eastern District of Virginia", "vaed", "4th Circuit")
            in 27..28 -> TupleState("NC", "Raleigh", "Eastern District of North Carolina", "nced", "4th Circuit")
            in 30..31 -> TupleState("GA", "Atlanta", "Northern District of Georgia", "gand", "11th Circuit")
            in 32..34 -> TupleState("FL", "Miami", "Southern District of Florida", "flsd", "11th Circuit")
            in 35..36 -> TupleState("AL", "Birmingham", "Northern District of Alabama", "alnd", "11th Circuit")
            in 37..38 -> TupleState("TN", "Nashville", "Middle District of Tennessee", "tnmd", "6th Circuit")
            in 40..42 -> TupleState("KY", "Louisville", "Western District of Kentucky", "kywd", "6th Circuit")
            in 43..45 -> TupleState("OH", "Columbus", "Southern District of Ohio", "ohsd", "6th Circuit")
            in 46..47 -> TupleState("IN", "Indianapolis", "Southern District of Indiana", "insd", "7th Circuit")
            in 48..49 -> TupleState("MI", "Detroit", "Eastern District of Michigan", "mied", "6th Circuit")
            in 60..62 -> TupleState("IL", "Chicago", "Northern District of Illinois", "ilnd", "7th Circuit")
            in 63..65 -> TupleState("MO", "St. Louis", "Eastern District of Missouri", "moed", "8th Circuit")
            in 70..71 -> TupleState("LA", "New Orleans", "Eastern District of Louisiana", "laed", "5th Circuit")
            in 73..74 -> TupleState("OK", "Oklahoma City", "Western District of Oklahoma", "okwd", "10th Circuit")
            in 75..79 -> TupleState("TX", "Dallas", "Northern District of Texas", "txnd", "5th Circuit")
            in 80..81 -> TupleState("CO", "Denver", "District of Colorado", "cod", "10th Circuit")
            in 84..85 -> TupleState("UT", "Salt Lake City", "District of Utah", "utd", "10th Circuit")
            in 85..86 -> TupleState("AZ", "Phoenix", "District of Arizona", "azd", "9th Circuit")
            in 90..96 -> TupleState("CA", "Los Angeles", "Central District of California", "cacd", "9th Circuit")
            in 97..97 -> TupleState("OR", "Portland", "District of Oregon", "ord", "9th Circuit")
            in 98..99 -> TupleState("WA", "Seattle", "Western District of Washington", "wawd", "9th Circuit")
            else -> TupleState("MD", "Baltimore", "District of Maryland", "mdd", "4th Circuit")
        }
    }

    private data class TupleState(
        val state: String,
        val city: String,
        val distName: String,
        val pacerCode: String,
        val circuit: String
    )

    /**
     * Pre-formatted Transmittal Letter to Congressional Representative for Fund Release & Sovereign Trust
     */
    fun buildCongressionalTransmittalText(info: JurisdictionInfo, grantAmountUsd: String = "$45,000,000"): String {
        return """
FORMAL CONSTITUENT PETITION & LEGISLATIVE PROPOSAL FOR FUND DISPERSAL RELEASE
Target Office: ${info.representation.representativeName} (${info.representation.houseDistrict})
Capitol Office: ${info.representation.capitolOffice}
Constituent Jurisdiction: ${info.city}, ${info.state} ${info.zipCode}

RE: Request for Immediate Release & Dispersal of Sovereign Trust Funds, Clean Energy Microgrid Grants ($grantAmountUsd), and Emergency Disability Administrative Adjudication Relief.

Dear Representative ${info.representation.representativeName},

I am writing as a constituent residing in ${info.city}, ${info.state} (${info.zipCode}). 

We request your legislative assistance and direct congressional inquiry regarding the formal release and dispersal of authorized federal infrastructure and disability trust funds:

1. FUND DISPERSAL RELEASE: Release of $grantAmountUsd under Inflation Reduction Act (IRA) and Infrastructure Investment & Jobs Act (IIJA) clean energy microgrid programs (2.5MW Solar / 5.0MWh Battery Storage for 200 local residential units).
2. DISABILITY SOVEREIGN TRUST OVERSIGHT: Congressional inquiry into SSA Administrative Law Judge (ALJ) backlog reduction and mandatory application of 20 C.F.R. § 404.1520e residual functional capacity guidelines.
3. LOCAL JUDICIAL NOTICE: Copy of formal Mandamus filing transmitted concurrently to the ${info.court.courtName} MDEC / PACER Case Intake Distribution List.

We urge your office to initiate a constituent service inquiry with the Department of Transportation (DOT), Department of Energy (DOE), and Social Security Administration (SSA) to expedite fund release.

Respectfully submitted,
Constituent of ${info.representation.houseDistrict}
ZIP Code: ${info.zipCode}
Transmitted via Sovereign Analytics & Legal Dispatch Platform
        """.trimIndent()
    }

    /**
     * Pre-formatted Legal Notice for MDEC / PACER Case Intake Distribution List
     */
    fun buildMdecCourtNoticeText(info: JurisdictionInfo): String {
        return """
IN THE UNITED STATES DISTRICT COURT FOR THE ${info.court.courtName.uppercase()}
ELECTRONIC CASE FILING (ECF) & MDEC CASE DISTRIBUTION NOTICE

TO: Office of the Clerk / Electronic Case Intake Distribution
E-Mail: ${info.court.ecfMdecEmail}
Distribution List: ${info.court.intakeDistributionEmail}
Circuit Jurisdiction: ${info.court.circuit}
Courthouse: ${info.court.courthouseAddress}

SUBJECT: NOTICE OF PETITION FOR WRIT OF MANDAMUS & FUND DISPERSAL OVERSIGHT
FILING VENUE: ${info.court.courtName} (PACER Org Code: ${info.court.pacerDistrictCode})

NOTICE IS HEREBY GIVEN that petitioner has submitted a formal Request for Release of Funds and Administrative Disability Adjudication Mandamus pursuant to 28 U.S.C. § 1361 and 42 U.S.C. § 405(g).

KEY PARTICULARS:
1. Jurisdiction: ${info.city}, ${info.state} (${info.zipCode}) - ${info.court.circuit}.
2. Concurrent Notice: Transmitted to Congressional Representative ${info.representation.representativeName} (${info.representation.houseDistrict}).
3. Case Distribution List Email: ${info.court.ecfMdecEmail} / ${info.court.intakeDistributionEmail}.
4. Public Access to Court Electronic Records (PACER): Listed under court code '${info.court.pacerDistrictCode}'.

Please index this notice into the MDEC / PACER electronic distribution queue for local civil intake.

DATED: ${java.time.LocalDate.now()}
TRANSMITTED BY: Sovereign Legal & Jurisdiction Dispatch System
        """.trimIndent()
    }
}
