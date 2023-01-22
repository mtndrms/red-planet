package com.metin.projectnasa.presentation.fragment.filter

import android.content.DialogInterface

interface FilterDialogDismissListener {
    fun handleDialogClose(dialog: DialogInterface, selectedCamera: String?)
}