package com.metin.projectnasa.common

import com.metin.projectnasa.data.model.Photo
import com.metin.projectnasa.presentation.activity.HomeActivityViewModel

fun List<Photo>.filterByCamera(
    camera: String?
): List<Photo> {
    return if (camera != null) {
        this.filter { it.camera.name == camera }
    } else {
        this
    }
}