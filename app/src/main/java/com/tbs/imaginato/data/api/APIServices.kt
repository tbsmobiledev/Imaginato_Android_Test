package com.tbs.imaginato.data.api

import com.google.gson.JsonObject
import com.tbs.imaginato.data.model.UserModel
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface APIServices {

    /**
     * POST API for User Login
     * */
    @POST(ApiConstant.APILOGIN)
    fun userLogin(@Body jsonObject: JsonObject): Observable<UserModel>

}