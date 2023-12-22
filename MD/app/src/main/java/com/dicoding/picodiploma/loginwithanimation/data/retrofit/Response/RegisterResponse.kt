package com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("registerResult")
	val registerResult: RegisterResult
)

data class RegisterResult(

	@field:SerializedName("email")
	val email: String
)
