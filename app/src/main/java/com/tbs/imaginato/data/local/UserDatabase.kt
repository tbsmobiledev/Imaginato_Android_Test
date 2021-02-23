package com.tbs.imaginato.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserModel::class], exportSchema = false, version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}