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
import com.metin.projectnasa.ui.fragment.DetailsPopupFragment
import com.squareup.picasso.Picasso

class PhotosRecyclerViewAdapter(
    private val context: Context,
    private val photos: List<Photo>
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

    override fun getItemCount(): Int = photos.size
}
