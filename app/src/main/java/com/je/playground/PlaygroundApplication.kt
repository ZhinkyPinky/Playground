package com.je.playground

import android.app.Application

class PlaygroundApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        GraphV2.provide(this)
    }
}