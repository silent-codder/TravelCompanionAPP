package com.travelcompanion.travelcompanion.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.travelcompanion.travelcompanion.room.roomModel.PlaceData

@Dao
interface DatabaseDao {
    @Insert
    fun insertPlaceData(place: PlaceData)

    @Query("select * from place_data")
    fun getAllPlaceData(): LiveData<List<PlaceData>>

    @Query("select * from place_data where id = :placeId")
    fun getPlaceData(placeId: String): LiveData<PlaceData>

    @Query("delete from place_data where id = :placeId")
    fun deletePlaceData(placeId: String)

    @Query("update place_data set placeName = :placeName, description = :description, date = :date, category = :category, gpsLocation = :gpsLocation, notes = :notes, rating = :rating where id = :placeId")
    fun updatePlaceData(placeId: String, placeName: String, description: String, date: String, category: String, gpsLocation: String, notes: String, rating: String)

    @Query("update place_data set imageList = :imageList where id = :placeId")
    fun updateImageList(placeId: String, imageList: String)
}