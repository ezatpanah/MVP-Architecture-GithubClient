package com.ezatpanah.githubclient.ui.login

import android.util.Log
import com.ezatpanah.githubclient.repository.ApiRepository
import com.ezatpanah.githubclient.ui.base.BasePresenterImpl
import com.ezatpanah.githubclient.utils.applyIoScheduler
import javax.inject.Inject

class LoginPresenter
@Inject constructor(
    private val repository: ApiRepository,
    val view: LoginContracts.View
) : LoginContracts.Presenter, BasePresenterImpl() {
    override fun callGetAccessToken(clientId: String, clientSecret: String, code: String) {
        if (view.checkInternet()) {
            disposable = repository
                .getAccessToken(clientId, clientSecret, code)
                .applyIoScheduler()
                .subscribe({ response ->
                    when (response.code()) {
                        in 200..202 -> {
                            Log.e("LoginPresenter", "${response.code()}")
                            response.body()?.let { itBody ->
                                view.loadAccessToken(itBody)
                            }
                        }
                        in 300..399 -> {
                            Log.d("Response Code", " Redirection messages : ${response.code()}")
                        }
                        in 400..499 -> {
                            Log.d("Response Code", " Client error responses : ${response.code()}")
                        }
                        in 500..599 -> {
                            Log.d("Response Code", " Server error responses : ${response.code()}")
                        }
                    }
                }, { error ->
                    view.responseError(error.message.toString())
                    Log.e("LoginPresenter", error.message.toString())
                })
        }
    }
}