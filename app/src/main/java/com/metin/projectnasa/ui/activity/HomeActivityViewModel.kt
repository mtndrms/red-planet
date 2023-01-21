package com.metin.projectnasa.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metin.projectnasa.data.model.NASAResponse
import com.metin.projectnasa.data.model.Photo
import com.metin.projectnasa.domain.repository.NASARepository
import com.metin.projectnasa.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeActivityViewModel @Inject constructor(private val repository: NASARepository) :
    ViewModel() {
    private val photosList: MutableList<Photo> = mutableListOf()

    private val _photos = MutableLiveData<HomeActivityState<MutableList<Photo>>>()
    val photos: LiveData<HomeActivityState<MutableList<Photo>>>
        get() = _photos

    // load first data as soon as new instance created
    init {
        loadData(0, 1)
    }

    fun resetData() {
        _photos.value?.data?.clear()
        photosList.clear()
    }

    fun loadData(activeTab: Int, page: Int, camera: String? = null) {
        _photos.value = HomeActivityState.Loading(null)
        requestData(camera, activeTab, page).enqueue(object : Callback<NASAResponse> {
            override fun onResponse(call: Call<NASAResponse>, response: Response<NASAResponse>) {
                if (response.isSuccessful) {
                    photosList.addAll((response.body() as NASAResponse).photos)
                    (_photos.value as HomeActivityState.Loading<MutableList<Photo>>).data =
                        photosList
                    _photos.value = HomeActivityState.Success(photosList)
                } else {
                    _photos.value = HomeActivityState.Error(response.code().toString(), null)
                }
            }

            override fun onFailure(call: Call<NASAResponse>, t: Throwable) {
                println(t.message.toString())
                _photos.value = HomeActivityState.Error(t.message.toString(), null)
            }
        })
    }

    fun pushFilteredData(filtered: List<Photo>) {
        photosList.addAll(filtered)
        _photos.value = HomeActivityState.Success(photosList)
    }

    private fun requestData(camera: String? = null, activeTab: Int, page: Int): Call<NASAResponse> {
        return repository.getPhotosByRover(
            roverName = Constants.rovers[activeTab].lowercase(),
            sol = 1000,
            camera = camera,
            page = page
        )
    }
}