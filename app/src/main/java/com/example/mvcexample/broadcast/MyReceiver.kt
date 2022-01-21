package com.example.mvcexample.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi

class MyReceiver: BroadcastReceiver() {
    private val TAG = "MyReceiver"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        val taskName = intent?.getStringExtra("taskName")
        Toast.makeText(context, "$taskName -> should be done", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "onReceive: $taskName")
    }
}