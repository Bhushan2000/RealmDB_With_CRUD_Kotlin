package com.example.realmdb_with_crud_kotlin.utils

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtil {
    private const val TAG = "NetworkUtil"
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return !(activeNetworkInfo == null || !activeNetworkInfo.isConnected)
    }
}