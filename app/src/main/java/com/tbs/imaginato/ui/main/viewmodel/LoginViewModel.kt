package com.tbs.imaginato.ui.main.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.google.gson.JsonObject
import com.tbs.imaginato.R
import com.tbs.imaginato.data.api.ApiConstant
import com.tbs.imaginato.data.api.ApiProvider
import com.tbs.imaginato.data.local.DatabaseConstant
import com.tbs.imaginato.data.local.UserDatabase
import com.tbs.imaginato.data.local.UserModel
import com.tbs.imaginato.utils.Utils
import com.tbs.imaginato.utils.listeners.Click
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

@SuppressLint("StaticFieldLeak")
class LoginViewModel(private val activity: Activity, private val userName: String, private val password: String, private val click: Click) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var userDatabase: UserDatabase =
        Room.databaseBuilder(activity, UserDatabase::class.java, DatabaseConstant.DATABASE_NAME).allowMainThreadQueries()
            .build()

    init {
        checkValidationAndLogin()
    }

    private fun checkValidationAndLogin() {
        if (isValidation()) {
            val arrayList = userDatabase.userDao().getCheckUser(userName)
            if(arrayList.isEmpty()) {
                userLogInAPI()
            } else {
                click.onclick(0,"", activity.resources.getString(R.string.done))
            }
        }
    }

    /**
     * Login validation status
     */
    private fun isValidation(): Boolean {
        return when {
            userName.isEmpty() -> {
                Utils.singleButtonDialog(activity,
                    activity.resources.getString(R.string.alert),
                    activity.resources.getString(R.string.enter_username),
                    activity.resources.getString(R.string.ok),
                    object : Click {
                        override fun onclick(position: Int, `object`: Any, text: String) {
                        }
                    })
                false
            }
            password.isEmpty() -> {
                Utils.singleButtonDialog(activity,
                    activity.resources.getString(R.string.alert),
                    activity.resources.getString(R.string.enter_password),
                    activity.resources.getString(R.string.ok),
                    object : Click {
                        override fun onclick(position: Int, `object`: Any, text: String) {
                        }
                    })
                false
            }
            else -> {
                true
            }
        }
    }

    /**
     * Login API integration
     */
    private fun userLogInAPI() {
        Utils.showProgress(activity)
        val jsonObject = JsonObject()
        jsonObject.addProperty(ApiConstant.KEY_LOGIN_USERNAME, userName)
        jsonObject.addProperty(ApiConstant.KEY_LOGIN_PASSWORD, password)

        compositeDisposable.add(
            ApiProvider.loginUser(activity)
                .userLogin(jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Utils.hideProgress()
                    val userdata = it
                    userdata.user?.let { user->
                        val name = user.userName
                        val id = user.userId
                        val mUserModel = UserModel(Utils.getRandomNumber(1,10000), id, name)
                        userDatabase.userDao().insertUser(mUserModel)
                        click.onclick(0,"", activity.resources.getString(R.string.done))
                    }

                }, { t: Throwable? ->
                    activity.let {
                        Utils.hideProgress()
                        Utils.errorHandlerModule(t, it)
                    }
                })
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

}