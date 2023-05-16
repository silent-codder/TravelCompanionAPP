package com.travelcompanion.travelcompanion

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.travelcompanion.travelcompanion.adapter.ImageAdapter
import com.travelcompanion.travelcompanion.adapter.ImageListAdapter
import com.travelcompanion.travelcompanion.utils.ImageBitmapString
import com.travelcompanion.travelcompanion.viewModel.RoomViewModel

class ViewPlaceActivity : AppCompatActivity() {

    private lateinit var placeId: String
    private lateinit var tvPlaceName: TextView
    private lateinit var tvPlaceDate: TextView
    private lateinit var tvPlaceRating: TextView
    private lateinit var tvPlaceDescription: TextView
    private lateinit var tvPlaceNotes: TextView
    private lateinit var chipCategory: Chip
    private lateinit var ivBtnMore: ImageView
    private lateinit var ivBtnBack: ImageView
    private lateinit var ratingBar: RatingBar
    private lateinit var rvPlaceImage: RecyclerView
    private lateinit var tvVersion: TextView



    private lateinit var roomViewModel: RoomViewModel

    private val imageListAdapter by lazy { ImageAdapter() }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_place)
        window.statusBarColor = resources.getColor(R.color.black)

        placeId = intent.getStringExtra("placeId").toString()
        roomViewModel = ViewModelProviders.of(this)[RoomViewModel::class.java]

        tvPlaceName = findViewById(R.id.tvPlaceName)
        tvPlaceDate = findViewById(R.id.tvPlaceDate)
        tvPlaceRating = findViewById(R.id.tvRating)
        tvPlaceDescription = findViewById(R.id.tvPlaceDescription)
        tvPlaceNotes = findViewById(R.id.tvPlaceNotes)
        chipCategory = findViewById(R.id.chipCategory)
        ratingBar = findViewById(R.id.ratingBar)
        rvPlaceImage = findViewById(R.id.rvPlaceImage)
        ivBtnMore = findViewById(R.id.ivBtnMore)
        ivBtnBack = findViewById(R.id.ivBtnBack)
        tvVersion = findViewById(R.id.version)

        tvVersion.text = "Version : ${BuildConfig.VERSION_NAME}"

        ivBtnBack.setOnClickListener {
            onBackPressed()
        }

        ivBtnMore.setOnClickListener {
            val popupMenu = android.widget.PopupMenu(this, ivBtnMore)
            popupMenu.menuInflater.inflate(R.menu.pop_up_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_edit -> {
                        val intent = android.content.Intent(this, EditPlaceActivity::class.java)
                        intent.putExtra("placeId", placeId)
                        startActivity(intent)
                        finish()
                    }
                    R.id.action_delete -> {
                        //alert dialog
                        val builder = android.app.AlertDialog.Builder(this)
                        builder.setTitle("Delete Place")
                        builder.setMessage("Are you sure you want to delete this place?")
                        builder.setPositiveButton("Yes") { _, _ ->
                            roomViewModel.deletePlaceData(placeId)
                            Toast.makeText(this, "Place Deleted", Toast.LENGTH_SHORT).show()
                            onBackPressed()
                        }
                        builder.setNegativeButton("No") { _, _ -> }
                        builder.show()
                    }
                }
                true
            }
            popupMenu.show()
        }

        rvPlaceImage.adapter = imageListAdapter
        rvPlaceImage.setHasFixedSize(true)
        rvPlaceImage.layoutManager = LinearLayoutManager(this)

        roomViewModel.getPlaceById(placeId)
        roomViewModel.getPlaceByIdData.observe(this){
            if (it != null){
                Log.d(TAG, "onCreate: $it")
                tvPlaceName.text = it.placeName
                tvPlaceDate.text = it.date
                tvPlaceDescription.text = "Description :\n ${it.description}"
                tvPlaceNotes.text = "Notes : \n${it.notes}"
                chipCategory.text = it.category
                tvPlaceRating.text = "Rating (${it.rating} Out of 5.0)"
                ratingBar.rating = it.rating.toFloat()

                val imageList = it.imageList.replace("[", "").replace("]", "").split(",")
                val bitmap = ArrayList<Bitmap>()
                for (i in imageList){
                    Log.d(TAG, "onCreate: $i")
                    bitmap.add(ImageBitmapString().StringToBitMap(i)!!)
                }
                imageListAdapter.setImageListData(bitmap)
            }
        }
    }
}