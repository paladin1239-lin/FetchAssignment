package com.homework.fetchassignment.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homework.fetchassignment.data.dto.ItemsListItem
import com.homework.fetchassignment.data.local.Item
import com.homework.fetchassignment.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// DI where we also inject a repository.
@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: Repository,
                                                private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): ViewModel() {

    val TAG = "MainActivityViewModel"

    // Used state flow as a tool to recompose UI cased on the state in itemsData
    val itemsData: StateFlow<List<ItemsListItem>?>
        get() = _itemsData
    private val _itemsData: MutableStateFlow<
            List<ItemsListItem>?> = MutableStateFlow(null)

    init {
        getData()
    }

    fun getData(){
        viewModelScope.launch(ioDispatcher) {
            val items = repository.getData()?.filter {
                !it.name.isNullOrBlank()
            }?.sortedWith(
                compareBy<ItemsListItem>
                {
                    it.listId
                }.thenBy {
                    it.name
                }
            )
            // To add try catch, in case of no error which will return a list of items
            _itemsData.value = items?.let { items ->
                insertAllItems(items)
                items
            } ?: getItemsFromLocalFB()
            // In case of error such as problems with internet which will return null, so
            // data will be taken from local database if there are items there.
        }
    }

    private suspend fun insertAllItems(items: List<ItemsListItem>){
        items.forEach {
            try{
                repository.insertItem(Item(listId = it.listId,
                    name = it.name,
                    id = it.id))
            } catch (e: Exception){
                Log.d(TAG, "Exception: ${e.message}")
            }
        }
    }

    private suspend fun getItemsFromLocalFB(): List<ItemsListItem>?{
        return try {
            repository.getAllItems().map { ItemsListItem(
                listId = it.listId,
                name = it.name,
                id = it.id)
            }.sortedWith(
                compareBy<ItemsListItem>
                {
                    it.listId
                }.thenBy {
                    it.name
                }
            )
        } catch (e: Exception){
            Log.d(TAG, "Exception: ${e.message}")
            null
        }
    }


}