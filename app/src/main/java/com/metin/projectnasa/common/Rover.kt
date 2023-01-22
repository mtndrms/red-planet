package com.metin.projectnasa.common

import com.metin.projectnasa.common.Constants

fun getCameraTypesByRover(rover: Int): List<String> {
    return when (rover) {
        0 -> Constants.cameraTypesCuriosity
        1 -> Constants.cameraTypesOpportunity
        2 -> Constants.cameraTypesSpirit
        else -> {
            listOf()
        }
    }
}
