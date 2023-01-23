package com.metin.projectnasa.common

object Constants {
    const val BASE_URL = "https://api.nasa.gov/mars-photos/api/v1/"
    const val API_KEY = "DEMO_KEY"

    const val DEFAULT_ACTIVE_TAB = 0
    const val DEFAULT_PAGE = 1
    const val DEFAULT_SOL_VALUE = 1000

    const val MAX_SOL_CURIOSITY = 3718
    const val MAX_SOL_OPPORTUNITY = 5111
    const val MAX_SOL_SPIRIT = 2208

    val allCameras = listOf(
        "FHAZ", "RHAZ", "MAST", "CHEMCAM", "MAHLI",
        "MARDI", "NAVCAM", "PANCAM", "MINITES"
    )
    val cameraTypesCuriosity = listOf("FHAZ", "RHAZ", "MAST", "CHEMCAM", "MAHLI", "MARDI", "NAVCAM")
    val cameraTypesOpportunity = listOf("FHAZ", "RHAZ", "NAVCAM", "PANCAM", "MINITES")
    val cameraTypesSpirit = listOf("FHAZ", "RHAZ", "NAVCAM", "PANCAM", "MINITES")

    val rovers = listOf("Curiosity", "Opportunity", "Spirit")
}