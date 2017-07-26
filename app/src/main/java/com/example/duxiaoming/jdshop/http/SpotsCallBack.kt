package com.example.duxiaoming.jdshop.http

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.activity.LoginActivity
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import dmax.dialog.SpotsDialog


abstract class SpotsCallBack<in T>(val mContext: Context) : BaseCallback<T>(mContext) {


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

    override fun onTokenError(response: Response, code: Int) {
        Toast.makeText(mContext, R.string.token_error, Toast.LENGTH_SHORT).show()
        val intent: Intent = Intent(mContext, LoginActivity::class.java)
        mContext.startActivity(intent)
    }
}