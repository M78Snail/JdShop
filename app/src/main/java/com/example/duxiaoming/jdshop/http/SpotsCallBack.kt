package com.example.duxiaoming.jdshop.http

import android.content.Context
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import dmax.dialog.SpotsDialog


abstract class SpotsCallBack<in T> (val mContext: Context) : BaseCallback<T>(mContext) {


    private val mDialog: SpotsDialog

    init {
        mDialog = SpotsDialog(mContext, "拼命加载中")
    }

    fun showDialog() {
        mDialog.show()
    }

    fun dismissDialog() {
        mDialog.dismiss()
    }


    fun setLoadMessage(resId: Int) {
        mDialog.setMessage(mContext.getString(resId))
    }


    override fun onFailure(request: Request, e: Exception) {
        dismissDialog()
    }

    override fun onBeforeRequest(request: Request) {

        showDialog()
    }

    override fun onResponse(response: Response) {
        dismissDialog()
    }
}