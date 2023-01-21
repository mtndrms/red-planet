package com.metin.projectnasa.data.repository

import com.metin.projectnasa.data.model.NASAResponse
import com.metin.projectnasa.data.service.NASAService
import com.metin.projectnasa.domain.repository.NASARepository
import com.metin.projectnasa.utils.Constants
import retrofit2.Call
import javax.inject.Inject

class NASARepositoryImpl @Inject constructor(private val nasaService: NASAService) :
    NASARepository {
    override fun getPhotosByRover(
        roverName: String,
        sol: Int,
        camera: String?,
        api_key: String?,
        page: Int
    ): Call<NASAResponse> {
        return nasaService.getPhotosByRover(
            roverName,
            sol,
            camera,
            api_key,
            page
        )
    }
}