package com.example.duxiaoming.jdshop.http

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.example.duxiaoming.jdshop.JDApplication
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.squareup.okhttp.*
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Created by duxiaoming on 2017/7/18.
 * blog:m78snail.com
 * description:
 */

class OkHttpHelper private constructor() {
    private var mHttpClient: OkHttpClient
    private var gson: Gson
    private var mHandler: Handler

    init {
        mHttpClient = OkHttpClient()
        gson = Gson()
        mHttpClient.setConnectTimeout(10, TimeUnit.SECONDS)
        mHttpClient.setReadTimeout(10, TimeUnit.SECONDS)
        mHttpClient.setWriteTimeout(30, TimeUnit.SECONDS)
        mHandler = Handler(Looper.getMainLooper())
    }

    companion object {
        var mInstance: OkHttpHelper? = null
            private set

        init {
            mInstance = OkHttpHelper()
        }

        val TOKEN_MISSING = 401// token 丢失
        val TOKEN_ERROR = 402 // token 错误
        val TOKEN_EXPIRE = 403 // token 过期

    }

    fun <T> get(url: String, callback: BaseCallback<T>) {
        get(url, null, callback)
    }

    fun <T> get(url: String, param: MutableMap<String, String>?, callback: BaseCallback<T>) {
        val request: Request = buildGetRequest(url)
        request(request, callback)
    }

    fun <T> post(url: String, params: MutableMap<String, String>, callback: BaseCallback<T>) {
        val request: Request = buildPostRequest(url, params)
        request(request, callback)
    }

    private fun <T> request(request: Request, callback: BaseCallback<T>) {
        callback.onBeforeRequest(request)
        mHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(response: Response?) {
                if (response != null) {
                    callbackResponse(callback, response)

                    if (response.isSuccessful) {
                        val resultStr = response.body().string()
                        if (callback.mType == String::javaClass) {

                        } else {
                            try {
                                val obj = gson.fromJson<T>(resultStr, callback.mType)

                                mHandler.post { callback.onSuccess(response, obj) }


                            } catch (e: JsonParseException) {

                            }
                        }
                    } else if(response.code() == TOKEN_ERROR||response.code() == TOKEN_EXPIRE ||response.code() == TOKEN_MISSING ){

                        callbackTokenError(callback,response)
                    }
                    else {
                        callbackError(callback, response, null!!)
                    }
                }
            }

            override fun onFailure(request: Request?, e: IOException?) {
                if (request != null) {
                    callbackFailure(callback, request, e)
                }
            }

        })
    }

    private fun buildPostRequest(url: String, params: MutableMap<String, String>): Request {
        return buildRequest(url, HttpMethodType.POST, params)
    }

    private fun buildGetRequest(url: String): Request {
        return buildRequest(url, HttpMethodType.GET, null)
    }

    private fun buildRequest(url: String, methodType: HttpMethodType, params: MutableMap<String, String>?): Request {
        val builder = Request.Builder().url(url)
        if (methodType == HttpMethodType.POST) {
            val body = builderFormData(params)
            builder.post(body)
        } else if (methodType == HttpMethodType.GET) {
            val urlTemp = buildUrlParams(url, params)
            builder.url(urlTemp)
            builder.get()
        }
        return builder.build()
    }

    private fun buildUrlParams(urlTemp: String, paramsTemp: MutableMap<String, String>?): String {
        var url = urlTemp
        var params = paramsTemp

        if (params == null)
            params = HashMap(1)

        val token = JDApplication.mInstance?.getToken()

        if (!TextUtils.isEmpty(token)) {
            params.put("token", token as String)
        }

        val sb = StringBuffer()
        for ((key, value) in params) {
            sb.append(key + "=" + value)
            sb.append("&")
        }
        var s = sb.toString()
        if (s.endsWith("&")) {
            s = s.substring(0, s.length - 1)
        }

        if (url.indexOf("?") > 0) {
            url = url + "&" + s
        } else {
            url = url + "?" + s
        }

        return url
    }


    private fun builderFormData(params: Map<String, String>?): RequestBody {
        val builder = FormEncodingBuilder()
        for (entry in params!!.entries) {

            builder.add(entry.key, entry.value)
        }
        return builder.build()

    }

    private fun callbackTokenError(callback: BaseCallback<*>, response: Response) {

        mHandler.post { callback.onTokenError(response, response.code()) }
    }


    private fun callbackError(callback: BaseCallback<*>, response: Response, e: Exception) {

        mHandler.post { callback.onError(response, response.code(), e) }
    }


    private fun callbackFailure(callback: BaseCallback<*>, request: Request, e: IOException?) {

        mHandler.post { callback.onFailure(request, e!!) }
    }


    private fun callbackResponse(callback: BaseCallback<*>, response: Response) {

        mHandler.post { callback.onResponse(response) }
    }

    internal enum class HttpMethodType {

        GET,
        POST

    }


}
