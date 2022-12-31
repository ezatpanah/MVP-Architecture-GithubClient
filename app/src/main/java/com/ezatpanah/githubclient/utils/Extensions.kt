package com.ezatpanah.githubclient.utils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

//RxJava
fun <T : Any> Single<T>.applyScheduler(scheduler: Scheduler): Single<T> = subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread())
fun <T : Any> Single<T>.applyIoScheduler() = applyScheduler(Schedulers.io())

//View
fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

//Check network
fun Context.isNetworkAvailable(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info = cm.activeNetworkInfo
    return info != null && info.isConnected
}


