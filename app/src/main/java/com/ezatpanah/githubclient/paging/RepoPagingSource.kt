package com.ezatpanah.githubclient.paging

import android.content.Context
import androidx.paging.PagingSource.LoadResult.Page.Companion.COUNT_UNDEFINED
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.ezatpanah.githubclient.repository.ApiRepository
import com.ezatpanah.githubclient.response.RepositoryResponse
import com.ezatpanah.githubclient.utils.SharedPref
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class RepoPagingSource @Inject constructor(
    private val repository: ApiRepository,
    val context: Context
) : RxPagingSource<Int, RepositoryResponse>() {

    private lateinit var sharedPref: SharedPref

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, RepositoryResponse>> {
        sharedPref = SharedPref(context)

        var nextPageNumber = params.key ?: 1

        return repository.getRepositories("bearer ${sharedPref.accessToken}", nextPageNumber)
                .subscribeOn(Schedulers.io())
                .map { response: Response<MutableList<RepositoryResponse>> -> response.body()?.let { toLoadResult(it, nextPageNumber) } }
                .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(
        response: MutableList<RepositoryResponse>,
        position:Int
    ): LoadResult<Int, RepositoryResponse> {

        return LoadResult.Page(
            response,
            null,
            position + 1,
            COUNT_UNDEFINED,
            COUNT_UNDEFINED
        )

    }

    override fun getRefreshKey(state: PagingState<Int, RepositoryResponse>): Int? {
        return null
    }
}