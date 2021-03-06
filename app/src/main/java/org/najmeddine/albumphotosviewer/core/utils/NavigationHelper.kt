package org.najmeddine.albumphotosviewer.core.utils



import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.najmeddine.albumphotosviewer.R


class NavigationHelper {
    fun replaceFragment(fragment: Fragment, activity: Activity) {
        val transaction = (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
            .addToBackStack(fragment.javaClass.name)
        transaction.commit()
    }
}