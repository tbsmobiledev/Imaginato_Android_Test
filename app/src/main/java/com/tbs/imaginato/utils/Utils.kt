@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.tbs.imaginato.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.Gravity
import android.view.Window
import android.widget.TextView
import com.tbs.imaginato.R
import com.tbs.imaginato.utils.listeners.Click
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.*

object Utils {

    private var dialogProgress: Dialog? = null

    fun showProgress(context: Context?) {
        if (dialogProgress == null) {
            dialogProgress = Dialog(context!!)
            Objects.requireNonNull(dialogProgress!!.window)?.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            dialogProgress!!.setContentView(R.layout.custom_progressbar)
            dialogProgress!!.setCanceledOnTouchOutside(false)
            dialogProgress!!.setCancelable(false)
            if (!dialogProgress!!.isShowing) {
                dialogProgress!!.show()
            }
        }
    }

    fun hideProgress() {
        if (dialogProgress != null && dialogProgress!!.isShowing) {
            dialogProgress!!.dismiss()
            dialogProgress!!.cancel()
            dialogProgress = null
        }
    }

    fun singleButtonDialog(
        context: Activity?,
        title: String,
        msg: String,
        btn_value: String,
        click: Click
    ) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_single_button)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val window: Window = dialog.window!!
        val wlp = window.attributes
        wlp.gravity = Gravity.CENTER
        window.attributes = wlp

        val mTvTitle: TextView = dialog.findViewById(R.id.mTvTitle)
        val mTvMSG: TextView = dialog.findViewById(R.id.mTvMSG)
        val mTvOK: TextView = dialog.findViewById(R.id.mTvOK)

        mTvTitle.text = title
        mTvMSG.text = msg
        mTvOK.text = btn_value

        mTvOK.setOnClickListener {
            click.onclick(0, "", "")
            dialog.dismiss()
        }
        dialog.show()
    }

    /**
     * Identify particular exception based on response.
     */
    fun errorHandlerModule(e: Throwable?, activity: Activity?) {
        activity?.let {
            when (e) {
                is HttpException -> {
                    val responseBody = (e).response()?.errorBody()
                    val s = getErrorMessage(responseBody)
                    if (!TextUtils.isEmpty(s)) {
                        singleButtonDialog(activity,
                            activity.resources.getString(R.string.app_name),
                            s,
                            activity.resources.getString(R.string.ok),
                            object : Click {
                                override fun onclick(position: Int, `object`: Any, text: String) {
                                }
                            })
                    } else {
                        singleButtonDialog(activity,
                            activity.resources.getString(R.string.app_name),
                            activity.resources.getString(R.string.alert_some_thing_wrong),
                            activity.resources.getString(R.string.ok),
                            object : Click {
                                override fun onclick(position: Int, `object`: Any, text: String) {
                                }
                            })

                    }
                }
                is SocketTimeoutException ->
                    singleButtonDialog(activity,
                        activity.resources.getString(R.string.app_name),
                        activity.resources.getString(R.string.alert_time_out),
                        activity.resources.getString(R.string.ok),
                        object : Click {
                            override fun onclick(position: Int, `object`: Any, text: String) {
                            }
                        })
                is IOException ->
                    singleButtonDialog(activity,
                        activity.resources.getString(R.string.app_name),
                        activity.resources.getString(R.string.alert_internet),
                        activity.resources.getString(R.string.ok),
                        object : Click {
                            override fun onclick(position: Int, `object`: Any, text: String) {
                            }
                        })
                else ->
                    singleButtonDialog(activity,
                        activity.resources.getString(R.string.app_name),
                        e?.message ?: activity.resources.getString(R.string.alert_server_error),
                        activity.resources.getString(R.string.ok),
                        object : Click {
                            override fun onclick(position: Int, `object`: Any, text: String) {
                            }
                        })
            }
        }
    }

    /**
     * Handle error message
     */
    private fun getErrorMessage(responseBody: ResponseBody?): String {
        var s = ""
        try {
            val jsonObject = JSONObject(responseBody?.string())
            if (jsonObject.has("resultsPage")) {
                val jsonObjectResultPage = JSONObject("resultsPage")
                if (jsonObjectResultPage.has("status")) {
                    if (jsonObjectResultPage.getString("status").equals("error", false)) {
                        if (jsonObjectResultPage.has("error")) {
                            val jsonObjectError = JSONObject("error")
                            if (jsonObjectError.has("message")) {
                                s = jsonObjectError.getString("message")
                            }
                        }
                    }
                }
            }
            return s
        } catch (e: Exception) {
            s = e.message ?: "Server Error"
            return s
        }

    }

    fun getRandomNumber(min: Int, max: Int): Int {
        return Random().nextInt(max - min + 1) + min
    }
}