package com.metin.projectnasa.ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.metin.projectnasa.R
import com.metin.projectnasa.utils.Constants


class FilterBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var cgFilterOptions: ChipGroup

    fun newInstance(rover: Int): FilterBottomSheetFragment {
        val args = Bundle()
        args.putInt("rover", rover)
        val fragment = FilterBottomSheetFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView: View = View.inflate(context, R.layout.fragment_bottom_sheet, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(resources.getColor(android.R.color.transparent))

        createChips(contentView, requireArguments().getInt("rover", 0))
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        println(cgFilterOptions.checkedChipIds)
    }

    private fun createChips(contentView: View, rover: Int) {
        val cameras: List<String> = when (rover) {
            0 -> Constants.curiosityCameraTypes
            1 -> Constants.opportunityCameraTypes
            2 -> Constants.spiritCameraTypes
            else -> {
                listOf()
            }
        }

        cgFilterOptions = contentView.findViewById(R.id.cgFilterOptions)
        cameras.forEach {
            val chip = Chip(requireContext())
            chip.run {
                text = it
                isCheckedIconVisible = true
                isCheckable = true
                checkedIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_done)
                chipBackgroundColor =
                    ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray))
            }
            cgFilterOptions.addView(chip)
        }
    }
}