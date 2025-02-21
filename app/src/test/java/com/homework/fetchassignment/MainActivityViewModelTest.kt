package com.homework.fetchassignment

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.homework.fetchassignment.data.dto.ItemsList
import com.homework.fetchassignment.data.dto.ItemsListItem
import com.homework.fetchassignment.data.local.Item
import com.homework.fetchassignment.data.repository.Repository
import com.homework.fetchassignment.presentation.MainActivityViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

// Unit test using mockk
@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MainActivityViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // Execute stateflow update instantly.

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private val repository: Repository = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    @Test
    fun `Test get data items from remote and insertItem in local`() = runTest {
        coEvery { repository.getData() } returns getNotSortedItemListFromRemote()
        coEvery { repository.insertItem(any()) } just Runs
        coEvery { repository.getAllItems() } returns getNotSortedItemListFromRemote()
            .map {
                Item(
                    id = it.id,
                    name = it.name,
                    listId = it.listId
                )
            }
        mainActivityViewModel = MainActivityViewModel(repository, ioDispatcher = testDispatcher)
        advanceUntilIdle()
        val itemList = mainActivityViewModel.itemsData.value
        assert(!itemList.isNullOrEmpty())
        assert((itemList?.size ?: -1) > 0)
        assertEquals(correctSortedList.size, itemList?.size ?: -1)
        itemList?.forEachIndexed{ index, item ->
            assertEquals(correctSortedList.get(index).listId, item.listId)
            assertEquals(correctSortedList.get(index).name, item.name)
        }
        coVerify(atLeast = 1 ) { repository.insertItem(any()) }
    }


    @Test
    fun `Test get data items from remote and getDataFromLocalDatabase`() = runTest {
        coEvery { repository.getData() } returns null
        coEvery { repository.getAllItems() } returns getNotSortedItemListFromLocal()
            .map {
                Item(
                    id = it.id,
                    name = it.name,
                    listId = it.listId
                )
            }
        mainActivityViewModel = MainActivityViewModel(repository, ioDispatcher = testDispatcher)
        advanceUntilIdle()
        val itemList = mainActivityViewModel.itemsData.value
        assert(!itemList.isNullOrEmpty())
        assert((itemList?.size ?: -1) > 0)
        assertEquals(correctSortedList.size, itemList?.size ?: -1)
        itemList?.forEachIndexed{ index, item ->
            assertEquals(correctSortedList.get(index).listId, item.listId)
            assertEquals(correctSortedList.get(index).name, item.name)
        }
        coVerify(exactly = 1) { repository.getAllItems() }
    }


    @Test
    fun `Test get data items from remote and getDataFromLocalDatabase with exception`() = runTest{
        coEvery { repository.getData() } returns null
        coEvery { repository.getAllItems() } throws Exception()
        mainActivityViewModel = MainActivityViewModel(repository, ioDispatcher = testDispatcher)
        advanceUntilIdle()
        val itemList = mainActivityViewModel.itemsData.value
        assertEquals(null, itemList)
    }


    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    companion object{
        const val ONE = 1
        const val TWO = 2
        const val THREE = 3
        const val FOUR = 4
        const val FIVE = 5
        const val NAME = "NAME"

        private val ITEM_1 = ItemsListItem(
            id = ONE,
            name = "${NAME} ${ONE}",
            listId = ONE
        )
        private val ITEM_2 = ItemsListItem(
            id = TWO,
            name = "${NAME} ${TWO}",
            listId = ONE
        )
        private val ITEM_3 = ItemsListItem(
            id = THREE,
            name = "${NAME} ${THREE}",
            listId = ONE
        )
        private val ITEM_4 = ItemsListItem(
            id = FOUR,
            name = "${NAME} ${FOUR}",
            listId = THREE
        )
        private val ITEM_5 = ItemsListItem(
            id = FIVE,
            name = "${NAME} ${FIVE}",
            listId = THREE
        )
        private val ITEM_TO_FILTER_OUT = ItemsListItem(
            id = FIVE,
            name = "",
            listId = TWO
        )

        private val correctSortedList = listOf(ITEM_1, ITEM_2, ITEM_3, ITEM_4, ITEM_5)

        private fun getNotSortedItemListFromRemote(): ItemsList {
            val itemList = ItemsList()
            itemList.add(ITEM_5)
            itemList.add(ITEM_2)
            itemList.add(ITEM_1)
            itemList.add(ITEM_3)
            itemList.add(ITEM_TO_FILTER_OUT)
            itemList.add(ITEM_4)
            return itemList
        }

        // Locally we assume that incorrect items were filtered out
        private fun getNotSortedItemListFromLocal(): ItemsList {
            val itemList = ItemsList()
            itemList.add(ITEM_5)
            itemList.add(ITEM_2)
            itemList.add(ITEM_1)
            itemList.add(ITEM_3)
            itemList.add(ITEM_4)
            return itemList
        }
    }
}