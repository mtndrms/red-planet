package com.metin.projectnasa.domain.repository

import com.metin.projectnasa.data.model.NASAResponseDto
import retrofit2.Call

interface NASARepository {
    fun getPhotosByRover(
        roverName: String,
        sol: Int = 1000,
        camera: String? = null,
        api_key: String? = null,
        page: Int
    ): Call<NASAResponseDto>
}