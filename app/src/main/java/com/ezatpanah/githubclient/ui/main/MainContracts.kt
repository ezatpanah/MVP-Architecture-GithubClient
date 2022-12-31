package com.ezatpanah.githubclient.ui.main

import com.ezatpanah.githubclient.response.RepositoryResponse
import com.ezatpanah.githubclient.response.UserResponse
import com.ezatpanah.githubclient.ui.base.BasePresenter
import com.ezatpanah.githubclient.ui.base.BaseView

interface MainContracts {
    interface View : BaseView {
        fun loadGetUserData(data : UserResponse)
        fun loadGetRepositories(data : MutableList<RepositoryResponse>)
    }

    interface Presenter : BasePresenter {
        fun callGetUserData(token: String)
        fun callGetRepositories(token: String,page : Int)
    }
}