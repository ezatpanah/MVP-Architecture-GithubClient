package com.ezatpanah.githubclient.di

import androidx.fragment.app.Fragment
import com.ezatpanah.githubclient.ui.login.LoginContracts
import com.ezatpanah.githubclient.ui.main.MainContracts
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object ContractsModule {
    @Provides
    fun loginView(fragment: Fragment) : LoginContracts.View{
        return fragment as LoginContracts.View
    }

    @Provides
    fun mainView(fragment: Fragment) : MainContracts.View{
        return fragment as MainContracts.View
    }
}