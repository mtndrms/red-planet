package com.metin.projectnasa.presentation.activity

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.metin.projectnasa.domain.adapter.PhotosRecyclerViewAdapter
import com.metin.projectnasa.presentation.fragment.filter.FilterBottomSheetFragment
import com.metin.projectnasa.common.EndlessRecyclerViewScrollListener
import com.metin.projectnasa.common.Resource
import com.metin.projectnasa.data.model.Photo
import com.metin.projectnasa.databinding.ActivityHomeBinding
import com.metin.projectnasa.presentation.fragment.filter.FilterDialogDismissListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), FilterDialogDismissListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var photosRecyclerViewAdapter: PhotosRecyclerViewAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var isFiltered = false
    private var activeTab = DEFAULT_ACTIVE_TAB
    private var camera: String? = null

    private val viewModel: HomeActivityViewModel by lazy {
        ViewModelProvider(this)[HomeActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gridLayoutManager = GridLayoutManager(this, 3)

        binding.tbBottomTabBar.setOnItemSelectedListener {
            activeTab = it
            isFiltered = false
            binding.btClearFilter.visibility = View.INVISIBLE
            resetCollectionView()
            when (it) {
                0 -> viewModel.loadData(activeTab = activeTab, page = DEFAULT_PAGE)
                1 -> viewModel.loadData(activeTab = activeTab, page = DEFAULT_PAGE)
                2 -> viewModel.loadData(activeTab = activeTab, page = DEFAULT_PAGE)
            }
        }

        binding.btFilter.setOnClickListener {
            val bottomSheet: FilterBottomSheetFragment =
                FilterBottomSheetFragment().newInstance(activeTab)
            bottomSheet.show(supportFragmentManager, "FILTER_BOTTOM_SHEET_FRAGMENT")
        }

        binding.btClearFilter.setOnClickListener {
            isFiltered = false
            resetCollectionView()
            binding.btClearFilter.visibility = View.INVISIBLE
            viewModel.loadData(activeTab, DEFAULT_PAGE)
        }

        photosRecyclerViewAdapter = PhotosRecyclerViewAdapter(this@HomeActivity, mutableListOf())
        binding.rvPhotos.layoutManager = gridLayoutManager
        binding.rvPhotos.adapter = photosRecyclerViewAdapter

        viewModel.photos.observe(this@HomeActivity) {
            processScreenState(it)
        }

        // infinite scrolling
        scrollListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                // if any camera filter is chosen we need to add camera to our api request as a query param
                // eger herhangi bir kamera filtresi aktifse, api istegi yapilan url'e camera query parametresi ekle
                // boylece filter() calistiktan sonra yuklenen fotograflar filtre kapatilana kadar secilen kameradan gelir
                viewModel.loadData(activeTab, page = page + 1, camera = camera)
            }
        }
        binding.rvPhotos.addOnScrollListener(scrollListener)
    }

    // reset CollectionView before fetching other rover's pictures
    // Diger rover fotograflarini indrimeye baslamadan once CollectionView'i sifirla
    private fun resetCollectionView() {
        photosRecyclerViewAdapter.resetDataSet()
        viewModel.resetData()
        scrollListener.resetState()
        camera = null
    }

    private fun processScreenState(state: Resource<List<Photo>>) {
        when (state) {
            is Resource.Success -> {
                if (state.data != null) {
                    showRecyclerView()
                    if (!isFiltered) {
                        photosRecyclerViewAdapter.updateDataSet(state.data)
                    } else {
                        isFiltered = false
                        photosRecyclerViewAdapter.pushFilteredList(state.data)
                    }
                }
            }
            is Resource.Loading -> {
                binding.shimmerLayout.startShimmer()
            }
            is Resource.Error -> {
                Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showRecyclerView() {
        binding.shimmerLayout.apply {
            stopShimmer()
            visibility = View.GONE
        }
        binding.rvPhotos.visibility = View.VISIBLE
    }

    // runs as soon as filter dialog closed
    override fun handleDialogClose(dialog: DialogInterface, selectedCamera: String?) {
        if (selectedCamera != null) {
            camera = selectedCamera.lowercase()
            isFiltered = true
            binding.btClearFilter.visibility = View.VISIBLE
            viewModel.filter(selectedCamera)
        } else {
            camera = null
        }
    }

    companion object {
        const val DEFAULT_ACTIVE_TAB = 0
        const val DEFAULT_PAGE = 1
    }
}