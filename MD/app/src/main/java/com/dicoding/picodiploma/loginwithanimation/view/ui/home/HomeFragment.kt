package com.dicoding.picodiploma.loginwithanimation.view.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.FragmentHomeBinding
import com.dicoding.picodiploma.loginwithanimation.dummy.ListHistory
import com.dicoding.picodiploma.loginwithanimation.utils.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.LayananActivity
import com.dicoding.picodiploma.loginwithanimation.view.adapter.LoadingStateAdapter
import com.dicoding.picodiploma.loginwithanimation.view.adapter.StoryAdapter
import com.dicoding.picodiploma.loginwithanimation.view.detail.DetailActivity
import com.dicoding.picodiploma.loginwithanimation.view.login.LoginActivity
import com.dicoding.picodiploma.loginwithanimation.view.ui.TipsActivity
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvItem.layoutManager = layoutManager
        adapter = StoryAdapter()
        binding.rvItem.adapter = adapter
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finishAffinity()
        }

        binding.btnTrick.setOnClickListener {
            val intent = Intent(requireActivity(), TipsActivity::class.java)
            startActivity(intent)
        }

        binding.btnKonsul.setOnClickListener {
            val intent = Intent(requireActivity(), LayananActivity::class.java)
            startActivity(intent)
        }

        adapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListHistory) {
                Intent(requireContext(), DetailActivity::class.java).also {
                    it.putExtra("getdetailUser", data.history.id)
                    startActivity(it)
                }
            }
        })

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(requireContext(), WelcomeActivity::class.java))
                requireActivity().finish()
            } else {
//                viewModel.loadStories(user.token)
//                viewModel.stories.observe(viewLifecycleOwner) { list ->
//                    getStory(list)
//                }
                viewModel.getAllList().onEach { drakorList ->
                    getStory(drakorList)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
                displayname(user)
            }
        }

//        setupView()

        return root
    }

    private fun displayname(user: UserModel) {
        val usernameWithoutDomain = user.email.substringBefore('@')
        binding.tvName.text = "Hi $usernameWithoutDomain"

    }
    private fun getStory(list: List<ListHistory>?) {
        val adapter = StoryAdapter()
        adapter.submitList(list)
        binding.rvItem.adapter = adapter
    }

//    private fun getData() {
//        adapter = StoryAdapter()
//        binding.rvItem.adapter = adapter.withLoadStateFooter(
//            footer = LoadingStateAdapter {
//                adapter.retry()
//            }
//        )
//        viewModel.stories.observe(viewLifecycleOwner, { data ->
//            viewLifecycleOwner.lifecycleScope.launch {
//                adapter.submitData(data)
//            }
//        })
//    }


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

    override fun onResume() {
        super.onResume()
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(requireContext(), WelcomeActivity::class.java))
                requireActivity().finish()
            } else {
//                viewModel.loadStories(user.token)
//                viewModel.stories.observe(viewLifecycleOwner) { list ->
//                    getStory(list)
//                }
                viewModel.getAllList().onEach { drakorList ->
                    getStory(drakorList)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
                displayname(user)
            }
        }
    }



}