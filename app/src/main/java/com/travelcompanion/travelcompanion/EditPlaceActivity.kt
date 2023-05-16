package com.travelcompanion.travelcompanion

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.travelcompanion.travelcompanion.adapter.ImageListAdapter
import com.travelcompanion.travelcompanion.room.roomModel.PlaceData
import com.travelcompanion.travelcompanion.utils.ImageBitmapString
import com.travelcompanion.travelcompanion.viewModel.RoomViewModel
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar

class EditPlaceActivity : AppCompatActivity() {

    private lateinit var ivBtnBack: ImageView
    private lateinit var tvVersion: TextView
    private lateinit var tvPlaceRating: TextView
    private lateinit var tilPlaceName: TextInputLayout
    private lateinit var tilDescription: TextInputLayout
    private lateinit var tilDate: TextInputLayout
    private lateinit var tilCategoryDropdown: TextInputLayout
    private lateinit var tilNotes: TextInputLayout
    private lateinit var tilGPS: TextInputLayout
    private lateinit var rvPhotos: RecyclerView

    private lateinit var etPlaceName: EditText
    private lateinit var etDescription: EditText
    private lateinit var etDate: EditText
    private lateinit var actCategory: AutoCompleteTextView
    private lateinit var etNotes: EditText
    private lateinit var etGPS: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var btnAddPhoto: Button
    private lateinit var btnSubmit: Button

