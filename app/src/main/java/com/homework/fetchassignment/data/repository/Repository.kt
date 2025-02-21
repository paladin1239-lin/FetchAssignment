package com.homework.fetchassignment.data.repository

import com.homework.fetchassignment.data.dto.ItemsList
import com.homework.fetchassignment.data.local.Item

//import com.homework.fetchassignment.data.local.Item

interface Repository {
    // For remote call
    suspend fun getData(): ItemsList?

    // For local calls
    suspend fun insertItem(item: Item)
    suspend fun getAllItems(): List<Item>


}