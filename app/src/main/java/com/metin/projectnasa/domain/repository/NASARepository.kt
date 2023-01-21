package com.metin.projectnasa.domain.repository

import com.metin.projectnasa.data.model.NASAResponse
import retrofit2.Call

interface NASARepository {
    fun getPhotosByRover(
        roverName: String,
        sol: Int,
        camera: String? = null,
        api_key: String? = null,
        page: Int
    ): Call<NASAResponse>
}