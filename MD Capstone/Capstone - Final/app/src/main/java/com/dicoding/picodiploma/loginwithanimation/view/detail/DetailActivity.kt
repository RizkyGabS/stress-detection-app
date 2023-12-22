package com.dicoding.picodiploma.loginwithanimation.view.detail

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.Story
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailBinding
import com.dicoding.picodiploma.loginwithanimation.dummy.FakeData
import com.dicoding.picodiploma.loginwithanimation.dummy.ListHistory
import com.dicoding.picodiploma.loginwithanimation.utils.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getLongExtra("getdetailUser", 0)
//        val index = 0
//        if (index >= 0 && index < FakeData.dummy.size) {
//            val idFromFakeData = FakeData.dummy[index].id
//
//
//        }
        viewModel.getSession().observe(this) {
            viewModel.getDetailStory(id)
            viewModel.DetailStory.observe(this) { detail ->
                getDetail(detail)
            }
        }
        setupView()
        playAnimation()
        }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun getDetail(detail: ListHistory) {
        binding.descname.text = detail.history.facialTest
        binding.desctext.text = detail.history.stressLvl
        Glide.with(this@DetailActivity)
            .load(detail.history.image)
            .centerCrop()
            .into(binding.descimg)
    }

    private fun playAnimation() {
        val image = ObjectAnimator.ofFloat(binding.descimg, View.ALPHA, 1f).setDuration(100)
        val name =
            ObjectAnimator.ofFloat(binding.descname, View.ALPHA, 1f).setDuration(100)
        val desc =
            ObjectAnimator.ofFloat(binding.desctext, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                image,
                name,
                desc
            )
            startDelay = 100
        }.start()
    }

}
