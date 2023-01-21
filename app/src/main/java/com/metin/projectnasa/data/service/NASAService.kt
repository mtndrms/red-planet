package com.metin.projectnasa.data.service

import com.metin.projectnasa.data.model.NASAResponse
import com.metin.projectnasa.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NASAService {

    @GET("{roverName}/photos")
    fun getPhotosByRover(
        @Path("roverName") roverName: String,
        @Query("sol") sol: Int,
        @Query("camera") camera: String? = null,
        @Query("api_key") api_key: String? = null,
        @Query("page") page: Int
    ): Call<NASAResponse>
}