package com.ezatpanah.githubclient.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ezatpanah.githubclient.paging.RepoPagingSource
import com.ezatpanah.githubclient.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepoViewModel @Inject constructor(
    private val repository: ApiRepository,
    application: Application
) : AndroidViewModel(application){

/*when we need to use context inside our Viewmodel we should use AndroidViewModel (AVM),
because it contains the application context. To retrieve the context call getApplication(),
otherwise use the regular ViewModel (VM).*/

    private val context = getApplication<Application>().applicationContext

    val repoList =Pager(PagingConfig(1)){
        RepoPagingSource(repository,context)
    }.flow.cachedIn(viewModelScope)

}
