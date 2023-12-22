package com.dicoding.picodiploma.loginwithanimation.data

import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.dummy.FakeData
import com.dicoding.picodiploma.loginwithanimation.dummy.ListHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import retrofit2.Response


class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
)

{
    private val history = mutableListOf<ListHistory>()

    init {
        if (history.isEmpty()) {
            FakeData.dummy.forEach {
                history.add(ListHistory(it))
            }
        }
    }

    fun getAllList(): Flow<List<ListHistory>> {
        return flowOf(history)
    }

    fun getListById(detailId: Long): ListHistory {
        return history.first {
            it.history.id == detailId
        }
    }

    suspend fun registerUser(name: String, email: String, password: String): Response<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            apiService.signup(name, email, password)
        }
    }

    suspend fun loginUser(email: String, password: String): Response<LoginResponse> {
        return withContext(Dispatchers.IO) {
            apiService.signin(email, password)
        }
    }

//    fun getStories(): LiveData<PagingData<ListStoryItem>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 1,
//                initialLoadSize = 20
//            ),
//            pagingSourceFactory = {
//                PagingSource(apiService)
//            }
//        ).liveData
//    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        var INSTANCE: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserRepository(userPreference, apiService)
            }.also { INSTANCE = it }
    }
}