    private var gpsLocation: String = "0.0,0.0"
    private var category: String = "other"
    private var ratingValue: String = "0"
    private lateinit var placeId: String
    private val PICK_IMG = 1
    private var imageList: ArrayList<String> = ArrayList()
    private val imageListAdapter by lazy { ImageListAdapter() }
    private lateinit var roomViewModel: RoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_place)

        window.statusBarColor = resources.getColor(R.color.black)

        ivBtnBack = findViewById(R.id.ivBtnBack)
        tvVersion = findViewById(R.id.version)
        tvPlaceRating = findViewById(R.id.tvPlaceRating)
        tilPlaceName = findViewById(R.id.tilPlaceName)
        tilDescription = findViewById(R.id.tilDescription)
        tilDate = findViewById(R.id.tilDate)
        tilCategoryDropdown = findViewById(R.id.tilCategoryDropdown)
        tilNotes = findViewById(R.id.tilNotes)
        tilGPS = findViewById(R.id.tilGPS)
        rvPhotos = findViewById(R.id.rvPhotos)

        etPlaceName = findViewById(R.id.etPlaceName)
        etDescription = findViewById(R.id.etDescription)
        etDate = findViewById(R.id.etDate)
        actCategory = findViewById(R.id.actCategory)
        etNotes = findViewById(R.id.etNotes)
        etGPS = findViewById(R.id.etGPS)
        ratingBar = findViewById(R.id.ratingBar)
        btnAddPhoto = findViewById(R.id.btnAddPhoto)
        btnSubmit = findViewById(R.id.btnSubmit)

        placeId = intent.getStringExtra("placeId").toString()

        roomViewModel = ViewModelProviders.of(this)[RoomViewModel::class.java]

        roomViewModel.getPlaceById(placeId)
        roomViewModel.getPlaceByIdData.observe(this){
            if (it!=null){
                etPlaceName.setText(it.placeName)
                etDescription.setText(it.description)
                etDate.setText(it.date)
                actCategory.setText(it.category)
                etNotes.setText(it.notes)
                etGPS.setText(it.gpsLocation)
                tvPlaceRating.text = "Rating (${it.rating} Out of 5.0)"
                ratingBar.rating = it.rating.toFloat()

                val imgList = it.imageList.replace("[", "").replace("]", "").split(",")
                val bitmap = ArrayList<Bitmap>()
                for (i in imgList){
                    Log.d(ContentValues.TAG, "onCreate: $i")
                    bitmap.add(ImageBitmapString().StringToBitMap(i)!!)
                }
                imageListAdapter.setImageListData(bitmap)

            }
        }


        ivBtnBack.setOnClickListener {
            onBackPressed()
        }
        tvVersion.text = "Version : " + BuildConfig.VERSION_NAME


        btnAddPhoto.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, PICK_IMG)
        }

        actCategory.setOnItemClickListener { parent, view, position, id ->
            category = parent.getItemAtPosition(position).toString()
            Toast.makeText(this, category, Toast.LENGTH_SHORT).show()
        }



        rvPhotos.adapter = imageListAdapter
        rvPhotos.setHasFixedSize(true)
        rvPhotos.layoutManager = GridLayoutManager(this, 3)

        tilDate.setEndIconOnClickListener {
            val datePicker = MaterialDatePicker.Builder
                .datePicker()
                .build()
            datePicker.show(supportFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener {
                etDate.setText(getDate(it))
            }
            datePicker.addOnNegativeButtonClickListener {
                etDate.setText("")
            }
            datePicker.addOnCancelListener {
                etDate.setText("")
            }
        }

        ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                ratingValue = rating.toString()
            }

        btnSubmit.setOnClickListener {
            val placeName = etPlaceName.text.toString()
            val description = etDescription.text.toString()
            val date = etDate.text.toString()
            val notes = etNotes.text.toString()
            val gps = etGPS.text.toString()
            val rating = ratingBar.rating.toString()
            val images = imageList.toString()

            //check if all fields are filled
            if (placeName.isEmpty()) {
                tilPlaceName.error = "Please enter a place name"
                tilPlaceName.requestFocus()
                return@setOnClickListener
            }

            if (description.isEmpty()) {
                tilDescription.error = "Please enter a description"
                tilDescription.requestFocus()
                return@setOnClickListener
            }

            if (date.isEmpty()) {
                tilDate.error = "Please enter a date"
                tilDate.requestFocus()
                return@setOnClickListener
            }

            if (category.isEmpty()) {
                tilCategoryDropdown.error = "Please enter a category"
                tilCategoryDropdown.requestFocus()
                return@setOnClickListener
            }

            if (notes.isEmpty()) {
                tilNotes.error = "Please enter some notes"
                tilNotes.requestFocus()
                return@setOnClickListener
            }

            if (gps.isEmpty()) {
                tilGPS.error = "Please enter a GPS location"
                tilGPS.requestFocus()
                return@setOnClickListener
            }

            if (ratingValue.isEmpty()) {
                Toast.makeText(this, "Please enter a rating", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (images.isEmpty()) {
                Toast.makeText(this, "Please add at least one image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val place = PlaceData(
                placeName = placeName,
                description = description,
                date = date,
                category = category,
                notes = notes,
                gpsLocation = gps,
                rating = rating,
                imageList = imageList.toString()
            )

            Log.d(ContentValues.TAG, "onCreate: $place")
            roomViewModel.updatePlaceData(placeId, placeName, description, date, category, notes, gps, rating)
            Toast.makeText(this, "Place update successfully", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMG && resultCode == RESULT_OK) {

            val bitmaps = ArrayList<Bitmap>()
            val imageSources = ArrayList<String>()

            if (data?.clipData != null) {
                val imgCount: Int = data.clipData!!.itemCount
                for (i in 0 until imgCount) {
//                    val ImgUri: Uri = data.clipData?.getItemAt(i)!!.uri
//                    val ImgUrl = ImgUri.toString()
//                    imageList.add(ImgUrl)
//                    imageListAdapter.setImageListData(imageList)

                    val ImgUri: Uri = data.clipData?.getItemAt(i)!!.uri
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, ImgUri)
                        bitmaps.add(bitmap)
                        val imageSource = ImageBitmapString().BitMapToString(bitmap)
                        imageList.add(imageSource!!)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

//                    imageList.add(getBitmapToByteArray(bitmap))
                    imageListAdapter.setImageListData(bitmaps)
                }
            } else if (data?.data != null) {
//                val ImgUri: Uri = data.data!!
//                imageList.add(ImgUri.toString())
//                imageListAdapter.setImageListData(imageList)
//                val ImgUri: Uri = data.data!!
//                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, ImgUri)
//                imageList.add(getBitmapToByteArray(bitmap))
//                imageListAdapter.setImageListData(imageList)

                val ImgUri: Uri = data.data!!
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, ImgUri)
                    bitmaps.add(bitmap)
                    val imageSource = ImageBitmapString().BitMapToString(bitmap)
                    imageList.add(imageSource!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                imageListAdapter.setImageListData(bitmaps)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDate(milliSeconds: Long): String? {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds.toLong()
        return formatter.format(calendar.time)
    }
}