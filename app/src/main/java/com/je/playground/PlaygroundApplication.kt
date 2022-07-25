package com.je.playground

import android.app.Application
import com.je.playground.database.AppDatabase

class PlaygroundApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Graph.provide(this)
    }
}