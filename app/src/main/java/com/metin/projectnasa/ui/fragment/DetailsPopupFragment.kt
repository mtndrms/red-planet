package com.metin.projectnasa.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.metin.projectnasa.R
import com.metin.projectnasa.data.model.Photo
import com.squareup.picasso.Picasso

class DetailsPopupFragment : androidx.fragment.app.DialogFragment() {
    fun newInstance(photo: Photo): DetailsPopupFragment {
        val args = Bundle()
        args.putSerializable("photo", photo)
        val fragment = DetailsPopupFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_dialog, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // getSerializable(key, defaultValue)
        // Deprecated! Fix this later!
        val photo = requireArguments().getSerializable("photo") as Photo

        val tvRoverName: TextView = view.findViewById(R.id.tvRoverName)
        val tvDatePhotoTaken: TextView = view.findViewById(R.id.tvDatePhotoTaken)
        val tvCameraType: TextView = view.findViewById(R.id.tvCameraType)
        val tvRoverStatus: TextView = view.findViewById(R.id.tvRoverStatus)
        val tvLaunchDate: TextView = view.findViewById(R.id.tvLaunchDate)
        val tvLandingDate: TextView = view.findViewById(R.id.tvLandingDate)
        val ivPhoto: ImageView = view.findViewById(R.id.ivPhoto)
        val btClose: AppCompatButton = view.findViewById(R.id.btClose)

        photo.run {
            tvRoverName.text = rover.name
            tvLaunchDate.text = rover.launch_date
            tvLandingDate.text = rover.landing_date
            tvDatePhotoTaken.text = earth_date
            tvRoverStatus.text = rover.status
            tvCameraType.text = "${camera.name}: ${camera.full_name}"
            Picasso.get()
                .load(img_src)
                .resize(350, 350)
                .centerCrop()
                .into(ivPhoto)
        }

        btClose.setOnClickListener {
            requireDialog().dismiss()
        }
    }
}