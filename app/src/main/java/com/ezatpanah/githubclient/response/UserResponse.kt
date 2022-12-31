package com.ezatpanah.githubclient.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("login")
    val username: String,
    val name: String,
    val email: String,
    val bio: String,
    val location: String,
    val followers: Int,
    val following: Int,
    val public_repos: Int,
    val avatar_url:String,
    val owned_private_repos:Int,
    val total_private_repos:Int,
    val plan: Plan
    ){
    data class Plan(
        @SerializedName("private_repos")
        val privateRepos: Int, // 20
    )
}