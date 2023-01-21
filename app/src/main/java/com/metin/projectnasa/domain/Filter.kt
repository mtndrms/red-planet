package com.metin.projectnasa.domain

import com.metin.projectnasa.data.model.Photo
import com.metin.projectnasa.utils.Constants

fun List<Photo>.filterByCamera(
    checkedChipIds: List<Int>,
    rover: Int
): List<Photo> {
    val checkedCameraTypes = getCheckedCameraTypes(checkedChipIds, rover)
    return this.filter {
        checkedCameraTypes.contains(it.camera.name)
    }
}

private fun getCheckedCameraTypes(checkedChipIds: List<Int>, rover: Int): List<String> {
    val camerasOfThisRover = getCameraTypesByRover(rover)
    val checkedCameras = mutableListOf<String>()

    checkedChipIds.forEach {
        checkedCameras.add(camerasOfThisRover[it])
    }

    return checkedCameras
}

private fun getCameraTypesByRover(rover: Int): List<String> {
    return when (rover) {
        0 -> Constants.curiosityCameraTypes
        1 -> Constants.opportunityCameraTypes
        2 -> Constants.spiritCameraTypes
        else -> {
            listOf()
        }
    }
}