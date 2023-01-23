package com.metin.projectnasa.data.service

import com.metin.projectnasa.data.model.NASAResponseDto
import com.metin.projectnasa.data.model.RoverX
import com.metin.projectnasa.data.model.Rovers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NASAService {
    @GET("rovers")
    fun getAllRovers(): Call<Rovers>

    @GET("rovers/{rover}")
    fun getRover(@Path("rover") rover: String): Call<RoverX>

    @GET("rovers/{rover}/photos")
    fun getPhotosByRover(
        @Path("rover") roverName: String,
        @Query("sol") sol: Int,
        @Query("camera") camera: String?,
        @Query("api_key") api_key: String?,
        @Query("page") page: Int
    ): Call<NASAResponseDto>
}