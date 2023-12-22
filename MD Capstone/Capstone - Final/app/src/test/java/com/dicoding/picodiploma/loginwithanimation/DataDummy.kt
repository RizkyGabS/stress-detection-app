package com.dicoding.picodiploma.loginwithanimation

import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "createdAt $i",
                "name $i",
                "descriptionm $i",
                i.toDouble(),
                "id $i",
                i.toDouble(),

            )
            items.add(story)
        }
        return items
    }
}