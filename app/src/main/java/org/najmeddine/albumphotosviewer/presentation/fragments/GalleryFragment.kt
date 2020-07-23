package org.najmeddine.albumphotosviewer.presentation.fragments

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.custom_toolbar_layout.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.najmeddine.albumphotosviewer.R
import org.najmeddine.albumphotosviewer.core.model.Photo
import org.najmeddine.albumphotosviewer.network.Client
import org.najmeddine.albumphotosviewer.network.GetDataApi
import org.najmeddine.albumphotosviewer.presentation.utils.EXTRA_ALBUM_DEFAULT_ID
import org.najmeddine.albumphotosviewer.presentation.utils.EXTRA_ALBUM_ID
import org.najmeddine.albumphotosviewer.presentation.utils.GALLERY_SPAN_COUNT
import org.najmeddine.albumphotosviewer.presentation.utils.NetworkStateReceiver
import org.najmeddine.photophotosviewer.Adapter.PhotoAdapter


class GalleryFragment : Fragment(), NetworkStateReceiver.NetworkStateReceiverListener {

    private var networkStateReceiver: NetworkStateReceiver? = null


    private lateinit var photoApi: GetDataApi
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var albumId: Number

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt(EXTRA_ALBUM_ID, EXTRA_ALBUM_DEFAULT_ID)?.let {
            albumId = it
        }
        app_bar_title.text = getString(R.string.gallery)
        app_bar_icon.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_photos_gallery, null))
        gallery_progressBar.visibility = View.VISIBLE
        setNetworkStateReceiver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    private fun setNetworkStateReceiver() {
        networkStateReceiver = NetworkStateReceiver(context)
        networkStateReceiver!!.addListener(this)
        context?.registerReceiver(networkStateReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    fun initAlbumRecyclerView() {
        photos_rv.setHasFixedSize(true)
        photos_rv.layoutManager = GridLayoutManager(context, GALLERY_SPAN_COUNT)
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

    private fun getPhotos(albumId: Number) {
        gallery_progressBar.visibility = View.VISIBLE
        compositeDisposable.add(photoApi.getPhotos(albumId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { photos -> displayData(filterPhotoList(photos, albumId)) }
        )
    }

    private fun filterPhotoList(photoslist: List<Photo>?, albumId: Number): List<Photo> {
        val filtredPhotolist: MutableList<Photo> = ArrayList()
        photoslist?.forEach {
            if (it.albumId.equals(albumId)) {
                filtredPhotolist.add(it)
            }
        }
        return filtredPhotolist
    }

    private fun displayData(photos: List<Photo>?) {
        val photoAdapter = PhotoAdapter(context, photos!!)
        photos_rv.adapter = photoAdapter
        gallery_progressBar.visibility = View.GONE
    }

}