package com.dicoding.picodiploma.loginwithanimation.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import kotlinx.coroutines.launch
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.ErrorResponse
import com.google.gson.Gson

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<Boolean>()
    val registerResult: LiveData<Boolean> get() = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: MutableLiveData<String?> get() = _errorMessage

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = userRepository.registerUser(name, email, password)
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val registerResponse = response.body()
                    if (registerResponse != null) {
                        _registerResult.value = true
                    }
                } else {
                    _isLoading.value = false
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                    _registerResult.value = false
                    _errorMessage.value = errorResponse.message
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _registerResult.value = false
                _errorMessage.value = "Terjadi kesalahan: ${e.message}"
            }
        }
    }
}
