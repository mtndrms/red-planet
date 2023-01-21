package com.metin.projectnasa.utils

import com.metin.projectnasa.data.model.Photo

fun MutableList<Photo>.filterByCamera(
    checkedChipId: Int,
    rover: Int
): MutableList<Photo> {
    val checkedCameraType = getCameraTypesByRover(rover)[checkedChipId]
    return this.filter {
        it.camera.name == checkedCameraType
    }.toMutableList()
}