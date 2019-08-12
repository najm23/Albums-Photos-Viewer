package org.najmeddine.albumphotosviewer.utils

import android.content.Context
import android.os.Build
import android.view.Window
import android.view.WindowManager


class ViewHelper {

    /**
     * set the activity layout translucent and with no limits
     */
    fun setViewTranslucent(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    /**
     * calculates the size of the item based on the screen size
     */
    fun calculateSizeOfView(context: Context, spanCount: Int): Int {

        val displayMetrics = context.resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels
        return (dpWidth / spanCount)
    }



}