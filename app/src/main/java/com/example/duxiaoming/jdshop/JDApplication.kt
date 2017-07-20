package com.example.duxiaoming.jdshop

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * Created by duxiaoming on 2017/7/19.
 * blog:m78snail.com
 * description:
 */
class JDApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}