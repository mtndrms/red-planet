package com.metin.projectnasa.utils

object Constants {
    const val NO_LIMIT_API_BASE_URL = "http://mars-photos.herokuapp.com/api/v1/rovers/"
    const val NASA_API_BASE_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/"
    const val API_KEY = "DEMO_KEY"
    const val TEST_IMAGE_URL =
        "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01000/opgs/edr/fcam/FRB_486265257EDR_F0481570FHAZ00323M_.JPG"

    val rovers = listOf("Curiosity", "Opportunity", "Spirit")
    val curiosityCameraTypes = listOf("FHAZ", "RHAZ", "MAST", "CHEMCAM", "MAHLI", "MARDI", "NAVCAM")
    val opportunityCameraTypes = listOf("FHAZ", "RHAZ", "NAVCAM", "PANCAM", "MINITES")
    val spiritCameraTypes = listOf("FHAZ", "RHAZ", "NAVCAM", "PANCAM", "MINITES")
}