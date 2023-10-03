package com.example.smallnote.workManager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.smallnote.R
import com.example.smallnote.roomDatabase.AppDatabase
import com.example.smallnote.roomDatabase.Note
import com.example.smallnote.roomDatabase.NoteRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
                   GlobalScope.launch {
                            val response = apiInterface.postNote(
                                Note(
                                    note.note,
                                    note.latitude,
                                    note.longitude
                                )
                            )
                            if (response.isSuccessful) {
                                showNotification(
                                    "Post Completed",
                                    "The note was posted successfully"
                                )
                            } else {
                                showNotification("Post Failed", "Failed to post the note")
                            }
                        }
                }
            }
        } catch (e: Exception) {
            Log.d("PostNoteToFirebaseByRetrofitWorker", "doWorkException: ${e.message}")
        }
        return Result.success()
    }


    private fun showNotification(task: String, desc: String) {
        val channelId = "work_notifications"
        val notificationId = 1

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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