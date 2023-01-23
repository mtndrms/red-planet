package com.metin.projectnasa.domain.repository

import com.metin.projectnasa.common.Constants.DEFAULT_SOL_VALUE
import com.metin.projectnasa.data.model.NASAResponseDto
import retrofit2.Call

interface NASARepository {
    fun getPhotosByRover(
        roverName: String,
        sol: Int,
        camera: String? = null,
        api_key: String? = null,
        page: Int
    ): Call<NASAResponseDto>
}