package com.tbs.imaginato.data.api

import com.google.gson.JsonObject
import com.tbs.imaginato.data.model.UserModel
import io.reactivex.Observable

class APIRepository(private val apiService: APIServices) {

    /**
     * User Login.
     * */
    fun userLogin(jsonObject: JsonObject): Observable<UserModel> {
        return apiService.userLogin(jsonObject)
    }

}