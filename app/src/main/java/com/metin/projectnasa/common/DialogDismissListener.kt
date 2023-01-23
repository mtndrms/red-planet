package com.metin.projectnasa.common

import android.content.DialogInterface

interface DialogDismissListener {
    fun <T> handleDialogClose(dialog: DialogInterface, value: T)
}