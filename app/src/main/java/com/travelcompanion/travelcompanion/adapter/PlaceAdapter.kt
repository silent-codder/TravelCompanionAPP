package com.travelcompanion.travelcompanion.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.travelcompanion.travelcompanion.R
import com.travelcompanion.travelcompanion.ViewPlaceActivity
import com.travelcompanion.travelcompanion.room.roomModel.PlaceData

class PlaceAdapter: RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    private lateinit var context: Context
    private var mList = ArrayList<PlaceData>()

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView){
        val placeName = itemView.findViewById(R.id.tvPlaceName) as TextView
        val placeDate = itemView.findViewById(R.id.tvPlaceDate) as TextView
        val placeDescription = itemView.findViewById(R.id.tvPlaceDescription) as TextView
        val cvPlaceList = itemView.findViewById(R.id.cvPlaceList) as CardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.place_list_view, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mList[position]
        holder.placeName.text = data.placeName
        holder.placeDate.text = data.date
        holder.placeDescription.text = data.description

        holder.cvPlaceList.setOnClickListener {
            val intent = Intent(context, ViewPlaceActivity::class.java)
            intent.putExtra("placeId", data.id.toString())
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return mList.size ?: 0
    }

    fun setPlaceListData(place:List<PlaceData>) {
        mList = place as ArrayList<PlaceData>
        notifyDataSetChanged()
    }
}