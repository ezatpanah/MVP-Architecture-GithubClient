package com.ezatpanah.githubclient.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.ezatpanah.githubclient.R
import com.ezatpanah.githubclient.adapter.RepositoryAdapter
import com.ezatpanah.githubclient.databinding.FragmentMainBinding
import com.ezatpanah.githubclient.response.RepositoryResponse
import com.ezatpanah.githubclient.response.UserResponse
import com.ezatpanah.githubclient.utils.SharedPref
import com.ezatpanah.githubclient.utils.isNetworkAvailable
import com.ezatpanah.githubclient.viewmodel.RepoViewModel

import dagger.hilt.android.AndroidEntryPoint
import greyfox.rxnetwork.RxNetwork
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(), MainContracts.View {

    lateinit var binding: FragmentMainBinding
    private val repositoryAdapter by lazy { RepositoryAdapter() }
    var doubleBackToExitPressedOnce = false
    private lateinit var sharedPref: SharedPref
    private val repoViewModel: RepoViewModel by viewModels()

    @Inject
    lateinit var mainPresenter: MainPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = SharedPref(requireContext())
        binding.imgLogout.setOnClickListener { logout() }

        //CheckInternet
        RxNetwork.init(requireContext()).observe()
            .subscribeOn(Schedulers.io())
            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe { internetError(it.isConnected) }

    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.onStop()
    }

    @SuppressLint("SetTextI18n")
    override fun loadGetUserData(data: UserResponse) {
        binding.apply {
            textName.text = data.name
            textDescription.text = data.bio
            circleImageView.load(data.avatar_url) {
                placeholder(R.drawable.profile)
            }
            tvFollowingNumber.text = data.following.toString()
            tvFollowersNumber.text = data.followers.toString()
            tvRepositoryNumber.text = (data.owned_private_repos + data.public_repos).toString()
        }
    }

    override fun loadGetRepositories(data: MutableList<RepositoryResponse>) {
        binding.apply {

            lifecycleScope.launchWhenCreated {
                repoViewModel.repoList.collectLatest { pagingData ->
                    repositoryAdapter.submitData(pagingData)
                }
            }

            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = repositoryAdapter
            }
        }
    }

    override fun responseError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    override fun checkInternet(): Boolean {
        return requireContext().isNetworkAvailable()
    }

    override fun internetError(hasInternet: Boolean) {
        if (hasInternet) {
            mainPresenter.callGetUserData("bearer ${sharedPref.accessToken}")
            mainPresenter.callGetRepositories("bearer ${sharedPref.accessToken}", 1)
            binding.imgNet.visibility = View.INVISIBLE

        } else {
            binding.imgNet.visibility = View.VISIBLE
        }
    }

    private fun logout() {
        sharedPref.accessToken = ""
        findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    activity?.finish()
                }
                doubleBackToExitPressedOnce = true
                Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
                Toast.makeText(requireContext(), "Double press to exit", Toast.LENGTH_SHORT).show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}