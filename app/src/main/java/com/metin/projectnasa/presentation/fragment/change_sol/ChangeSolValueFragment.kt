package com.metin.projectnasa.presentation.fragment.change_sol

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.metin.projectnasa.R

class ChangeSolValueFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_change_sol, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvInfo: TextView = view.findViewById(R.id.tvInfo)
        val etSolValue: EditText = view.findViewById(R.id.etSolValue)
        val btClose: AppCompatButton = view.findViewById(R.id.btClose)

        btClose.setOnClickListener {
            requireDialog().dismiss()
        }
    }
}