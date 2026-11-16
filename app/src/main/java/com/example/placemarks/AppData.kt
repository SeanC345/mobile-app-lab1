package com.example.placemarks

import android.content.Context
import java.io.File

object AppData {

    lateinit var placedMarks: PlacemarkMemStore
        private set

    fun initialize(context: Context) {
        val file = File(
            context.filesDir,
            "place_list.json"
        )

        placedMarks = PlacemarkMemStore(file)
    }
}