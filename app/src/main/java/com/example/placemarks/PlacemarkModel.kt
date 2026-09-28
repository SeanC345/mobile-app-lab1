package com.example.placemarks

data class PlacemarkModel(
    var id: Long = 0L,
    var title: String = "",
    var desc: String = "",
    var x: Double = 0.0,
    var y: Double = 0.0
)
