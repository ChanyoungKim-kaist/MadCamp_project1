package com.example.viewpager.adapters

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.viewpager.R
import java.io.IOException

class PhotoAdapter(private val mContext: Context) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
    private var mListPhoto: List<Uri>? = null
    fun setData(list: List<Uri>?) {
        mListPhoto = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val uri = mListPhoto!![position] ?: return
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(mContext.contentResolver, uri)
            if (bitmap != null) {
                holder.imgPhoto.setImageBitmap(bitmap)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return if (mListPhoto != null) {
            mListPhoto!!.size
        } else 0
    }

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_photo)

    }
}