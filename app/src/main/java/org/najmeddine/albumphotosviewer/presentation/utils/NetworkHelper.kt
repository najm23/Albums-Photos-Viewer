package org.najmeddine.albumphotosviewer.presentation.utils

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager


fun setNetworkStateReceiver(context:Context?, listener: NetworkStateReceiver.NetworkStateReceiverListener) {
    val networkStateReceiver = NetworkStateReceiver(context)
    networkStateReceiver.addListener(listener)
    context?.registerReceiver(networkStateReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
}