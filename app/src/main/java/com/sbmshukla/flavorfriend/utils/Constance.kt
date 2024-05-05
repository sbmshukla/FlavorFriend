package com.sbmshukla.flavorfriend.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class Constance {

    companion object{
        fun isInternetConnected(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            return connectivityManager?.run {
                val capabilities = getNetworkCapabilities(activeNetwork)
                capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                ) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
            } ?: false
        }

    }
}