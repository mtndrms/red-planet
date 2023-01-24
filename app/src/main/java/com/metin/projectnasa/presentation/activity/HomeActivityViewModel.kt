package com.metin.projectnasa.presentation.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metin.projectnasa.domain.repository.NASARepository
import com.metin.projectnasa.common.Constants
import com.metin.projectnasa.common.Constants.DEFAULT_ROVER
import com.metin.projectnasa.common.Constants.DEFAULT_SOL_VALUE
import com.metin.projectnasa.common.Resource
import com.metin.projectnasa.common.filterByCamera
import com.metin.projectnasa.data.model.NASAResponseDto
import com.metin.projectnasa.data.model.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeActivityViewModel @Inject constructor(private val repository: NASARepository) :
    ViewModel() {
    val allPhotos: MutableList<Photo> = mutableListOf()
    private var filteredPhotos: List<Photo> = emptyList()

    private val _photos = MutableLiveData<Resource<List<Photo>>>()
    val photos: LiveData<Resource<List<Photo>>>
        get() = _photos

    // load first data as soon as new instance created
    init {
        loadData(roverName = DEFAULT_ROVER, page = 1, sol = DEFAULT_SOL_VALUE)
    }

    fun resetData() {
        allPhotos.clear()
        _photos.value = Resource.Success(allPhotos)
    }

    fun loadData(roverName: String, sol: Int, page: Int, camera: String? = null) {
        _photos.value = Resource.Loading(null)
        requestData(camera = camera, sol = sol, roverName = roverName, page = page).enqueue(object :
            Callback<NASAResponseDto> {
            override fun onResponse(
                call: Call<NASAResponseDto>,
                response: Response<NASAResponseDto>
            ) {
                if (response.isSuccessful) {
                    allPhotos.addAll((response.body() as NASAResponseDto).photos)
                    _photos.value = Resource.Success(allPhotos)
                } else {
                    _photos.value = Resource.Error(response.code().toString(), null)
                }
            }

            override fun onFailure(call: Call<NASAResponseDto>, t: Throwable) {
                println(t.message.toString())
                _photos.value = Resource.Error(t.message.toString(), null)
            }
        })
    }

    // filter already fetched photos
    // halihazirda indirilmis fotograflari filtrele
    fun filter(selectedCamera: String?) {
        filteredPhotos = allPhotos.filterByCamera(selectedCamera)
        _photos.value = Resource.Success(filteredPhotos)
    }

    private fun requestData(
        camera: String? = null,
        roverName: String,
        sol: Int,
        page: Int
    ): Call<NASAResponseDto> {
        return repository.getPhotosByRover(
            roverName = roverName,
            camera = camera,
            api_key = Constants.API_KEY,
            sol = sol,
            page = page
        )
    }
}