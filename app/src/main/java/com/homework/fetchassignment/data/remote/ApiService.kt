package com.homework.fetchassignment.data.remote

import com.homework.fetchassignment.data.dto.ItemsList
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("hiring.json")
    suspend fun getData(): Response<ItemsList>
}
