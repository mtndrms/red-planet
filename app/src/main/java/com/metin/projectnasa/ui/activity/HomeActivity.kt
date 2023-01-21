package com.metin.projectnasa.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.metin.projectnasa.data.model.Photo
import com.metin.projectnasa.databinding.ActivityHomeBinding
import com.metin.projectnasa.domain.adapter.PhotosRecyclerViewAdapter
import com.metin.projectnasa.ui.fragment.FilterBottomSheetFragment
import com.metin.projectnasa.utils.EndlessRecyclerViewScrollListener
import com.metin.projectnasa.utils.filterByCamera
import com.metin.projectnasa.utils.getCameraTypesByRover
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var photosRecyclerViewAdapter: PhotosRecyclerViewAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var isFiltered = false
    private lateinit var photos: MutableList<Photo>

    private val viewModel: HomeActivityViewModel by lazy {
        ViewModelProvider(this)[HomeActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var activeTab = DEFAULT_ACTIVE_TAB
        val gridLayoutManager = GridLayoutManager(this, 3)

        binding.tbBottomTabBar.setOnItemSelectedListener {
            activeTab = it
            resetCollectionView(viewModel)
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

            filter()
        }

        photosRecyclerViewAdapter = PhotosRecyclerViewAdapter(this@HomeActivity, mutableListOf())
        binding.rvPhotos.layoutManager = gridLayoutManager
        binding.rvPhotos.adapter = photosRecyclerViewAdapter

        viewModel.photos.observe(this@HomeActivity) {
            viewModel.photos.value?.data?.let { it1 ->
                photos = viewModel.photos.value!!.data!!
                photosRecyclerViewAdapter.updateDataSet(
                    it1
                )
            }
        }

        // infinite scrolling
        scrollListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                // TODO: page = (filtered / 25).toInt() -> correct page number to continue fetching
                // if any camera filter is chosen we need to add camera to our api request as a query param
                // eger herhangi bir kamera filtresi aktifse, api istegi yapilan url'e camera query parametresi ekle
                // boylece filter() calistiktan sonra yuklenen fotograflar filtre kapatilana kadar secilen kameradan gelsin
                if (isFiltered) {
                    viewModel.loadData(
                        activeTab,
                        page = page + 1,
                        camera = getCameraTypesByRover(activeTab)[2] // camera param
                    )
                } else {
                    viewModel.loadData(activeTab, page + 1)
                }
            }
        }
        binding.rvPhotos.addOnScrollListener(scrollListener)
    }

    // filter already fetched photos
    // halihazirda indirilmis fotograflari filtrele
    private fun filter() {
        val filtered = photos.filterByCamera(2, 0)

        isFiltered = true
        photosRecyclerViewAdapter.resetDataSet()
        viewModel.resetData()
        scrollListener.resetState()
        viewModel.pushFilteredData(filtered)
    }

    // reset CollectionView before fetching other rover's pictures
    // Diger rover fotograflarini indrimeye baslamadan once CollectionView'i sifirla
    private fun resetCollectionView(viewModel: HomeActivityViewModel) {
        photosRecyclerViewAdapter.resetDataSet()
        viewModel.resetData()
        scrollListener.resetState()
    }

    companion object {
        const val DEFAULT_ACTIVE_TAB = 0
        const val DEFAULT_PAGE = 1
    }
}