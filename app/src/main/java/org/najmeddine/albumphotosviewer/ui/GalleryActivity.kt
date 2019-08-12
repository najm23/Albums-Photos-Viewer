package org.najmeddine.albumphotosviewer.ui

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.custom_toolbar_layout.*
import org.najmeddine.albumphotosviewer.R
import org.najmeddine.albumphotosviewer.model.Photo
import org.najmeddine.albumphotosviewer.retrofit.Client
import org.najmeddine.albumphotosviewer.retrofit.GetDataApi
import org.najmeddine.albumphotosviewer.utils.*
import org.najmeddine.photophotosviewer.Adapter.PhotoAdapter

class GalleryActivity : AppCompatActivity(), NetworkStateReceiver.NetworkStateReceiverListener {

    private var networkStateReceiver: NetworkStateReceiver? = null


    private lateinit var photoApi: GetDataApi
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var albumId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewHelper().setViewTranslucent(window)
        setContentView(R.layout.activity_gallery)
        albumId = intent.getIntExtra(EXTRA_ALBUM_ID, EXTRA_ALBUM_DEFAULT_ID)
        app_bar_title.text = getString(R.string.gallery)
        app_bar_icon.setImageDrawable(getDrawable(R.drawable.ic_photos_gallery))
        gallery_progressBar.visibility = View.VISIBLE
        setNetworkStateReceiver()
    }

    private fun setNetworkStateReceiver() {
        networkStateReceiver = NetworkStateReceiver(this)
        networkStateReceiver!!.addListener(this)
        applicationContext.registerReceiver(networkStateReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    fun initAlbumRecyclerView() {
        photos_rv.setHasFixedSize(true)
        photos_rv.layoutManager = GridLayoutManager(this, GALLERY_SPAN_COUNT)
        gallery_no_internet.visibility = View.GONE
        photos_rv.visibility = View.VISIBLE
    }

    override fun onNetworkAvailable() {
        initAlbumRecyclerView()
        //initApi
        val client = Client.instance
        photoApi = client.create(GetDataApi::class.java)
        getPhotos(albumId) //TODO need to fix albumId
    }

    override fun onNetworkUnavailable() {
        gallery_progressBar.visibility = View.GONE
        photos_rv.visibility = View.GONE
        gallery_no_internet.visibility = View.VISIBLE
        gallery_no_internet.text = getString(R.string.noInternetConnection)
    }

    private fun getPhotos(albumId: Int) {
        compositeDisposable.add(photoApi.getPhotos(albumId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { photos -> displayData(filterPhotoList(photos, albumId)) }
        )
    }

    private fun filterPhotoList(photoslist: List<Photo>?, albumId: Int): List<Photo> {
        val filtredPhotolist: MutableList<Photo> = ArrayList()
        photoslist?.forEach {
            if (it.albumId.equals(albumId)) {
                filtredPhotolist.add(it)
            }
        }
        return filtredPhotolist
    }

    private fun displayData(photos: List<Photo>?) {
        val photoAdapter = PhotoAdapter(this, photos!!)
        photos_rv.adapter = photoAdapter
        gallery_progressBar.visibility = View.GONE
    }
}
