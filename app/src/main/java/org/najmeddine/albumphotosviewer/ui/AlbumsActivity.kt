package org.najmeddine.albumphotosviewer.ui


import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_album.*
import kotlinx.android.synthetic.main.custom_toolbar_layout.*
import org.najmeddine.albumphotosviewer.Adapter.AlbumAdapter
import org.najmeddine.albumphotosviewer.R
import org.najmeddine.albumphotosviewer.R.layout
import org.najmeddine.albumphotosviewer.model.Album
import org.najmeddine.albumphotosviewer.retrofit.Client
import org.najmeddine.albumphotosviewer.retrofit.GetDataApi
import org.najmeddine.albumphotosviewer.utils.NetworkStateReceiver
import org.najmeddine.albumphotosviewer.utils.ViewHelper


class AlbumsActivity : AppCompatActivity(), NetworkStateReceiver.NetworkStateReceiverListener {

    private var networkStateReceiver: NetworkStateReceiver? = null
    private lateinit var albumsApi: GetDataApi
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewHelper().setViewTranslucent(window)
        setContentView(layout.activity_album)
        app_bar_title.text = getString(R.string.list_of_albums)
        app_bar_icon.setImageDrawable(getDrawable(R.drawable.ic_albums))
        album_progressBar.visibility = View.VISIBLE
        setNetworkStateReceiver()
    }

    private fun setNetworkStateReceiver() {
        networkStateReceiver = NetworkStateReceiver(this)
        networkStateReceiver!!.addListener(this)
        applicationContext.registerReceiver(networkStateReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    fun initAlbumRecyclerView() {
        albums_rv.setHasFixedSize(true)
        albums_rv.layoutManager = LinearLayoutManager(this)
        album_no_internet.visibility = View.GONE
        albums_rv.visibility = View.VISIBLE
    }

    override fun onNetworkAvailable() {
        initAlbumRecyclerView()
        //initApi
        val client = Client.instance
        albumsApi = client.create(GetDataApi::class.java)
        getAlbums()
    }

    override fun onNetworkUnavailable() {
        album_progressBar.visibility = View.GONE
        albums_rv.visibility = View.GONE
        album_no_internet.visibility = View.VISIBLE
        album_no_internet.text = getString(R.string.noInternetConnection)
    }


    private fun getAlbums() {
        compositeDisposable.add(albumsApi.albums
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { albums ->
                //sort album list alphabetically
                val sortedAlbumList = albums.sortedWith(compareBy<Album> { it.title }.thenBy { it.id })
                getUsers(sortedAlbumList)
            }
        )
    }

    private fun getUsers(albums: List<Album>?) {
        compositeDisposable.add(albumsApi.users
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { users ->
                albums?.forEach lit@{ album ->
                    users.forEach {
                        if (it.id.equals(album.userId)) {
                            album.author = it.name
                            return@lit
                        }
                    }
                }
                displayData(albums)
            }
        )
    }

    private fun displayData(albums: List<Album>?) {
        val albumAdapter = AlbumAdapter(this, albums!!)
        albums_rv.adapter = albumAdapter
        album_progressBar.visibility = View.GONE
    }


}

