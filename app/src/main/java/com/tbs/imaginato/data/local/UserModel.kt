package com.tbs.imaginato.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DatabaseConstant.USER_TABLE_NAME)
data class UserModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = DatabaseConstant.KEY_USER_ID)
    val userId: String,

    @ColumnInfo(name = DatabaseConstant.KEY_USER_NAME)
    val userName: String

)