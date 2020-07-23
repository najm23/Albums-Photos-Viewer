package org.najmeddine.albumphotosviewer.network

import io.reactivex.Observable
import org.najmeddine.albumphotosviewer.core.model.Album
import org.najmeddine.albumphotosviewer.core.model.Photo
import org.najmeddine.albumphotosviewer.core.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface GetDataApi {

    /* Get list of albums */
    @get:GET("albums")
    val albums: Observable<List<Album>>

    /* Get list of users */
    @get:GET("users")
    val users: Observable<List<User>>

    /* Get list of photos */
    @GET("albums/{id}/photos")
    fun getPhotos(@Path("id") id: Number?): Observable<List<Photo>>

}