package com.dicoding.picodiploma.loginwithanimation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.picodiploma.loginwithanimation.R

class LayananActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layanan)
        supportActionBar?.hide()
    }
}