package com.homework.fetchassignment.data.repository

import android.util.Log
import com.homework.fetchassignment.data.dto.ItemsList
import com.homework.fetchassignment.data.local.Item
import com.homework.fetchassignment.data.local.ItemDao
import com.homework.fetchassignment.data.remote.ApiClient
import javax.inject.Inject

class RepositoryImp @Inject constructor(private val itemDao: ItemDao): Repository {
    val TAG = "RepositoryImp"

    override suspend fun getData(): ItemsList? {
        // Performing an API call, return null in case of an error/exception.
        return try {
            val response = ApiClient.apiService.getData()
            if(response.isSuccessful){
                response.body()
            } else{
                Log.d(TAG, "Response error: ${response.message()}")
                null
            }
        } catch (e: Exception){
            Log.d(TAG, "Exception: ${e.message}")
            null
        }
    }

    override suspend fun insertItem(item: Item) {
        itemDao.insertItem(item)
    }

    override suspend fun getAllItems(): List<Item> {
        return itemDao.getAllItems()
    }
}