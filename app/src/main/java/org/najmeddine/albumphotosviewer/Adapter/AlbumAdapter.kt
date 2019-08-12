package org.najmeddine.albumphotosviewer.Adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.najmeddine.albumphotosviewer.R
import org.najmeddine.albumphotosviewer.model.Album
import org.najmeddine.albumphotosviewer.ui.GalleryActivity
import org.najmeddine.albumphotosviewer.utils.EXTRA_ALBUM_ID


class AlbumAdapter(
    private val context: Context,
    private var albumList: List<Album>
) : RecyclerView.Adapter<AlbumAdapter.AlbumHolder>() {

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
            StringBuilder().append(context.getString(R.string.album_title), albumList[position].title).toString()



        holder.authorTv?.text =
            StringBuilder().append(context.getString(R.string.album_author), albumList[position].author).toString()
        holder.albumView?.setOnClickListener {
            val intent = Intent(context, GalleryActivity::class.java)
            intent.putExtra(EXTRA_ALBUM_ID, albumList[position].id)
            context.startActivity(intent)
        }
    }

    inner class AlbumHolder(albumView: View) : RecyclerView.ViewHolder(albumView) {
        val authorTv = itemView.findViewById<TextView?>(R.id.album_author_tv)
        val titleTv = itemView.findViewById<TextView?>(R.id.album_title_tv)
        val albumView = itemView.findViewById<View?>(R.id.card_view)
    }

}