package com.metin.projectnasa.presentation.fragment

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.metin.projectnasa.R
import com.metin.projectnasa.common.Constants.DEFAULT_SOL_VALUE
import com.metin.projectnasa.common.Constants.rovers
import com.metin.projectnasa.common.DialogDismissListener
import com.metin.projectnasa.common.getMaxSolValueByRover

class ChangeSolValuePopupFragment : DialogFragment() {
    private lateinit var etSolValue: EditText
    fun newInstance(rover: Int): ChangeSolValuePopupFragment {
        val args = Bundle()
        args.putInt("rover", rover)
        val fragment = ChangeSolValuePopupFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val view: View = inflater.inflate(R.layout.fragment_change_sol, container, false)

        // need this to make popup rounded
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rover = requireArguments().getInt("rover", 0)

        val tvInfo: TextView = view.findViewById(R.id.tvInfo)
        etSolValue = view.findViewById(R.id.etSolValue)
        val btClose: AppCompatButton = view.findViewById(R.id.btClose)

        tvInfo.text = "Max sol value for ${rovers[rover]} is:\n${getMaxSolValueByRover(rover)}"

        btClose.setOnClickListener {
            requireDialog().dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        val sol = if (etSolValue.text.isNotEmpty()) {
            etSolValue.text.toString().toInt()
        } else {
            DEFAULT_SOL_VALUE
        }

        val activity: Activity? = activity
        if (activity is DialogDismissListener) (activity as DialogDismissListener).handleDialogClose(
            dialog,
            sol
        )
    }
}