package com.homework.fetchassignment.di

import android.content.Context
import androidx.room.Room
import com.homework.fetchassignment.data.local.AppDatabase
import com.homework.fetchassignment.data.local.ItemDao
import com.homework.fetchassignment.data.repository.Repository
import com.homework.fetchassignment.data.repository.RepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Module {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase{
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideItemDao(database: AppDatabase): ItemDao{
        return database.itemDao()
    }

    // Used modules for providing a repository.
    @Provides
    fun provideRepository(itemDao: ItemDao): Repository{
        return RepositoryImp(itemDao)
    }


    // For unit test
    @Provides
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }


}