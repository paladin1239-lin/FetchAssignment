package com.homework.fetchassignment.data.dto

class ItemsList : ArrayList<ItemsListItem>()

data class ItemsListItem(
    val id: Int,
    val listId: Int,
    val name: String
)