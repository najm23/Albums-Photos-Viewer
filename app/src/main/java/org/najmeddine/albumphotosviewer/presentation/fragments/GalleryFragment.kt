package org.najmeddine.albumphotosviewer.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.custom_toolbar_layout.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.najmeddine.albumphotosviewer.R
import org.najmeddine.albumphotosviewer.core.model.Photo
import org.najmeddine.albumphotosviewer.presentation.adapters.PhotoAdapter
import org.najmeddine.albumphotosviewer.presentation.utils.*
import org.najmeddine.albumphotosviewer.presentation.viewmodels.GalleryViewModel


class GalleryFragment(private val albumId: Int) : Fragment(), NetworkStateReceiver.NetworkStateReceiverListener {

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
    }

    private fun setProgressBarState(state: Boolean){
        when(state){
            true ->  gallery_progressBar.visibility =View.VISIBLE
            else -> gallery_progressBar.visibility =View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initAlbumRecyclerView()
        setNetworkStateReceiver(context,this)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel.getProgressBarState().observe(viewLifecycleOwner, Observer { state -> setProgressBarState(state) })
        galleryViewModel.getPhotoslist().observe(viewLifecycleOwner, Observer { photos -> displayData(photos) })
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }


    override fun onNetworkAvailable() {
        galleryViewModel.getPhotos(albumId)
    }

    override fun onNetworkUnavailable() {
        gallery_progressBar.visibility = View.GONE
        photos_rv.visibility = View.GONE
        gallery_no_internet.visibility = View.VISIBLE
        gallery_no_internet.text = getString(R.string.noInternetConnection)
    }

    private fun initView(){
        app_bar_title.text = getString(R.string.gallery)
        app_bar_icon.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_photos_gallery, null))
    }

    private fun initAlbumRecyclerView() {
        photos_rv.setHasFixedSize(true)
        photos_rv.layoutManager = GridLayoutManager(context, GALLERY_SPAN_COUNT)
        gallery_no_internet.visibility = View.GONE
        photos_rv.visibility = View.VISIBLE
    }

    private fun displayData(photos: List<Photo>) {
        val photoAdapter = context?.let { PhotoAdapter(it, photos) }
        photos_rv.adapter = photoAdapter
        gallery_progressBar.visibility = View.GONE
    }

}