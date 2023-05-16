package com.travelcompanion.travelcompanion.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.travelcompanion.travelcompanion.room.DatabaseDao
import com.travelcompanion.travelcompanion.room.database.PlaceDatabase
import com.travelcompanion.travelcompanion.room.roomModel.PlaceData
import com.travelcompanion.travelcompanion.utils.subscribeOnBackground

class RoomRepository(application: Application) {
    private var placeDao: DatabaseDao
    private var allPlaces: LiveData<List<PlaceData>>
    private var database: PlaceDatabase = PlaceDatabase.getInstance(application)

    init {
        placeDao = database.placeDao()
        allPlaces = placeDao.getAllPlaceData()
    }

    fun insertPlace(placeData: PlaceData) {
        subscribeOnBackground {
            placeDao.insertPlaceData(placeData)
        }
    }

    fun getAllPlaceData(): LiveData<List<PlaceData>> {
        return allPlaces
    }

    fun getPlaceById(placeId: String): LiveData<PlaceData> {
        return placeDao.getPlaceData(placeId)
    }

    fun deletePlaceData(placeId: String) {
        subscribeOnBackground {
            placeDao.deletePlaceData(placeId)
        }
    }

    fun updatePlaceData(placeId: String, placeName: String, description: String, date: String, category: String, gpsLocation: String, notes: String, rating: String) {
        subscribeOnBackground {
            placeDao.updatePlaceData(placeId, placeName, description, date, category, gpsLocation, notes, rating)
        }
    }

    fun updateImageList(placeId: String, imageList: String) {
        subscribeOnBackground {
            placeDao.updateImageList(placeId, imageList)
        }
    }
}