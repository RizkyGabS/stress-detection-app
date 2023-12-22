package com.dicoding.picodiploma.loginwithanimation.view.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.StoriesResponse
import com.dicoding.picodiploma.loginwithanimation.dummy.ListHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel(private val repository: UserRepository) : ViewModel() {


    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getAllList(): Flow<List<ListHistory>> {
        return repository.getAllList()
    }


//    private val _stories = MutableLiveData<List<ListStoryItem>?>()
//    val stories: LiveData<List<ListStoryItem>?> = _stories
//
//    fun loadStories(token : String) {
//        val client = ApiConfig.getApiService(token).getStories()
//        client.enqueue(object : Callback<StoriesResponse> {
//            override fun onResponse(
//                call: Call<StoriesResponse>,
//                response: Response<StoriesResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null) {
//                        val listStoryItems = responseBody.listStory
//                        _stories.value = listStoryItems
//                    } else {
//                        Log.e("Stories", "onFailure: ${response.message()}")
//                    }
//                }
//            }
//            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
//                Log.e("Main", "onFailure: ")
//            }
//        })
//    }

//    val stories: LiveData<PagingData<ListStoryItem>> = repository.getStories().cachedIn(viewModelScope)



}