package com.ezatpanah.githubclient.ui.login

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ezatpanah.githubclient.BuildConfig
import com.ezatpanah.githubclient.R
import com.ezatpanah.githubclient.databinding.FragmentLoginBinding
import com.ezatpanah.githubclient.response.AccessTokenResponse
import com.ezatpanah.githubclient.utils.Constants.SCOPE
import com.ezatpanah.githubclient.utils.Constants.oauthLoginURL
import com.ezatpanah.githubclient.utils.SharedPref
import com.ezatpanah.githubclient.utils.isNetworkAvailable
import com.ezatpanah.githubclient.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import greyfox.rxnetwork.RxNetwork
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(), LoginContracts.View {

    lateinit var binding: FragmentLoginBinding
    lateinit var githubdialog: Dialog
    private lateinit var sharedPref: SharedPref
    lateinit var githubAuthURLFull: String
    var doubleBackToExitPressedOnce = false

    @Inject
    lateinit var loginPresenter: LoginPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val state = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())

        githubAuthURLFull = "$oauthLoginURL?client_id=${BuildConfig.CLIENT_ID}&scope=$SCOPE&redirect_uri=${BuildConfig.REDIRECT_URI}&state=$state"
        sharedPref = SharedPref(requireContext())

        binding.apply {

            val accessToken = sharedPref.accessToken
            if (accessToken!!.isNotEmpty()) {
                goToMainFragment()
            }
            binding.btnLogin.setOnClickListener {
                setupGithubWebviewDialog(githubAuthURLFull)
            }

            //CheckInternet
            RxNetwork.init(requireContext()).observe()
                .subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe { internetError(it.isConnected) }

        }


    }


    // Show Github login page in a dialog
    @SuppressLint("SetJavaScriptEnabled")
    fun setupGithubWebviewDialog(url: String) {
        githubdialog = Dialog(requireContext())
        val webView = WebView(requireContext())
        val settings = webView.settings

        clearCash(webView)

        settings.javaScriptEnabled = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        webView.webViewClient = GithubWebViewClient()
        webView.loadUrl(url)
        githubdialog.setContentView(webView)
        githubdialog.show()
    }

    // A client to know about WebView navigations
    // For API 21 and above
    @Suppress("OverridingDeprecatedMember")
    inner class GithubWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?,
        ): Boolean {
            if (request!!.url.toString().startsWith(BuildConfig.REDIRECT_URI)) {
                handleUrl(request.url.toString())
                // Close the dialog after getting the authorization code
                if (request.url.toString().contains("code=")) {
                    githubdialog.dismiss()
                }
                return true
            }
            return false
        }

        // For API 19 and below
        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith(BuildConfig.REDIRECT_URI)) {
                handleUrl(url)
                // Close the dialog after getting the authorization code
                if (url.contains("?code=")) {
                    githubdialog.dismiss()
                }
                return true
            }
            return false
        }

        // Check webview url for access token code or error
        private fun handleUrl(url: String) {
            val uri = Uri.parse(url)
            if (url.contains("code")) {
                val githubCode = uri.getQueryParameter("code") ?: ""
                loginPresenter.callGetAccessToken(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, githubCode)

            }
        }
    }

    override fun loadAccessToken(data: AccessTokenResponse) {
        binding.apply {
            sharedPref.accessToken = data.accessToken
            goToMainFragment()
        }
    }

    override fun responseError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    override fun checkInternet(): Boolean {
        return requireContext().isNetworkAvailable()
    }

    override fun internetError(hasInternet: Boolean) {
        binding.apply {
            if (hasInternet) {
                loginLayout.visible(true)
                networkLayout.visible(false)
            } else {
                networkLayout.visible(true)
                loginLayout.visible(false)
            }
        }
    }

    private fun goToMainFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
    }

    private fun clearCash(webView: WebView) {
        WebStorage.getInstance().deleteAllData()
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
        webView.clearCache(true)
        webView.clearFormData()
        webView.clearHistory()
        webView.clearSslPreferences()
    }

    override fun onDestroy() {
        super.onDestroy()
        loginPresenter.onStop()
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