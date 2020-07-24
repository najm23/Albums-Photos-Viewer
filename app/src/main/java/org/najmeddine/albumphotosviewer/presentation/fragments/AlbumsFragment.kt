package org.najmeddine.albumphotosviewer.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.custom_toolbar_layout.*
import kotlinx.android.synthetic.main.fragment_albums.*
import org.najmeddine.albumphotosviewer.R
import org.najmeddine.albumphotosviewer.core.model.Album
import org.najmeddine.albumphotosviewer.presentation.adapters.AlbumAdapter
import org.najmeddine.albumphotosviewer.presentation.utils.AlbumSelectedListener
import org.najmeddine.albumphotosviewer.presentation.utils.NetworkStateReceiver
import org.najmeddine.albumphotosviewer.presentation.utils.setNetworkStateReceiver
import org.najmeddine.albumphotosviewer.presentation.viewmodels.AlbumsViewModel


class AlbumsFragment (private val albumSelectedListener: AlbumSelectedListener) : Fragment() , NetworkStateReceiver.NetworkStateReceiverListener{

    private lateinit var albumsViewModel:AlbumsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        albumsViewModel = ViewModelProvider(this).get(AlbumsViewModel::class.java)
    }

    private fun setProgressBarState(state: Boolean){
        when(state){
            true ->  album_progressBar.visibility =View.VISIBLE
            else -> album_progressBar.visibility =View.GONE
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        albumsViewModel.getProgressBarState().observe(viewLifecycleOwner, Observer { state -> setProgressBarState(state) })
        albumsViewModel.getAlbumslist().observe(viewLifecycleOwner, Observer { albums -> displayData(albums) })
        return inflater.inflate(R.layout.fragment_albums, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initAlbumRecyclerView()
        setNetworkStateReceiver(context,this)
    }

    private fun initView(){
        app_bar_title.text = getString(R.string.list_of_albums)
        app_bar_icon.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_albums, null))
    }

    private fun initAlbumRecyclerView() {
        albums_rv.setHasFixedSize(true)
        albums_rv.layoutManager = LinearLayoutManager(context)
        album_no_internet.visibility = View.GONE
        albums_rv.visibility = View.VISIBLE
    }

    override fun onNetworkAvailable() {
        albumsViewModel.getAlbums()
    }

    override fun onNetworkUnavailable() {
        album_progressBar.visibility = View.GONE
        albums_rv.visibility = View.GONE
        album_no_internet.visibility = View.VISIBLE
        album_no_internet.text = getString(R.string.noInternetConnection)
    }




    private fun displayData(albums: List<Album>) {
        val albumAdapter =
            AlbumAdapter(
                context,
                albums,
                albumSelectedListener
            )
        albums_rv.visibility=View.VISIBLE
        albums_rv.adapter = albumAdapter
        album_progressBar.visibility = View.GONE
    }

}