package com.ezatpanah.githubclient.api

import com.ezatpanah.githubclient.response.AccessTokenResponse
import com.ezatpanah.githubclient.response.RepositoryResponse
import com.ezatpanah.githubclient.response.UserResponse
import com.ezatpanah.githubclient.utils.Constants
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface ApiServices {

    @Headers("Accept: application/json")
    @POST(Constants.domainURL + "login/oauth/access_token")
    @FormUrlEncoded
    fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String
    ): Single<Response<AccessTokenResponse>>

    @Headers("Accept: application/json")
    @GET("user")
    fun getUserData(
        @Header("authorization") token: String
    ): Single<Response<UserResponse>>


    @Headers("Accept: application/json")
    @GET("user/repos")
    fun getRepositories(
        @Header("authorization") token: String,
        @Query("page") page: Int
    ): Single<Response<MutableList<RepositoryResponse>>>

}