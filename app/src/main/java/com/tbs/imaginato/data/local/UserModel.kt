package com.tbs.imaginato.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class UserModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "UserId")
    val userId: String,

    @ColumnInfo(name = "UserName")
    val userName: String

)