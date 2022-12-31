package com.ezatpanah.githubclient.ui.login

import com.ezatpanah.githubclient.response.AccessTokenResponse
import com.ezatpanah.githubclient.ui.base.BasePresenter
import com.ezatpanah.githubclient.ui.base.BaseView

interface LoginContracts {
    interface View : BaseView {
        fun loadAccessToken(data : AccessTokenResponse)
    }

    interface Presenter : BasePresenter {
        fun callGetAccessToken(clientId: String,clientSecret: String,code: String)
    }
}