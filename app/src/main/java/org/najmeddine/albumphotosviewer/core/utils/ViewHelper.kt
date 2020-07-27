package org.najmeddine.albumphotosviewer.core.utils

import android.content.Context
import android.view.Window
import android.view.WindowManager


class ViewHelper {

    /**
     * set the activity layout translucent and with no limits
     */
    fun setViewTranslucent(window: Window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
    }

    /**
     * calculates the size of the item based on the screen size
     */
    fun calculateSizeOfView(context: Context?, spanCount: Int): Int {

        val displayMetrics = context!!.resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels
        return (dpWidth / spanCount)
    }



}