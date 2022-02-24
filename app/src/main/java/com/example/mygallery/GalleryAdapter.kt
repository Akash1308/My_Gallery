package com.example.mygallery

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GalleryAdapter(private val items: List<ImageData> = listOf()) : RecyclerView.Adapter<GalleryViewHolder> (){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false)
        return GalleryViewHolder(view)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val currentItem = items[position]
        Glide.with(holder.itemView.context).load(currentItem.contentUri).into(holder.imageView)

    }

    override fun getItemCount(): Int {
        return items.size
    }





}

class GalleryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val imageView: ImageView = itemView.findViewById(R.id.image)

}