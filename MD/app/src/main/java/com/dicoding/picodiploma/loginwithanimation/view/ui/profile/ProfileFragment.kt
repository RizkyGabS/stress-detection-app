package com.dicoding.picodiploma.loginwithanimation.view.ui.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.Story
import com.dicoding.picodiploma.loginwithanimation.databinding.FragmentProfileBinding
import com.dicoding.picodiploma.loginwithanimation.utils.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.adapter.StoryAdapter
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity
import kotlin.math.log

class ProfileFragment : Fragment() {
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(requireContext(), WelcomeActivity::class.java))
                requireActivity().finish()
            } else {
                displayemail(user)
            }
        }

        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }

        setupView()

        return root
    }

    private fun displayemail(user: UserModel) {
        binding.textName.text = user.email

    }


    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }





}