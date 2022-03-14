package com.example.locationnews

import android.app.Dialog
import android.content.Context
import android.os.Bundle

class LoadingDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_screen)
        this.window?.setLayout(
            800,
            800
        )
    }
}