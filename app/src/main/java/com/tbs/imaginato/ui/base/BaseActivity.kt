package com.tbs.imaginato.ui.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tbs.imaginato.R

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }
}