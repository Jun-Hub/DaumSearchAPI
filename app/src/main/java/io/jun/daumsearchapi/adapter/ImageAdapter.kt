package io.jun.daumsearchapi.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import com.bumptech.glide.Glide
import io.jun.daumsearchapi.R
import io.jun.daumsearchapi.model.ImageDocument
import io.jun.daumsearchapi.view.ImageActivity

class ImageAdapter internal constructor(private val context: Context)
    : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var items: MutableList<ImageDocument> = mutableListOf()

    inner class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ImageViewHolder {
        val itemView = inflater.inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holer: ImageViewHolder, position: Int) {
        items[position].let { item ->
            with(holer) {

                Glide.with(context)
                    .load(item.thumbnail_url)
                    .fitCenter()
                    .into(imageView)

                imageView.setOnClickListener {
                    context.startActivity(Intent(context, ImageActivity::class.java)
                        .putExtra("image_url", item.image_url)
                        .putExtra("display_sitename", item.display_sitename)
                        .putExtra("datetime", item.datetime))
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun setImageDocs(imageDocs: MutableList<ImageDocument>) {
        items = imageDocs
        notifyDataSetChanged()
    }

    fun addImageDocs(imageDocs: MutableList<ImageDocument>) {
        imageDocs.forEach {
            items.add(it)
        }
        notifyItemRangeInserted(itemCount-10, itemCount)
    }

}