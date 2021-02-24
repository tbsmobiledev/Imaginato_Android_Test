package com.tbs.imaginato.ui.main.view

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.tbs.imaginato.R
import com.tbs.imaginato.data.local.DatabaseConstant
import com.tbs.imaginato.data.local.UserDatabase
import com.tbs.imaginato.data.local.UserModel
import com.tbs.imaginato.ui.base.BaseActivity
import com.tbs.imaginato.ui.main.adapter.UserAdapter
import com.tbs.imaginato.ui.main.viewmodel.UserDetailsViewModel
import com.tbs.imaginato.utils.listeners.Click

@Suppress("UNCHECKED_CAST")
class UserDetailsActivity : BaseActivity() {

    private val activity : Activity = this
    private lateinit var userDatabase: UserDatabase
    private lateinit var mRecyclerview: RecyclerView
    private lateinit var userDetailsViewModel: UserDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        init()
        bindData()
    }

    /**
     * For content initialization
     */
    private fun init() {
        userDatabase =
            Room.databaseBuilder(this, UserDatabase::class.java, DatabaseConstant.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()

        mRecyclerview = findViewById(R.id.mRecyclerview)
        mRecyclerview.layoutManager = LinearLayoutManager(this)

    }

    /**
     * Bind data for User
     */
    private fun bindData() {
        userDetailsViewModel = UserDetailsViewModel(activity,userDatabase, object: Click {
            override fun onclick(position: Int, `object`: Any, text: String) {

                val userList: List<UserModel> = `object` as List<UserModel>
                val adapter = UserAdapter(userList)
                mRecyclerview.adapter = adapter
            }
        })

    }

}