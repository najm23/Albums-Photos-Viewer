package org.najmeddine.albumphotosviewer.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.najmeddine.albumphotosviewer.core.model.Photo
import org.najmeddine.albumphotosviewer.network.Client
import org.najmeddine.albumphotosviewer.network.GetDataApi

class GalleryViewModel : ViewModel() {
    private lateinit var photoApi: GetDataApi
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private  var progressBarState : MutableLiveData<Boolean> = MutableLiveData()
    private  var photosList : MutableLiveData<List<Photo>> = MutableLiveData()

    fun getPhotoslist(): LiveData<List<Photo>> {
        return photosList
    }

    fun getProgressBarState(): LiveData<Boolean> {
        return progressBarState
    }

    fun getPhotos(albumId: Number) {
        progressBarState.postValue(true)
        val client = Client.instance
        photoApi = client.create(GetDataApi::class.java)
        compositeDisposable.add(photoApi.getPhotos(albumId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { photos -> photosList.postValue(filterPhotoList(photos, albumId)) }
        )
    }

    private fun filterPhotoList(photoslist: List<Photo>?, albumId: Number): List<Photo> {
        val filtredPhotolist: MutableList<Photo> = ArrayList()
        photoslist?.forEach {
            if (it.albumId == albumId) {
                filtredPhotolist.add(it)
            }
        }
        return filtredPhotolist
    }
}