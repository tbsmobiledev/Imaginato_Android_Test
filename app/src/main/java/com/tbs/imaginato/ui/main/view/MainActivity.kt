package com.tbs.imaginato.ui.main.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room
import com.google.gson.JsonObject
import com.tbs.imaginato.R
import com.tbs.imaginato.data.api.ApiProvider
import com.tbs.imaginato.data.local.UserDatabase
import com.tbs.imaginato.data.local.UserModel
import com.tbs.imaginato.ui.base.BaseActivity
import com.tbs.imaginato.utils.Utils
import com.tbs.imaginato.utils.listeners.Click
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : BaseActivity() {

    private var activity: Activity = this
    private lateinit var mEtEmail: EditText
    private lateinit var mEtPassword: EditText
    private lateinit var mIvHide: ImageView
    private lateinit var mTvLogin: TextView
    private val compositeDisposable = CompositeDisposable()
    private lateinit var userDatabase: UserDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        setListener()
    }

    /**
     * For content initialization
     */
    private fun init() {
        userDatabase =
            Room.databaseBuilder(this, UserDatabase::class.java, "Imaginato").allowMainThreadQueries()
                .build()
        mEtEmail = findViewById(R.id.mEtEmail)
        mEtPassword = findViewById(R.id.mEtPassword)
        mIvHide = findViewById(R.id.mIvHide)
        mTvLogin = findViewById(R.id.mTvLogin)
        // rippleDrawable = (mTvLogin.background as RippleDrawable)
    }

    /**
     * For setting click event on screen
     */
    private fun setListener() {
        mTvLogin.setOnClickListener(onClickListener)
        mIvHide.setOnClickListener(onClickListener)
    }

    /**
     * For click event listner
     */
    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.mTvLogin -> {
                if (isValidation()) {
                   val arrayList = userDatabase.userDao().getCheckUser(mEtEmail.text.trim().toString())
                    if(arrayList.isEmpty()) {
                        userLogInAPI()
                    }else{
                        startActivity(Intent(activity,UserDetailsActivity::class.java))
                    }
                }
            }
            R.id.mIvHide -> {
                if (mEtPassword.inputType == InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                ) {
                    mEtPassword.inputType = InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_PASSWORD
                    mIvHide.setImageResource(R.mipmap.ic_hide)
                } else {
                    mEtPassword.inputType = InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    mIvHide.setImageResource(R.mipmap.ic_show)
                }
                mEtPassword.setSelection(mEtPassword.text.length)
            }
        }
    }

    /**
     * Login validation status
     */
    private fun isValidation(): Boolean {
        return when {
            mEtEmail.text.trim().isEmpty() -> {
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
            mEtPassword.text.trim().isEmpty() -> {
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
        jsonObject.addProperty("username", mEtEmail.text.trim().toString())
        jsonObject.addProperty("password", mEtPassword.text.trim().toString())

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

                        startActivity(Intent(activity,UserDetailsActivity::class.java))
                    }


                }, { t: Throwable? ->
                    activity.let {
                        Utils.hideProgress()
                        Utils.errorHandlerModule(t, it)
                    }
                })
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}
