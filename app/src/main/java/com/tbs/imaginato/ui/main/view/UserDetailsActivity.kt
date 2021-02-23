package com.tbs.imaginato.ui.main.view

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.tbs.imaginato.R
import com.tbs.imaginato.data.local.UserDatabase
import com.tbs.imaginato.data.local.UserModel
import com.tbs.imaginato.ui.base.BaseActivity
import com.tbs.imaginato.ui.main.adapter.UserAdapter

class UserDetailsActivity : BaseActivity() {

    private lateinit var userDatabase: UserDatabase
    private lateinit var mRecyclerview: RecyclerView

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
            Room.databaseBuilder(this, UserDatabase::class.java, "Imaginato")
                .allowMainThreadQueries()
                .build()

        mRecyclerview = findViewById(R.id.mRecyclerview)
        mRecyclerview.layoutManager = LinearLayoutManager(this)

    }

    /**
     * Bind data for User
     */
    private fun bindData() {
        val userList: List<UserModel> = userDatabase.userDao().getUser()

        if (userList.isNotEmpty()) {
            val adapter = UserAdapter(userList)
            mRecyclerview.adapter = adapter
            for (i in userList.indices) {
                Log.e(MainActivity::class.simpleName, "Id: ${userList[i].id}")
                Log.e(MainActivity::class.simpleName, "User Id: ${userList[i].userId}")
                Log.e(
                    MainActivity::class.simpleName,
                    "User Name: ${userList[i].userName}"
                )
            }
        }
    }

}