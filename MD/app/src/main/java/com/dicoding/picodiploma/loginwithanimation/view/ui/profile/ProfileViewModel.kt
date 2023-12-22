package com.dicoding.picodiploma.loginwithanimation.view.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.DetailResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.LoginResult
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.Story
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}