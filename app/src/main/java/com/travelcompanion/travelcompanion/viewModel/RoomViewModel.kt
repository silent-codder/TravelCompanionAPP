package com.travelcompanion.travelcompanion.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.travelcompanion.travelcompanion.repository.RoomRepository
import com.travelcompanion.travelcompanion.room.roomModel.PlaceData

class RoomViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = RoomRepository(app)
    val allPlaceData = repository.getAllPlaceData()
    var getPlaceByIdData: LiveData<PlaceData> = MutableLiveData()

    fun insertPlace(placeData: PlaceData) {
        repository.insertPlace(placeData)
    }

    fun getPlaceById(placeId: String) {
        getPlaceByIdData = repository.getPlaceById(placeId)
    }

    fun deletePlaceData(placeId: String) {
        repository.deletePlaceData(placeId)
    }

    fun updatePlaceData(placeId: String, placeName: String, description: String, date: String, category: String, gpsLocation: String, notes: String, rating: String) {
        repository.updatePlaceData(placeId, placeName, description, date, category, gpsLocation, notes, rating)
    }

    fun updateImageList(placeId: String, imageList: String) {
        repository.updateImageList(placeId, imageList)
    }

}