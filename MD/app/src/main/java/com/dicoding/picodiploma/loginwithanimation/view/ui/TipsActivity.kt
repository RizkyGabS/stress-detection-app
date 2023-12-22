package com.dicoding.picodiploma.loginwithanimation.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.picodiploma.loginwithanimation.R

class TipsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)

        supportActionBar?.hide()
    }
}