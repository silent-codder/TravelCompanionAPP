package com.travelcompanion.travelcompanion.room.roomModel

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "place_data")
data class PlaceData(
    val placeName: String,
    val description: String,
    val date: String,
    val category: String,
    val gpsLocation: String,
    val notes: String,
    val rating: String,
//    val imageList: String,
    val imageList: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
){
    constructor(placeName: String, description: String, date: String, category: String, gpsLocation: String, notes: String, rating: String, imageList: String) : this(placeName, description, date, category, gpsLocation, notes, rating,imageList, 0)
}
