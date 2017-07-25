package com.example.duxiaoming.jdshop

import android.app.Application
import android.content.Context
import android.content.Intent
import com.example.duxiaoming.jdshop.bean.User
import com.example.duxiaoming.jdshop.utils.UserLocalData
import com.facebook.drawee.backends.pipeline.Fresco


/**
 * Created by duxiaoming on 2017/7/19.
 * blog:m78snail.com
 * description:
 */
class JDApplication : Application() {


    private var user: User? = null

    companion object {
        var mInstance: JDApplication? = null
            private set
    }


    override fun onCreate() {
        super.onCreate()
        mInstance = this
        initUser()
        Fresco.initialize(this)
    }

    private fun initUser() {

        this.user = UserLocalData.getUser(this)
    }


    fun getUser(): User? {

        return user
    }


    fun putUser(user: User, token: String) {
        this.user = user
        UserLocalData.putUser(this, user)
        UserLocalData.putToken(this, token)
    }

    fun clearUser() {
        this.user = null
        UserLocalData.clearUser(this)
        UserLocalData.clearToken(this)


    }


    fun getToken(): String {

        return UserLocalData.getToken(this)
    }


    private var intent: Intent? = null
    fun putIntent(intent: Intent) {
        this.intent = intent
    }

    fun getIntent(): Intent? {
        return this.intent
    }

    fun jumpToTargetActivity(context: Context) {

        context.startActivity(intent)
        this.intent = null
    }

}