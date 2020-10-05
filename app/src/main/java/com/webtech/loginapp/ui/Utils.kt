package com.webtech.loginapp.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.webtech.loginapp.data.network.Resource
import com.webtech.loginapp.ui.auth.LoginFragment
import com.webtech.loginapp.ui.base.BaseFragment

fun<A : Activity> Activity.startNewActivity(activity: Class<A>){
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun View.visible(isVisible: Boolean){
    visibility = if(isVisible) View.VISIBLE else View.GONE
}

fun View.enable(enabled: Boolean){
    isEnabled = enabled
    alpha = if(enabled) 1f else 0.5f
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun ProgressBar.show() {
    // progress_bar.visibility = View.VISIBLE
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    // progress_bar.visibility = View.GONE
    visibility = View.GONE
}



fun View.snackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}

fun Fragment.handleApiError(
    failure: Resource.Failure,
    retry: (() -> Unit)? = null
) {
    when {
        failure.isNetworkError -> requireView().snackbar(
            "Please check your internet connection",
            retry
        )
        failure.errorCode == 401 -> {
            if (this is LoginFragment) {
                requireView().snackbar("You've entered incorrect email or password")
            } else {
                (this as BaseFragment<*, *, *>).logout()
            }
        }
        else -> {
            val error = failure.errorBody?.string().toString()
            requireView().snackbar(error)
        }
    }
}
