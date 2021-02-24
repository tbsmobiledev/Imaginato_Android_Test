package com.tbs.imaginato.ui.main.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.ViewModel
import com.tbs.imaginato.R
import com.tbs.imaginato.data.local.UserDatabase
import com.tbs.imaginato.data.local.UserModel
import com.tbs.imaginato.utils.listeners.Click

@SuppressLint("StaticFieldLeak")
class UserDetailsViewModel(private val activity: Activity, private val userDatabase: UserDatabase, private val click: Click) : ViewModel(){

    init {
        fetchUserData()
    }

    private fun fetchUserData() {
        val userList: List<UserModel> = userDatabase.userDao().getUser()
        if (userList.isNotEmpty()){
            click.onclick(0, userList, activity.resources.getString(R.string.done))
        }
    }
}