package com.metin.projectnasa.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.metin.projectnasa.data.ApiClient
import com.metin.projectnasa.data.model.NASAResponse
import com.metin.projectnasa.data.model.Photo
import com.metin.projectnasa.data.service.NASAService
import com.metin.projectnasa.databinding.ActivityHomeBinding
import com.metin.projectnasa.utils.EndlessRecyclerViewScrollListener
import com.metin.projectnasa.domain.adapter.PhotosRecyclerViewAdapter
import com.metin.projectnasa.domain.filterByCamera
import com.metin.projectnasa.ui.fragment.FilterBottomSheetFragment
import com.metin.projectnasa.utils.Constants.rovers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var photosRecyclerViewAdapter: PhotosRecyclerViewAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var nasaService: NASAService
    private var photos: MutableList<Photo> = mutableListOf()
    private var isFiltered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiClient = ApiClient.getClient()
        nasaService = apiClient.create(NASAService::class.java)
        val gridLayoutManager = GridLayoutManager(this, 3)

        var activeTab = 0
        binding.tbBottomTabBar.run {
            setOnItemSelectedListener {
                activeTab = it
                resetCollectionView()
                when (it) {
                    0 -> loadData(activeTab = activeTab, page = 1)
                    1 -> loadData(activeTab = activeTab, page = 1)
                    2 -> loadData(activeTab = activeTab, page = 1)
                }
            }

            setOnItemReselectedListener {
                // Do nothing
            }
        }

        binding.btFilter.setOnClickListener {
            val bottomSheet: FilterBottomSheetFragment =
                FilterBottomSheetFragment().newInstance(activeTab)
            bottomSheet.show(supportFragmentManager, "FILTER_BOTTOM_SHEET_FRAGMENT")

            filter()
        }

        loadData(activeTab, 1)

        photosRecyclerViewAdapter = PhotosRecyclerViewAdapter(this@HomeActivity, photos)
        binding.rvPhotos.layoutManager = gridLayoutManager
        binding.rvPhotos.adapter = photosRecyclerViewAdapter

        scrollListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                if (isFiltered) {
                    loadData(activeTab, camera = "MAST".lowercase(), page = page + 1)
                } else {
                    loadData(activeTab, page + 1)
                }
            }
        }
        binding.rvPhotos.addOnScrollListener(scrollListener)
    }

    private fun loadData(activeTab: Int, page: Int, camera: String? = null) {
        requestData(camera, activeTab, page).enqueue(object : Callback<NASAResponse> {
            override fun onResponse(call: Call<NASAResponse>, response: Response<NASAResponse>) {
                println(call.request().url)
                if (response.isSuccessful) {
                    photos.addAll((response.body() as NASAResponse).photos)
                    showData(
                        photos.size - (response.body() as NASAResponse).photos.size, photos.size
                    )
                }
            }

            override fun onFailure(call: Call<NASAResponse>, t: Throwable) {
                println(t.message.toString())
            }
        })
    }

    private fun requestData(camera: String? = null, activeTab: Int, page: Int): Call<NASAResponse> {
        return nasaService.getPhotosByRover(
            roverName = rovers[activeTab],
            1000,
            camera = camera,
            page = page
        )
    }

    private fun showData(sizeBefore: Int, sizeAfter: Int) {
        photosRecyclerViewAdapter.notifyItemRangeInserted(sizeBefore, sizeAfter)
    }

    private fun filter() {
        isFiltered = !isFiltered
        val filtered = photos.filterByCamera(listOf(0), 0)
        // 1. First, clear the array of data
        photos.clear();
        photos.addAll(filtered)
        // 2. Notify the adapter of the update
        photosRecyclerViewAdapter.notifyDataSetChanged()
        // 3. Reset endless scroll listener when performing a new search
        scrollListener.resetState();
    }

    private fun resetCollectionView() {
        photos.clear();
        photosRecyclerViewAdapter.notifyDataSetChanged()
        scrollListener.resetState();
    }
}