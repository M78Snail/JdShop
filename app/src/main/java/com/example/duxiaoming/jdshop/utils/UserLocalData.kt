package com.example.duxiaoming.jdshop.utils

import android.content.Context
import android.text.TextUtils
import com.example.duxiaoming.jdshop.Contants
import com.example.duxiaoming.jdshop.bean.User


/**
 * ProjectName:YayaShop
 * Autor： [菜鸟窝](http://www.cniao5.com)
 * Description：
 *
 *
 * 菜鸟窝是一个只专注做Android开发技能的在线学习平台，课程以实战项目为主，对课程与服务”吹毛求疵”般的要求，打造极致课程，是菜鸟窝不变的承诺
 */
object UserLocalData {


    fun putUser(context: Context, user: User) {


        val user_json = JSONUtil.toJSON(user)
        PreferencesUtils.putString(context, Contants.USER_JSON, user_json)

    }

    fun putToken(context: Context, token: String) {

        PreferencesUtils.putString(context, Contants.TOKEN, token)
    }


    fun getUser(context: Context): User? {

        val user_json = PreferencesUtils.getString(context, Contants.USER_JSON, "")
        if (!TextUtils.isEmpty(user_json)) {

            return JSONUtil.fromJson(user_json, User::class.java)
        }
        return null
    }

    fun getToken(context: Context): String {

        return PreferencesUtils.getString(context, Contants.TOKEN, "")

    }


    fun clearUser(context: Context) {


        PreferencesUtils.putString(context, Contants.USER_JSON, "")

    }

    fun clearToken(context: Context) {

        PreferencesUtils.putString(context, Contants.TOKEN, "")
    }


}
