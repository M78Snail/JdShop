package com.example.duxiaoming.jdshop.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.duxiaoming.jdshop.JDApplication
import com.example.duxiaoming.jdshop.activity.LoginActivity


abstract class BaseFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = createView(inflater, container, savedInstanceState)

        initToolBar()

        init(view)

        return view
    }


    fun initToolBar() {

    }


    abstract fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View

    abstract fun init(view: View)


    fun startActivity(intent: Intent, isNeedLogin: Boolean) {


        if (isNeedLogin) {

            val user = JDApplication.mInstance?.getUser()
            if (user != null) {
                super.startActivity(intent)
            } else {

                JDApplication.mInstance?.putIntent(intent)
                val loginIntent = Intent(activity, LoginActivity::class.java)
                super.startActivity(loginIntent)

            }

        } else {
            super.startActivity(intent)
        }

    }


}
