package com.dicoding.picodiploma.loginwithanimation.view.detail

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
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.Story
import com.dicoding.picodiploma.loginwithanimation.dummy.ListHistory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val repository: UserRepository) : ViewModel() {


    private val detailStory = MutableLiveData<ListHistory>()
    val DetailStory: LiveData<ListHistory> = detailStory

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getDetailStory(detailId: Long) {
        // Panggil metode untuk mendapatkan detail story dari repository
        viewModelScope.launch {
            val detail = repository.getListById(detailId)
            detailStory.postValue(detail)
        }


//    fun detailStories(token : String, id : String) {
//        val client = ApiConfig.getApiService(token).getDetail(id)
//        client.enqueue(object : Callback<DetailResponse> {
//            override fun onResponse(
//                call: Call<DetailResponse>,
//                response: Response<DetailResponse>
//            ) {
//                if (response.isSuccessful) {
//                    detailUser.value = response.body()
//                    detailStory.value = response.body()?.story
//                    } else {
//                        Log.e("detailStories", "onFailure: ${response.message()}")
//                    }
//
//            }
//            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//        })
//    }

    }
}