package org.najmeddine.albumphotosviewer.presentation.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.najmeddine.albumphotosviewer.R
import org.najmeddine.albumphotosviewer.core.model.Album
import org.najmeddine.albumphotosviewer.core.utils.AlbumSelectedListener


class AlbumAdapter(private val context: Context?, private var albumList: List<Album>, private val albumSelectedListener: AlbumSelectedListener) : RecyclerView.Adapter<AlbumAdapter.AlbumHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.album_card_layout, parent, false
        )
        return AlbumHolder(itemView)
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    override fun onBindViewHolder(holder: AlbumHolder, position: Int) {
        holder.titleTv?.text =
            StringBuilder().append(context?.getString(R.string.album_title), albumList[position].title).toString()
        holder.authorTv?.text =
            StringBuilder().append(context?.getString(R.string.album_author), albumList[position].author).toString()
        holder.albumView?.setOnClickListener {
            albumSelectedListener.onAlbumSelected(albumList[position].id)
        }
    }

    inner class AlbumHolder(albumView: View) : RecyclerView.ViewHolder(albumView) {
        val authorTv = itemView.findViewById<TextView?>(R.id.album_author_tv)
        val titleTv = itemView.findViewById<TextView?>(R.id.album_title_tv)
        val albumView = itemView.findViewById<View?>(R.id.card_view)
    }
}