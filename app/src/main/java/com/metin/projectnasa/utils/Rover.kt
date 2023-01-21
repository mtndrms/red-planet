package com.metin.projectnasa.utils

fun getCameraTypesByRover(rover: Int): List<String> {
    return when (rover) {
        0 -> Constants.curiosityCameraTypes
        1 -> Constants.opportunityCameraTypes
        2 -> Constants.spiritCameraTypes
        else -> {
            listOf()
        }
    }
}
