package com.example.duxiaoming.jdshop.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.example.duxiaoming.jdshop.JDApplication


open class BaseActivity : AppCompatActivity() {


    fun startActivity(intent: Intent, isNeedLogin: Boolean) {


        if (isNeedLogin) {

            val user = JDApplication.mInstance?.getUser()
            if (user != null) {
                super.startActivity(intent)
            } else {

                JDApplication.mInstance?.putIntent(intent)
                val loginIntent = Intent(this, LoginActivity::class.java)
                super.startActivity(loginIntent)

            }

        } else {
            super.startActivity(intent)
        }

    }
}
