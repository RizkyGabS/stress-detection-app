package com.dicoding.picodiploma.loginwithanimation.data.retrofit

import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.DetailResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.FileUploadResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.StoriesResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun signup(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun signin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET("stories")
    fun getStories() : Call<StoriesResponse>

    @GET("stories")
    fun getStoriesWithLocation(
        @Query("location") location : Int = 1,
    ): Call<StoriesResponse>

    @GET("stories/{id}")
    fun getDetail(
        @Path("id") id: String
    ) : Call<DetailResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Part file: MultipartBody.Part,
//        @Part("description") description: RequestBody,
    ): Call<FileUploadResponse>
}