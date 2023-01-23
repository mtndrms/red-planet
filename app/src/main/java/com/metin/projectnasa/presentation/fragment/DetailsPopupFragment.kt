package com.metin.projectnasa.presentation.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.metin.projectnasa.R
import com.metin.projectnasa.data.model.Photo
import com.squareup.picasso.Picasso
import java.util.*


class DetailsPopupFragment : DialogFragment() {
    fun newInstance(photo: Photo): DetailsPopupFragment {
        val args = Bundle()
        args.putSerializable("photo", photo)
        val fragment = DetailsPopupFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val view: View = inflater.inflate(R.layout.fragment_detail, container, false)

        // need this to make popup rounded
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // getSerializable(key, defaultValue)
        // Deprecated! Fix this later!
        val photoDto = requireArguments().getSerializable("photo") as Photo

        val tvRoverName: TextView = view.findViewById(R.id.tvRoverName)
        val tvDatePhotoTaken: TextView = view.findViewById(R.id.tvDatePhotoTaken)
        val tvCameraType: TextView = view.findViewById(R.id.tvCameraType)
        val tvRoverStatus: TextView = view.findViewById(R.id.tvRoverStatus)
        val tvLaunchDate: TextView = view.findViewById(R.id.tvLaunchDate)
        val tvLandingDate: TextView = view.findViewById(R.id.tvLandingDate)
        val ivPhoto: ImageView = view.findViewById(R.id.ivPhoto)
        val btClose: AppCompatButton = view.findViewById(R.id.btClose)

        photoDto.run {
            tvRoverName.text = rover.name
            tvLaunchDate.text = rover.launch_date
            tvLandingDate.text = rover.landing_date
            tvDatePhotoTaken.text = earth_date
            tvRoverStatus.text = rover.status.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
            tvCameraType.text = camera.name
            Picasso.get().load(img_src).resize(350, 350).centerCrop().into(ivPhoto)
        }

        btClose.setOnClickListener {
            requireDialog().dismiss()
        }
    }
}