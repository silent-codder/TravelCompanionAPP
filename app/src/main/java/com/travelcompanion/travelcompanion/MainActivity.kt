package com.travelcompanion.travelcompanion

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.travelcompanion.travelcompanion.adapter.ImageListAdapter
import com.travelcompanion.travelcompanion.adapter.PlaceAdapter
import com.travelcompanion.travelcompanion.viewModel.RoomViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var fabAddPlace: FloatingActionButton
    private lateinit var rvPlaceList: RecyclerView
    private lateinit var tvVersion: TextView
    private lateinit var tvNoDataFound: TextView


    private val placeAdapter by lazy { PlaceAdapter() }
    private lateinit var roomViewModel: RoomViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabAddPlace = findViewById(R.id.fabAddPlace)
        rvPlaceList = findViewById(R.id.rvPlaceList)
        tvVersion = findViewById(R.id.version)
        tvNoDataFound = findViewById(R.id.tvNoDataFound)

        window.statusBarColor = resources.getColor(R.color.black)

        tvVersion.text = "Version : " + BuildConfig.VERSION_NAME

        roomViewModel = RoomViewModel(application)

        fabAddPlace.setOnClickListener {
            val intent = Intent(this, AddPlaceActivity::class.java)
            startActivity(intent)
        }

        rvPlaceList.adapter = placeAdapter
        rvPlaceList.setHasFixedSize(true)
        rvPlaceList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        roomViewModel.allPlaceData.observe(this) {
            if (it.isNotEmpty()) {
                placeAdapter.setPlaceListData(it)
                tvNoDataFound.visibility = android.view.View.GONE
            } else {
                tvNoDataFound.visibility = android.view.View.VISIBLE
            }

        }

    }

    //exit permission when back button is pressed
    override fun onBackPressed() {
        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(this)
        alertDialog.setTitle("Exit")
        alertDialog.setMessage("Are you sure you want to exit?")
        alertDialog.setPositiveButton("Yes") { _, _ ->
            finish()
        }
        alertDialog.setNegativeButton("No") { _, _ ->
            alertDialog.setCancelable(true)
        }
        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
        roomViewModel.allPlaceData.observe(this) {
            if (it.isNotEmpty()) {
                placeAdapter.setPlaceListData(it)
                tvNoDataFound.visibility = android.view.View.GONE
                rvPlaceList.visibility = android.view.View.VISIBLE
            } else {
                rvPlaceList.visibility = android.view.View.GONE
                tvNoDataFound.visibility = android.view.View.VISIBLE
            }
        }
    }

    override fun onStart() {
        super.onStart()
        roomViewModel.allPlaceData.observe(this) {
            if (it.isNotEmpty()) {
                placeAdapter.setPlaceListData(it)
                tvNoDataFound.visibility = android.view.View.GONE
                rvPlaceList.visibility = android.view.View.VISIBLE
            } else {
                rvPlaceList.visibility = android.view.View.GONE
                tvNoDataFound.visibility = android.view.View.VISIBLE
            }
        }
    }

}