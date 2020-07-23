package org.najmeddine.albumphotosviewer.presentation.viewmodels

import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.najmeddine.albumphotosviewer.core.model.Album
import org.najmeddine.albumphotosviewer.network.Client
import org.najmeddine.albumphotosviewer.network.GetDataApi

class AlbumsViewModel : ViewModel() {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    lateinit var albumsApi: GetDataApi
    private lateinit var progressBarState : MutableLiveData<Boolean>
    private lateinit var albumList : MutableLiveData<List<Album>>

    fun getAlbumslist(): LiveData<List<Album>>{
        return albumList
    }

    fun getPregressBarState(): LiveData<Boolean>{
        return progressBarState
    }

    fun getAlbums() {
        val client = Client.instance
        albumsApi = client.create(GetDataApi::class.java)
        progressBarState.postValue(true)
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

    private fun getUsers(albums: List<Album>) {
        compositeDisposable.add(albumsApi.users
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { users ->
                albums.forEach lit@{ album ->
                    users.forEach {
                        if (it.id.equals(album.userId)) {
                            album.author = it.name
                            return@lit
                        }
                    }
                }
                albumList.postValue(albums)
            }
        )
    }
}