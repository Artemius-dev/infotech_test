package com.example.myapplication.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.myapplication.R
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: Save wheter file was read or not
        val jsonFileString = getJsonDataFromAsset(applicationContext, "city_list.json")
        if (jsonFileString != null) {
            Log.i("jsonFileString data", jsonFileString)
        }
        viewModel.readJson(jsonFileString)
    }
}

fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}