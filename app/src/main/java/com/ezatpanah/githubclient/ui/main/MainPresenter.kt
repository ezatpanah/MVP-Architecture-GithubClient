package com.ezatpanah.githubclient.ui.main

import android.util.Log
import com.ezatpanah.githubclient.repository.ApiRepository
import com.ezatpanah.githubclient.ui.base.BasePresenterImpl
import com.ezatpanah.githubclient.utils.applyIoScheduler
import javax.inject.Inject

class MainPresenter
@Inject constructor(
    private val repository: ApiRepository,
    val view: MainContracts.View
) : MainContracts.Presenter, BasePresenterImpl() {
    override fun callGetUserData(token: String) {
        if (view.checkInternet()) {

            disposable = repository
                .getUserData(token)
                .applyIoScheduler()
                .subscribe { response ->
                    Log.e("MainPresenter", "${response.code()}")
                    when (response.code()) {
                        in 200..202 ->
                            response.body()?.let { itBody ->
                                Log.e("MainPresenter", "itBody : $itBody")
                                view.loadGetUserData(itBody)
                            }
                        in 300..399 -> {
                            Log.d("MainPresenter", " Redirection messages : ${response.code()}")
                        }
                        in 400..499 -> {
                            Log.d("MainPresenter", " Client error responses : ${response.code()}")
                        }
                        in 500..599 -> {
                            Log.d("MainPresenter", " Server error responses : ${response.code()}")
                        }
                    }
                }
        }
    }

    override fun callGetRepositories(token: String,page:Int) {
        if (view.checkInternet()) {
            disposable = repository
                .getRepositories(token, page)
                .applyIoScheduler()
                .subscribe { response ->
                    when (response.code()) {
                        in 200..202 ->
                            response.body()?.let { itBody ->
                                Log.e("MainPresenter", "itBody : $itBody")
                                view.loadGetRepositories(itBody)
                            }
                        in 300..399 -> {
                            Log.d("MainPresenter", " Redirection messages : ${response.code()}")
                        }
                        in 400..499 -> {
                            Log.d("MainPresenter", " Client error responses : ${response.code()}")
                        }
                        in 500..599 -> {
                            Log.d("MainPresenter", " Server error responses : ${response.code()}")
                        }
                    }
                }
        }
    }
}