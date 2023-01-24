package com.metin.projectnasa.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.metin.projectnasa.R
import com.metin.projectnasa.common.DialogDismissListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var cgFilterOptions: ChipGroup
    private lateinit var rovers: Set<String>
    private lateinit var allCameras: Set<String>
    private var roverID: Int = 0

    fun newInstance(rover: Int): FilterBottomSheetFragment {
        val args = Bundle()
        args.putInt("rover", rover)
        val fragment = FilterBottomSheetFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = requireActivity().getSharedPreferences("RED_PLANET", Context.MODE_PRIVATE)

        roverID = requireArguments().getInt("rover", 0)
        rovers = prefs.getStringSet("rovers", emptySet()) as Set<String>
        allCameras = prefs.getStringSet("all_cameras", emptySet()) as Set<String>
    }

    // ?
    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView: View = View.inflate(context, R.layout.fragment_bottom_sheet, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(resources.getColor(R.color.black))

        createChips(contentView, roverID)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        val checked = cgFilterOptions.checkedChipId % allCameras.size
        val selectedCamera = if (checked > 0) {
            allCameras.toList()[checked - 1]
        } else if (checked == 0) {
            allCameras.toList()[allCameras.size - 1]
        } else {
            null
        }

        val activity: Activity? = activity
        if (activity is DialogDismissListener) (activity as DialogDismissListener).handleDialogClose(
            dialog, selectedCamera
        )
    }

    private fun createChips(contentView: View, roverID: Int) {
        val prefs = requireActivity().getSharedPreferences("RED_PLANET", Context.MODE_PRIVATE)
        val roverCameras = prefs.getStringSet("cameras_${rovers.elementAt(roverID)}", emptySet())

        cgFilterOptions = contentView.findViewById(R.id.cgFilterOptions)
        allCameras.forEach {
            val chip = Chip(requireContext())
            chip.run {
                text = it
                textSize = 16F
                isCheckedIconVisible = true
                isCheckable = true
                setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
                typeface = ResourcesCompat.getFont(requireContext(), R.font.jost_regular)
                checkedIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_done)
                chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(), R.color.dark_orange
                    )
                )
            }
            if (!roverCameras!!.contains(it)) {
                chip.isClickable = false
                chip.chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(), R.color.dark_gray
                    )
                )
            }
            cgFilterOptions.addView(chip)
        }
    }
}