package com.metin.projectnasa.domain.repository

import com.metin.projectnasa.data.model.NASAResponseDto
import com.metin.projectnasa.data.model.RoverX
import com.metin.projectnasa.data.model.Rovers
import retrofit2.Call

interface NASARepository {
    fun getPhotosByRover(
        roverName: String,
        sol: Int,
        camera: String? = null,
        api_key: String? = null,
        page: Int
    ): Call<NASAResponseDto>

    fun getAllRovers(): Call<Rovers>

    fun getRover(rover: String): Call<RoverX>
}