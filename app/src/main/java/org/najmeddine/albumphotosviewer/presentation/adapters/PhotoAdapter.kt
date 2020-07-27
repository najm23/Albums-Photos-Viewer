package org.najmeddine.albumphotosviewer.presentation.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.najmeddine.albumphotosviewer.R
import org.najmeddine.albumphotosviewer.core.model.Photo
import org.najmeddine.albumphotosviewer.core.utils.GALLERY_SPAN_COUNT
import org.najmeddine.albumphotosviewer.core.utils.ViewHelper


class PhotoAdapter(
    private val context: Context,
    private var photoList: List<Photo>
) : RecyclerView.Adapter<PhotoAdapter.PhotoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.photo_card_layout, parent, false
        )

        // calculate photo size(same for the width and height)
        val size = ViewHelper().calculateSizeOfView(context, GALLERY_SPAN_COUNT)

        itemView.layoutParams = GridLayout.LayoutParams(ViewGroup.LayoutParams(size, size))

        return PhotoHolder(itemView)

    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        Picasso.get().load(photoList[position].url)
            .placeholder(R.drawable.ic_file_download)
            .error(R.drawable.ic_broken_image)
            .into(holder.photoIv)
    }

    inner class PhotoHolder(photoView: View) : RecyclerView.ViewHolder(photoView) {
        val photoIv = itemView.findViewById<ImageView?>(R.id.photo_iv)
    }

}