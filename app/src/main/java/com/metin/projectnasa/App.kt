package com.metin.projectnasa

import android.app.Application
import android.content.Context
import com.metin.projectnasa.data.ApiClient
import com.metin.projectnasa.data.model.Rovers
import com.metin.projectnasa.data.repository.NASARepositoryImpl
import com.metin.projectnasa.data.service.NASAService
import dagger.hilt.android.HiltAndroidApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val prefs = getSharedPreferences("RED_PLANET", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val service = ApiClient.getClient().create(NASAService::class.java)
        val repository = NASARepositoryImpl(service)

        repository.getAllRovers().enqueue(object : Callback<Rovers> {
            override fun onResponse(call: Call<Rovers>, response: Response<Rovers>) {
                if (response.isSuccessful) {
                    val rovers = mutableSetOf<String>()
                    val cameras = mutableSetOf<String>()

                    // drop perseverance from the list
                    val roversObj = (response.body() as Rovers).rovers.dropLast(1)
                    roversObj.forEach {
                        val roverCameras = mutableSetOf<String>()

                        // rover specific cameras
                        it.cameras.forEach { rc ->
                            roverCameras.add(rc.name)
                        }

                        // all cameras
                        rovers.add(it.name.lowercase())
                        it.cameras.forEach { c ->
                            cameras.add(c.name)
                        }

                        editor.putStringSet("cameras_${it.name.lowercase()}", roverCameras)
                        editor.putInt("sol_${it.name.lowercase()}", it.max_sol)
                    }

                    // drop camera named ENTRY because it's not in the API table
                    // see table here: https://api.nasa.gov/ -> Mars Rover Photos
                    cameras.remove("ENTRY")
                    editor.putStringSet("all_cameras", cameras)
                    editor.putStringSet("rovers", rovers)
                    editor.apply()
                }
            }

            override fun onFailure(call: Call<Rovers>, t: Throwable) {
                println(t.message.toString())
            }
        })
    }
}