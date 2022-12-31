package com.ezatpanah.githubclient.ui.base

interface BaseView {
    fun responseError(error: String)
    fun checkInternet(): Boolean
    fun internetError(hasInternet: Boolean)

}