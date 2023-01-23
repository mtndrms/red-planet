package com.metin.projectnasa.common

import com.metin.projectnasa.common.Constants.DEFAULT_SOL_VALUE

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

fun getMaxSolValueByRover(rover: Int): Int {
    return when (rover) {
        0 -> Constants.MAX_SOL_CURIOSITY
        1 -> Constants.MAX_SOL_OPPORTUNITY
        2 -> Constants.MAX_SOL_SPIRIT
        else -> {
            DEFAULT_SOL_VALUE
        }
    }
}
