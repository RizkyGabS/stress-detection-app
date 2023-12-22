package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySignupBinding
import com.dicoding.picodiploma.loginwithanimation.utils.ViewModelFactory


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var factory: ViewModelFactory
    private lateinit var signupViewModel: SignUpViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        factory = ViewModelFactory.getInstance(this)
        signupViewModel = ViewModelProvider(this, factory).get(SignUpViewModel::class.java)

        setupView()
        setupAction()
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

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val name = binding.nameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Mohon data dilengkapi", Toast.LENGTH_LONG).show()
            } else {
                signupViewModel.isLoading.observe(this, Observer { isLoading ->
                    if (isLoading) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }
                })
                signupViewModel.register(name, email, password)
                signupViewModel.registerResult.observe(this, Observer { success ->
                    if (success) {
                        showSuccessDialog()
                    } else {
                        showErrorDialog(signupViewModel.errorMessage.value)
                    }
                })
            }
        }
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Signup")
            setMessage("Akun sudah berhasil dibuat")
            setPositiveButton("Lanjut") { _, _ ->
                finish()
            }
            create()
            show()
        }
    }

    private fun showErrorDialog(errorMessage: String?) {
        AlertDialog.Builder(this).apply {
            setTitle("Error")
            setMessage(errorMessage ?: "Terjadi kesalahan. Mohon coba lagi nanti.")
            setPositiveButton("Ok") { _, _ ->
                finish()
            }
            create()
            show()
        }
    }



    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }
}