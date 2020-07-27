package org.najmeddine.albumphotosviewer.presentation.viewmodels


import org.junit.Assert.assertFalse
import org.junit.Test
import org.najmeddine.albumphotosviewer.core.model.Album
import org.najmeddine.albumphotosviewer.core.model.User
import org.najmeddine.albumphotosviewer.network.Client
import org.najmeddine.albumphotosviewer.network.GetDataApi


class AlbumsViewModelTest {
    private lateinit var albumApi: GetDataApi
    private val client = Client.instance

    @Test
    fun getAlbums() {
        albumApi = client.create(GetDataApi::class.java)
        val albumList: MutableList<Album> = albumApi.albums.toList().blockingGet()[0]
        assertFalse(albumList.isEmpty())
    }

    @Test
    fun getUsers() {
        albumApi = client.create(GetDataApi::class.java)
        val usersList: MutableList<User> = albumApi.users.toList().blockingGet()[0]
        assertFalse(usersList.isEmpty())
    }
}


