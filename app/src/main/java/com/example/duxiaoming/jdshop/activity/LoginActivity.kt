package com.example.duxiaoming.jdshop.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.duxiaoming.jdshop.Contants
import com.example.duxiaoming.jdshop.JDApplication
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.bean.User
import com.example.duxiaoming.jdshop.http.OkHttpHelper
import com.example.duxiaoming.jdshop.http.SpotsCallBack
import com.example.duxiaoming.jdshop.msg.LoginRespMsg
import com.example.duxiaoming.jdshop.utils.DESUtil
import com.example.duxiaoming.jdshop.widget.ClearEditText
import com.example.duxiaoming.jdshop.widget.JDToolBar
import com.squareup.okhttp.Response
import java.util.*


/**
 * Created by duxiaoming on 2017/7/25.
 * blog:m78snail.com
 * description:
 */
class LoginActivity : AppCompatActivity(), View.OnClickListener {


    private var mToolBar: JDToolBar? = null

    private var mEtxtPhone: ClearEditText? = null

    private var mEtxtPwd: ClearEditText? = null

    private val okHttpHelper = OkHttpHelper.mInstance


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
        initToolBar()
    }

    private fun initView() {
        mToolBar = findViewById(R.id.toolbar) as JDToolBar?
        mEtxtPhone = findViewById(R.id.etxt_phone) as ClearEditText?
        mEtxtPwd = findViewById(R.id.etxt_pwd) as ClearEditText?

        findViewById(R.id.btn_login).setOnClickListener(this)

    }

    private fun initToolBar() {

        mToolBar?.setNavigationOnClickListener {
            finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }



    override fun onClick(v: View?) {
        val phone = mEtxtPhone?.text.toString().trim()
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show()
            return
        }

        val pwd = mEtxtPwd?.text.toString().trim()
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show()
            return
        }

        val params = HashMap<String, String>(2)
        params.put("phone", phone)
        Log.d("TAG>>>>>>>>>>>PWD", "--" + DESUtil.encode(Contants.DES_KEY, pwd))
        params.put("password", DESUtil.encode(Contants.DES_KEY, pwd) as String)

        okHttpHelper?.post(Contants.API.LOGIN, params, object : SpotsCallBack<LoginRespMsg<User>>(this@LoginActivity) {
            override fun onSuccess(response: Response, userLoginRespMsg: LoginRespMsg<User>) {
                val application = JDApplication.mInstance
                Log.d("TAG>>>>>>data", userLoginRespMsg.data.toString())
                Log.d("TAG>>>>>>token", userLoginRespMsg.token.toString())

                if (userLoginRespMsg.data != null && userLoginRespMsg.token != null) {
                    application?.putUser(userLoginRespMsg.data!!, userLoginRespMsg.token!!)
                } else {
                    Toast.makeText(this@LoginActivity, "登录失败，用户名或密码有误", Toast.LENGTH_SHORT).show()
                    return
                }

                if (application?.getIntent() == null) {
                    setResult(RESULT_OK)
                    finish()
                } else {

                    application.jumpToTargetActivity(this@LoginActivity)
                    finish()

                }
            }

            override fun onError(response: Response, code: Int, e: Exception) {
            }


        })

    }

}