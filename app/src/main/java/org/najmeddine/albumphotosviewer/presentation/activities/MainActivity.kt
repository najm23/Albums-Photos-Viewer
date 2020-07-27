package org.najmeddine.albumphotosviewer.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.najmeddine.albumphotosviewer.R
import org.najmeddine.albumphotosviewer.core.utils.AlbumSelectedListener
import org.najmeddine.albumphotosviewer.core.utils.NavigationHelper
import org.najmeddine.albumphotosviewer.core.utils.ViewHelper
import org.najmeddine.albumphotosviewer.presentation.fragments.AlbumsFragment
import org.najmeddine.albumphotosviewer.presentation.fragments.GalleryFragment

class MainActivity : AppCompatActivity(), AlbumSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewHelper().setViewTranslucent(window)
        setContentView(R.layout.activity_main)
        NavigationHelper().replaceFragment(AlbumsFragment(this), this)
    }

    override fun onAlbumSelected(albumId: Int) {
        NavigationHelper().replaceFragment(GalleryFragment(albumId), this)
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount

        if (count == 1) {
            super.onBackPressed()
            finish()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}