package com.metin.projectnasa.presentation.fragment

import android.app.Activity
import android.content.Context
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
import com.metin.projectnasa.common.DialogDismissListener
import java.util.*

class ChangeSolValuePopupFragment : DialogFragment() {
    private lateinit var etSolValue: EditText
    private var randomGenerated: Int? = null
    private var roverID: Int = 0
    private lateinit var rovers: Set<String>

    fun newInstance(rover: Int): ChangeSolValuePopupFragment {
        val args = Bundle()
        args.putInt("rover", rover)
        val fragment = ChangeSolValuePopupFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = requireActivity().getSharedPreferences("RED_PLANET", Context.MODE_PRIVATE)

        roverID = requireArguments().getInt("rover", 0)
        rovers = prefs.getStringSet("rovers", emptySet()) as Set<String>
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val view: View = inflater.inflate(R.layout.fragment_change_sol, container, false)

        // need this to make pop up corners rounded
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity().getSharedPreferences("RED_PLANET", Context.MODE_PRIVATE)

        etSolValue = view.findViewById(R.id.etSolValue)
        val tvInfo: TextView = view.findViewById(R.id.tvInfo)
        val btClose: AppCompatButton = view.findViewById(R.id.btClose)
        val btRandom: AppCompatButton = view.findViewById(R.id.btRandom)

        val maxSolValue = prefs.getInt("sol_${rovers.elementAt(roverID)}", 0)

        tvInfo.text = String.format("Max sol value for %1$2s is,\n%2$2s ",
            rovers.elementAt(roverID)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
            maxSolValue
        )

        btRandom.setOnClickListener {
            randomGenerated = (0..maxSolValue).random()
            requireDialog().dismiss()
        }

        btClose.setOnClickListener {
            requireDialog().dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        val sol = if (randomGenerated != null) {
            randomGenerated
        } else if (etSolValue.text.isNotEmpty()) {
            etSolValue.text.toString().toInt()
        } else {
            DEFAULT_SOL_VALUE
        }

        val activity: Activity? = activity
        if (activity is DialogDismissListener) (activity as DialogDismissListener).handleDialogClose(
            dialog,
            sol
        )

        randomGenerated = null
    }
}