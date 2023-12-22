package com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error")
    val error: Boolean? = null,
    @SerializedName("message")
    val message: String? = null
)