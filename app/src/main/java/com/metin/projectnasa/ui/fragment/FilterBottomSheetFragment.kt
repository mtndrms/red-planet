package com.metin.projectnasa.ui.fragment

import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.metin.projectnasa.R
import com.metin.projectnasa.utils.getCameraTypesByRover

class FilterBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var cgFilterOptions: ChipGroup

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

        createChips(contentView, requireArguments().getInt("rover", 0))
    }

    private fun createChips(contentView: View, rover: Int) {
        cgFilterOptions = contentView.findViewById(R.id.cgFilterOptions)
        getCameraTypesByRover(rover).forEach {
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
            cgFilterOptions.addView(chip)
        }
    }
}