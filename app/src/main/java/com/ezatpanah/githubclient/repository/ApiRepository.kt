package com.ezatpanah.githubclient.repository

import com.ezatpanah.githubclient.api.ApiServices
import com.ezatpanah.githubclient.response.AccessTokenResponse
import com.ezatpanah.githubclient.response.RepositoryResponse
import com.ezatpanah.githubclient.response.UserResponse
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject


class ApiRepository @Inject constructor(
    private val apiServices: ApiServices
) {
    fun getAccessToken(clientId: String, clientSecret: String, code: String): Single<Response<AccessTokenResponse>> {
        return apiServices.getAccessToken(clientId, clientSecret, code)
    }

    fun getUserData(token: String): Single<Response<UserResponse>> {
        return apiServices.getUserData(token)
    }

    fun getRepositories(token: String, page: Int): Single<Response<MutableList<RepositoryResponse>>> {
        return apiServices.getRepositories(token, page)
    }
}