package com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("loginResult")
	val loginResult: LoginResult,

	@field:SerializedName("message")
	val message: String
)

data class LoginResult(

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("email")
	val email: String
)
