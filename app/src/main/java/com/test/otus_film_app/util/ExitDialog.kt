package com.test.otus_film_app.util

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.test.otus_film_app.R

class ExitDialog: DialogFragment() {
    companion object {
        const val EXIT_DIALOG_TAG = "exit"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.dialog_exit_fragment)
                .setPositiveButton(R.string.dialog_exit_yes) { dialog, id ->
                    activity?.finish()
                }
                .setNegativeButton(R.string.dialog_exit_no) { dialog, id ->
                    dialog.cancel()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}