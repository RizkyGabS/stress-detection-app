package com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response

data class StoriesResponse(
    val listStory: List<ListStoryItem> = emptyList(),
    val error: Boolean,
    val message: String
)

data class ListStoryItem(
	val photoUrl: String,
	val createdAt: String,
	val name: String,
	val description: String,
	val lon: Double,
	val id: String,
	val lat: Double
)

