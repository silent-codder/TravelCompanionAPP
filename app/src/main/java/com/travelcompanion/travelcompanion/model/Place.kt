package com.travelcompanion.travelcompanion.model

data class Place(
    val placeName: String,
    val description: String,
    val date: String,
    val category: String,
    val gpsLocation: String,
    val notes: String,
    val rating: String,
    val imageList : List<String>

)
