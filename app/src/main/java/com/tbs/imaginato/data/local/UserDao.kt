package com.tbs.imaginato.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    fun insertUser(userModel: UserModel)

    @Query("SELECT * FROM Users")
    fun getUser(): List<UserModel>

    @Query("SELECT * FROM Users WHERE UserName=:userName")
    fun getCheckUser(userName: String): List<UserModel>

}