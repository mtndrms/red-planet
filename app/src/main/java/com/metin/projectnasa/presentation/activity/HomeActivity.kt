package com.metin.projectnasa.presentation.activity

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.metin.projectnasa.common.Constants.DEFAULT_ACTIVE_TAB
import com.metin.projectnasa.common.Constants.DEFAULT_PAGE
import com.metin.projectnasa.common.Constants.DEFAULT_SOL_VALUE
import com.metin.projectnasa.domain.adapter.PhotosRecyclerViewAdapter
import com.metin.projectnasa.presentation.fragment.FilterBottomSheetFragment
import com.metin.projectnasa.common.InfiniteRecyclerViewScrollListener
import com.metin.projectnasa.common.Resource
import com.metin.projectnasa.data.model.Photo
import com.metin.projectnasa.databinding.ActivityHomeBinding
import com.metin.projectnasa.common.DialogDismissListener
import com.metin.projectnasa.presentation.fragment.ChangeSolValuePopupFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), DialogDismissListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var photosRecyclerViewAdapter: PhotosRecyclerViewAdapter
    private lateinit var scrollListener: InfiniteRecyclerViewScrollListener
    private var isFiltered = false
    private var activeTab = DEFAULT_ACTIVE_TAB
    private var camera: String? = null
    private var sol = DEFAULT_SOL_VALUE
    private var page = DEFAULT_PAGE
    private lateinit var rovers: Set<String>

    private val viewModel: HomeActivityViewModel by lazy {
        ViewModelProvider(this)[HomeActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gridLayoutManager = GridLayoutManager(this, 3)
        val prefs = getSharedPreferences("RED_PLANET", Context.MODE_PRIVATE)

        rovers = prefs.getStringSet("rovers", emptySet()) as Set<String>

        if (savedInstanceState != null) {
            camera = savedInstanceState.getString("camera", null)
            sol = savedInstanceState.getInt("sol", DEFAULT_SOL_VALUE)
            isFiltered = savedInstanceState.getBoolean("isFiltered", false)
            activeTab = savedInstanceState.getInt("activeTab", DEFAULT_ACTIVE_TAB)
            page = savedInstanceState.getInt("page", DEFAULT_PAGE)
        }

        binding.tbBottomTabBar.itemActiveIndex = activeTab
        binding.tbBottomTabBar.setOnItemSelectedListener {
            activeTab = it
            resetCollectionView()
            resetFilters()
            binding.btClearFilter.visibility = View.INVISIBLE
            viewModel.loadData(
                roverName = rovers.elementAt(activeTab),
                page = DEFAULT_PAGE,
                sol = sol
            )
        }

        binding.btFilter.setOnClickListener {
            val bottomSheet: FilterBottomSheetFragment =
                FilterBottomSheetFragment().newInstance(activeTab)
            bottomSheet.show(supportFragmentManager, "FILTER_BOTTOM_SHEET_FRAGMENT")
        }

        binding.btClearFilter.setOnClickListener {
            resetCollectionView()
            resetFilters()
            binding.btClearFilter.visibility = View.INVISIBLE
            viewModel.loadData(
                roverName = rovers.elementAt(activeTab),
                page = DEFAULT_PAGE,
                sol = sol
            )
        }

        binding.btChangeSolValue.setOnClickListener {
            val bottomSheet: ChangeSolValuePopupFragment =
                ChangeSolValuePopupFragment().newInstance(activeTab)
            bottomSheet.show(supportFragmentManager, "CHANGE_SOL_VALUE_POP_UP_FRAGMENT")
        }

        photosRecyclerViewAdapter = PhotosRecyclerViewAdapter(this@HomeActivity, mutableListOf())
        binding.rvPhotos.layoutManager = gridLayoutManager
        binding.rvPhotos.adapter = photosRecyclerViewAdapter

        viewModel.photos.observe(this@HomeActivity) {
            processScreenState(it)
        }

        // infinite scrolling
        scrollListener = object : InfiniteRecyclerViewScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                // if any camera filter is chosen we need to add camera to our api request as a query param
                // eger herhangi bir kamera filtresi aktifse, api istegi yapilan url'e camera query parametresi ekle
                // boylece filter() calistiktan sonra yuklenen fotograflar filtre kapatilana kadar secilen kameradan gelir
                viewModel.loadData(
                    roverName = rovers.elementAt(activeTab),
                    sol = sol,
                    page = this@HomeActivity.page + page - 1,
                    camera = camera
                )
            }
        }
        binding.rvPhotos.addOnScrollListener(scrollListener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("camera", camera)
        outState.putInt("sol", sol)
        outState.putInt("activeTab", activeTab)
        outState.putBoolean("isFiltered", isFiltered)
        outState.putInt("page", this@HomeActivity.page + scrollListener.getCurrentPageIndex() - 1)
        super.onSaveInstanceState(outState)
    }

    // reset CollectionView before fetching other rover's pictures
    // call when changing tab or clearing the filters
    // Diger rover fotograflarini indrimeye baslamadan once CollectionView'i sifirla
    // yeni bir tab'e gecmeden once ya da filtreleri temizledikden hemen sonra cagirilir
    private fun resetCollectionView() {
        photosRecyclerViewAdapter.resetDataSet()
        viewModel.resetData()
        scrollListener.resetState()
    }

    private fun resetFilters() {
        isFiltered = false
        sol = DEFAULT_SOL_VALUE
        camera = null
        page = DEFAULT_PAGE
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

    // Verinin yuklenmesi bittiginde cagirilir.
    private fun showRecyclerView() {
        binding.shimmerLayout.apply {
            stopShimmer()
            visibility = View.GONE
        }
        binding.rvPhotos.visibility = View.VISIBLE
    }

    // runs as soon as dialog's are closed
    // sol degeri degistirme ve kamera tipi filtresi fragment dialog'lari kapandiginda calisir
    // TODO: not the best solution fix this
    override fun <T> handleDialogClose(dialog: DialogInterface, value: T) {
        if (value is String) {
            if (value.isNotEmpty()) {
                camera = value.lowercase()
                isFiltered = true
                binding.btClearFilter.visibility = View.VISIBLE
                viewModel.filter(value)
            } else {
                camera = null
            }
        }

        if (value is Int) {
            val old = sol
            sol = value
            if (value.toInt() != old) {
                photosRecyclerViewAdapter.resetDataSet()
                viewModel.loadData(
                    roverName = rovers.elementAt(activeTab),
                    page = DEFAULT_PAGE,
                    sol = sol,
                    camera = camera
                )
                this.page = DEFAULT_PAGE
                binding.btClearFilter.visibility = View.VISIBLE
            }
        }
    }
}