/*
*JSONUtil.java
*Created on 2014-9-29 上午9:54 by Ivan
*Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
*http://www.cniao5.com
*/
package com.example.duxiaoming.jdshop.utils

import com.google.gson.GsonBuilder
import java.lang.reflect.Type


object JSONUtil {


    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()


    fun <T> fromJson(json: String, clz: Class<T>): T {

        return gson.fromJson(json, clz)
    }


    fun <T> fromJson(json: String, type: Type): T {

        return gson.fromJson<T>(json, type)
    }


    fun toJSON(`object`: Any): String {

        return gson.toJson(`object`)
    }

}
