package com.ezatpanah.githubclient.response

import com.google.gson.annotations.SerializedName

data class RepositoryResponse(
    val id: Int,
    val name: String,
    @SerializedName("private")
    val isPrivate: Boolean,
    val language: String,
    val updated_at: String,
    val stargazers_count: Int,
    val forks_count: Int,
    val html_url:String
)