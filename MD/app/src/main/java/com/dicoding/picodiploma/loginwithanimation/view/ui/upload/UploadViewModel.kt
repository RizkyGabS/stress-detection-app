package com.dicoding.picodiploma.loginwithanimation.view.ui.upload

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.FileUploadResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadViewModel(private val repository: UserRepository) : ViewModel() {

    private val _uploadResult = MutableLiveData<Boolean>()
    val uploadResult: LiveData<Boolean> get() = _uploadResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

//    fun uploaddata(token : String,imageFile: File, quest1: Int, quest2: Int, quest3: Int, quest4: Int, quest5: Int, quest6: Int, quest7: Int, quest8: Int, quest9 : Int, quest10 : Int) {
//        _isLoading.value = true
//        val quest = "$quest1 $quest2 $quest3 $quest4 $quest5 $quest6 $quest7 $quest8 $quest9 $quest10"
//        val requestBody = quest.toRequestBody("text/plain".toMediaType())
//        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
//        val multipartBody = MultipartBody.Part.createFormData(
//            "photo",
//            imageFile.name,
//            requestImageFile)
//        val client = ApiConfig.getApiService(token).uploadImage(multipartBody, requestBody)
//        client.enqueue(object : Callback<FileUploadResponse> {
//            override fun onResponse(
//                call: Call<FileUploadResponse>,
//                response: Response<FileUploadResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null) {
//                        _uploadResult.value = true
//                        _isLoading.value = false
//                    } else {
//                        _uploadResult.value = false
//                        _isLoading.value = false
//                        Log.e("Upload", "onFailure: ${response.message()}")
//                    }
//                }
//            }
//            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
//                Log.e("Upload", "onFailure: ")
//            }
//        })
//    }

    fun uploaddata(imageFile: File) {
        _isLoading.value = true
//        val quest = "$quest1 $quest2 $quest3 $quest4 $quest5 $quest6 $quest7 $quest8 $quest9 $quest10"
//        val requestBody = quest.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile)
        val client = ApiConfig.getApiService().uploadImage(multipartBody)
        client.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _uploadResult.value = true
                        _isLoading.value = false
                    } else {
                        _uploadResult.value = false
                        _isLoading.value = false
                        Log.e("Upload", "onFailure: ${response.message()}")
                    }
                }
            }
            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                Log.e("Upload", "onFailure: ")
            }
        })
    }
}