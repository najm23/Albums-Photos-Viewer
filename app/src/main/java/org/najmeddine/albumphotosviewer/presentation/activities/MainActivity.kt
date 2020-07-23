package org.najmeddine.albumphotosviewer.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.najmeddine.albumphotosviewer.R
import org.najmeddine.albumphotosviewer.presentation.fragments.AlbumsFragment
import org.najmeddine.albumphotosviewer.presentation.utils.NavigationHelper
import org.najmeddine.albumphotosviewer.presentation.utils.ViewHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewHelper().setViewTranslucent(window)
        setContentView(R.layout.activity_main)
        NavigationHelper().replaceFragment(AlbumsFragment(),this)
    }
}