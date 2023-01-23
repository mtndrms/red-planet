package com.metin.projectnasa.domain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.metin.projectnasa.R
import com.metin.projectnasa.data.model.Photo
import com.metin.projectnasa.presentation.fragment.DetailsPopupFragment
import com.squareup.picasso.Picasso
import okhttp3.internal.notify
import okhttp3.internal.notifyAll

class PhotosRecyclerViewAdapter(
    private val context: Context,
    private var photos: MutableList<Photo>
) :
    RecyclerView.Adapter<PhotosRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView

        init {
            image = view.findViewById(R.id.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_photo_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get()
            .load(photos[position].img_src)
            .resize(125, 125)
            .centerCrop()
            .into(holder.image)

        holder.itemView.setOnClickListener {
            val detailsPopupFragment = DetailsPopupFragment().newInstance(photos[position])
            detailsPopupFragment.show(
                (context as AppCompatActivity).supportFragmentManager,
                "Details Fragment Dialog"
            )
        }
    }

    // normally use this before filtering
    fun resetDataSet() {
        photos.clear()
        notifyDataSetChanged()
    }

    // show filtered photos
    fun pushFilteredList(list: List<Photo>) {
        photos.clear()
        photos.addAll(list)
        notifyDataSetChanged()
    }

    // if data set change update it, this also changes the photo displayed in the ui
    fun updateDataSet(list: List<Photo>) {
        val before = photos.size
        photos = list as MutableList<Photo>
        notifyItemRangeInserted(
            photos.size - (photos.size - before),
            photos.size
        ) //or we can implement a DiffUtil.Callback
    }

    override fun getItemCount(): Int = photos.size
}
