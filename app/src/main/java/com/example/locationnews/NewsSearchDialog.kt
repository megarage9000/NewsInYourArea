package com.example.locationnews

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type


interface SearchInterface {
    fun search(params: HashMap<String, String>)
}

class NewsSearchDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_search_dialog)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        this.window?.setLayout(
            800,
            800
        )
    }


}

