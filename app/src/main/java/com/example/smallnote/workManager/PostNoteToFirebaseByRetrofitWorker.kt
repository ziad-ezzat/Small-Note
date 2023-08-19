package com.example.smallnote.workManager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.smallnote.R
import com.example.smallnote.roomDatabase.AppDatabase
import com.example.smallnote.roomDatabase.Note
import com.example.smallnote.roomDatabase.NoteRepo
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostNoteToFirebaseByRetrofitWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams)
{
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://test-13589-default-rtdb.firebaseio.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var apiInterface: NoteApiService = retrofit.create(NoteApiService::class.java)

    override fun doWork(): Result {

        val noteRepository = NoteRepo(AppDatabase.getInstance(applicationContext).noteDao())

        val notes = runBlocking { noteRepository.allNotes.firstOrNull() }

        try{
            if (notes != null) {
                for (note in notes) {
                    val call = apiInterface.postNote(note)
                    call.enqueue(object : Callback<Note> {
                        override fun onResponse(call: Call<Note>, response: Response<Note>) {
                            if (response.isSuccessful) {
                                showNotification("Post Completed 1", "The note was posted successfully")
                            } else {
                                showNotification("Post Failed 2", "Failed to post the note")
                            }
                        }

                        override fun onFailure(call: Call<Note>, t: Throwable) {
                            showNotification("Post Failed 3", "Failed to post the note")
                        }
                    })
                }
            }
        } catch (e: Exception) {
            showNotification("Error 4", "Failed to fetch notes")
            val call = apiInterface.postNote(Note("test"))
            call.enqueue(object : Callback<Note> {
                override fun onResponse(call: Call<Note>, response: Response<Note>) {
                    if (response.isSuccessful) {
                        showNotification("Post Completed 1", "The note was posted successfully")
                    } else {
                        showNotification("Post Failed 2", "Failed to post the note")
                    }
                }

                override fun onFailure(call: Call<Note>, t: Throwable) {
                    showNotification("Post Failed 3", "Failed to post the note")
                }
            })
        }

        return Result.success()
    }


    private fun showNotification(task: String, desc: String) {
        val channelId = "work_notifications"
        val notificationId = 1

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel for devices running Android 8.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Work Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(task)
            .setContentText(desc)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}