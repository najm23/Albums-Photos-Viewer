package org.najmeddine.albumphotosviewer.presentation.adapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import org.najmeddine.albumphotosviewer.R
import org.najmeddine.albumphotosviewer.core.model.Album
import org.najmeddine.albumphotosviewer.presentation.utils.EXTRA_ALBUM_ID


class AlbumAdapter(
    private val context: Context?,
    private var albumList: List<Album>
    private lateinit var progressBarState : MutableLiveData<Boolean>
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
            StringBuilder().append(context?.getString(R.string.album_title), albumList[position].title).toString()



        holder.authorTv?.text =
            StringBuilder().append(context?.getString(R.string.album_author), albumList[position].author).toString()
        holder.albumView?.setOnClickListener {


            fun getAlbumslist(): LiveData<List<Album>> {
                return albumList
            }

            fun getPregressBarState(): LiveData<Boolean> {
                return progressBarState
            }
        }
    }

    inner class AlbumHolder(albumView: View) : RecyclerView.ViewHolder(albumView) {
        val authorTv = itemView.findViewById<TextView?>(R.id.album_author_tv)
        val titleTv = itemView.findViewById<TextView?>(R.id.album_title_tv)
        val albumView = itemView.findViewById<View?>(R.id.card_view)
    }

}