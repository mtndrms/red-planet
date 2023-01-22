package com.metin.projectnasa.presentation.fragment.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilterViewModel: ViewModel() {
    private val _selectedCamera = MutableLiveData<String>()
    val selectedCamera: LiveData<String>
        get() = _selectedCamera

    fun selectCamera(camera: String) {
        _selectedCamera.value = camera
    }
}