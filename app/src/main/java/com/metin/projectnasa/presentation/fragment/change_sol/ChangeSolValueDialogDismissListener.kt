package com.metin.projectnasa.presentation.fragment.change_sol

import android.content.DialogInterface

interface ChangeSolValueDialogDismissListener {
    fun handleDialogClose(dialog: DialogInterface, solValue: Int)
}