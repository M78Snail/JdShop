package com.example.duxiaoming.jdshop.http

import android.os.Handler
import android.os.Looper
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
    }

    fun <T> get(url: String, callback: BaseCallback<T>) {
        val request: Request = buildGetRequest(url)
        request(request, callback)
    }

    fun <T> post(url: String, params: Map<String, String>, callback: BaseCallback<T>) {
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
                    } else {
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

    private fun buildPostRequest(url: String, params: Map<String, String>): Request {
        return buildRequest(url, HttpMethodType.POST, params)
    }

    private fun buildGetRequest(url: String): Request {
        return buildRequest(url, HttpMethodType.GET, null)
    }

    private fun buildRequest(url: String, methodType: HttpMethodType, params: Map<String, String>?): Request {
        val builder = Request.Builder().url(url)
        if (methodType == HttpMethodType.POST) {
            val body = builderFormData(params)
            builder.post(body)
        } else if (methodType == HttpMethodType.GET) {
            builder.get()
        }
        return builder.build()
    }

    private fun builderFormData(params: Map<String, String>?): RequestBody {
        val builder = FormEncodingBuilder()
        for (entry in params!!.entries) {

            builder.add(entry.key, entry.value)
        }
        return builder.build()

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
