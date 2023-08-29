package com.example.smallnote.di

import android.app.Application
import android.content.Context
import com.example.smallnote.roomDatabase.AppDatabase
import com.example.smallnote.roomDatabase.NoteDao
import com.example.smallnote.roomDatabase.NoteRepo
import com.example.smallnote.roomDatabase.NoteViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun getAppDatabase(context: Application): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun getNoteDao(appDatabase: AppDatabase) = appDatabase.noteDao()

    @Provides
    @Singleton
    fun getNoteRepository(noteDao: NoteDao) = NoteRepo(noteDao)

    @Provides
    @Singleton
    fun getNoteViewModelFactory(noteRepo: NoteRepo) = NoteViewModelFactory(noteRepo)
}