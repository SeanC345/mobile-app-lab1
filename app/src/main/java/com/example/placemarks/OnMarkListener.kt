package com.example.placemarks

interface OnMarkListener {

    fun onMarkClick(mark: PlacemarkModel)

    fun onMarkLongClick(mark: PlacemarkModel): Boolean
}