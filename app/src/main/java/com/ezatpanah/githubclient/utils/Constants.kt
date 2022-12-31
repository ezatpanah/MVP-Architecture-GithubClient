package com.ezatpanah.githubclient.utils

import android.content.Context

object Constants {

    const val domainURL = "https://github.com/"
    const val BASE_URL = "https://api.github.com/"

    /* Put your
         CLIENT_ID
         CLIENT_SECRET
         REDIRECT_URI
       Here*/
    const val oauthLoginURL = "https://github.com/login/oauth/authorize"

    const val NETWORK_TIMEOUT = 60L

    const val NAMED_HEADER = "named_header"
    const val NAMED_BODY = "named_body"

    const val SCOPE = "repo,read:user"

    const val PREF_NAME = "GithubApp"
    const val PREF_MODE = Context.MODE_PRIVATE
    val PREF_ACCESS_TOKEN = Pair("ACCESS_TOKEN", "")

}