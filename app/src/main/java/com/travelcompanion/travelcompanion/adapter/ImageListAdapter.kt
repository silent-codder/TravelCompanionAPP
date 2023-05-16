package com.travelcompanion.travelcompanion.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.travelcompanion.travelcompanion.R

class ImageListAdapter : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {

    private lateinit var context: Context
    private var mList = ArrayList<Bitmap>()

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView){
        val image: ImageView = itemView.findViewById(R.id.image)
        val cancel: ImageView = itemView.findViewById(R.id.btnCancel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.image_list_view, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mList[position]
        holder.image.setImageBitmap(data)
//        holder.image.setImageBitmap(toBitmap(data))
//        Picasso.get().load(data).into(holder.image)
        holder.cancel.setOnClickListener {
            removeItem(position)
        }
    }

    override fun getItemCount(): Int {
        return mList.size ?: 0
    }

    fun setImageListData(image:List<Bitmap>) {
        mList = image as ArrayList<Bitmap>
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        mList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mList.size)
    }

    fun toBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}