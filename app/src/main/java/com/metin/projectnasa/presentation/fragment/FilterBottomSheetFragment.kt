package com.metin.projectnasa.presentation.fragment

import android.app.Activity
import android.app.Dialog
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
import com.metin.projectnasa.common.Constants.allCameras
import com.metin.projectnasa.common.getCameraTypesByRover
import com.metin.projectnasa.common.DialogDismissListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var cgFilterOptions: ChipGroup
    private var rover: Int = 0

    fun newInstance(rover: Int): FilterBottomSheetFragment {
        val args = Bundle()
        args.putInt("rover", rover)
        val fragment = FilterBottomSheetFragment()
        fragment.arguments = args
        return fragment
    }

    // ?
    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView: View = View.inflate(context, R.layout.fragment_bottom_sheet, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(resources.getColor(R.color.black))

        rover = requireArguments().getInt("rover", 0)
        createChips(contentView, rover)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        val checked = cgFilterOptions.checkedChipId % allCameras.size

        val selectedCamera = if (checked > 0) {
            allCameras[checked - 1]
        } else if (checked == 0) {
            allCameras[allCameras.size - 1]
        } else {
            null
        }

        val activity: Activity? = activity
        if (activity is DialogDismissListener) (activity as DialogDismissListener).handleDialogClose(
            dialog,
            selectedCamera
        )
    }

    private fun createChips(contentView: View, rover: Int) {
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
                chipBackgroundColor =
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_orange
                        )
                    )
            }
            if (!getCameraTypesByRover(rover).contains(it)) {
                chip.isClickable = false
                chip.chipBackgroundColor =
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_gray
                        )
                    )
            }
            cgFilterOptions.addView(chip)
        }
    }
}