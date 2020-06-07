package com.calcforbuilder.provider

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.File

class AdapterRec(private val context: Context, private var files: List<File>?) :
    RecyclerView.Adapter<AdapterRec.Holder>() {

    fun setFileList(_files: List<File>?) {
        files = _files
        notifyDataSetChanged()
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var image: ImageView = itemView.findViewById(R.id.iv_image)

        fun bind(file: File) {
            Glide.with(context)
                .load(Uri.fromFile(file))
                .into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.holder_media, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = files?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        files?.let { holder.bind(it[position]) }
    }
}