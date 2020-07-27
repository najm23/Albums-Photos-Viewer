package org.najmeddine.albumphotosviewer.presentation.viewmodels

import org.junit.Assert.assertFalse
import org.junit.Test
import org.najmeddine.albumphotosviewer.core.model.Photo
import org.najmeddine.albumphotosviewer.network.Client
import org.najmeddine.albumphotosviewer.network.GetDataApi

class GalleryViewModelTest {

    @Test
    fun getPhotos() {
        val client = Client.instance
        val photosList: MutableList<Photo>
        photosList = client.create(GetDataApi::class.java).getPhotos(1).toList().blockingGet()[0]
        assertFalse(photosList.isEmpty())
    }
}
