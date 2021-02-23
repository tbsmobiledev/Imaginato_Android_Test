package com.tbs.imaginato.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class UserModel(

	@field:SerializedName("errorMessage")
	val errorMessage: String = "",

	@field:SerializedName("errorCode")
	val errorCode: String = "",

	@field:SerializedName("user")
	val user: User? = null
)

@Entity
data class User(

	@PrimaryKey val id: Int,

	@field:SerializedName("userName")
	@ColumnInfo(name = "userName")
	val userName: String = "",

	@ColumnInfo(name = "userId")
	@field:SerializedName("userId")
	val userId: String = ""
)
