package com.tbs.imaginato.ui.main.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.tbs.imaginato.R
import com.tbs.imaginato.ui.base.BaseActivity
import com.tbs.imaginato.ui.main.viewmodel.LoginViewModel
import com.tbs.imaginato.utils.listeners.Click

class MainActivity : BaseActivity() {

    private var activity: Activity = this
    private lateinit var mEtEmail: EditText
    private lateinit var mEtPassword: EditText
    private lateinit var mIvHide: ImageView
    private lateinit var mTvLogin: TextView

    private lateinit var loginViewModel: LoginViewModel


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
        mEtEmail = findViewById(R.id.mEtEmail)
        mEtPassword = findViewById(R.id.mEtPassword)
        mIvHide = findViewById(R.id.mIvHide)
        mTvLogin = findViewById(R.id.mTvLogin)
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

                loginViewModel = LoginViewModel(
                    activity,
                    mEtEmail.text.trim().toString(),
                    mEtPassword.text.trim().toString(),
                    object : Click {
                        override fun onclick(position: Int, `object`: Any, text: String) {
                            startActivity(Intent(activity, UserDetailsActivity::class.java))
                        }

                    })

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

}
