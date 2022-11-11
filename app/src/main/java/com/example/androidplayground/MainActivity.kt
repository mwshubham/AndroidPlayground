package com.example.androidplayground

import android.os.Bundle
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantLock
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val reentrantLock = ReentrantLock(true)
    private val executor = Executors.newFixedThreadPool(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repeat(10) {
            executor.submit {
                specialFunction()
            }
        }
    }

    @WorkerThread
    private fun specialFunction() {
        reentrantLock.lock()
        Log.d(TAG, "specialFunction() [START] [Thread:${getThreadName()}]")
        val time = Random.nextLong(100, 300)
        Log.d(TAG, "Sleep Time: $time")
        Thread.sleep(Random.nextLong(100, 500))
        Log.d(TAG, "specialFunction() [END] [Thread:${getThreadName()}]")
        reentrantLock.unlock()
    }

    companion object {
        private const val TAG = "MainActivity"
    }

    private fun getThreadName() = Thread.currentThread().name
}