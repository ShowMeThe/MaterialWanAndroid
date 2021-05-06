package com.show.wanandroid.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET

fun checkConnection(context: Context): Boolean {
    val cm = context.getSystemService(ConnectivityManager::class.java)
    val network = cm.activeNetwork ?: return false
    val capabilities = cm.getNetworkCapabilities(network)
    return capabilities?.hasCapability(NET_CAPABILITY_INTERNET)?:false
}
