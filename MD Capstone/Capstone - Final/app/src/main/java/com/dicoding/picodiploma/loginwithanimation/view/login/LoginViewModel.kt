package com.dicoding.picodiploma.loginwithanimation.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> get() = _loginResult

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: MutableLiveData<String?> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.loginUser(email, password)
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        repository.saveSession(UserModel(loginResponse.loginResult.email))
                        _loginResult.value = true
                    }
                } else {
                    _isLoading.value = false
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                    _loginResult.value = false
                    _errorMessage.value = errorResponse.message
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _loginResult.value = false
                _errorMessage.value = "Terjadi kesalahan: ${e.message}"
            }
        }
    }
}