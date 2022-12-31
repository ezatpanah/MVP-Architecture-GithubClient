package com.ezatpanah.githubclient.ui.base

import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable

open class BasePresenterImpl : BasePresenter {
    @NonNull
    var disposable: Disposable? = null

    override fun onStop() {
        disposable?.let {
            it.dispose()

        }
    }
}