package com.example.duxiaoming.jdshop.activity

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK
import cn.smssdk.utils.SMSLog
import com.example.duxiaoming.jdshop.Contants
import com.example.duxiaoming.jdshop.JDApplication
import com.example.duxiaoming.jdshop.MainActivity
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.bean.User
import com.example.duxiaoming.jdshop.http.OkHttpHelper
import com.example.duxiaoming.jdshop.http.SpotsCallBack
import com.example.duxiaoming.jdshop.msg.BaseRespMsg
import com.example.duxiaoming.jdshop.msg.LoginRespMsg
import com.example.duxiaoming.jdshop.utils.CountTimerView
import com.example.duxiaoming.jdshop.utils.DESUtil
import com.example.duxiaoming.jdshop.utils.ManifestUtil
import com.example.duxiaoming.jdshop.widget.ClearEditText
import com.example.duxiaoming.jdshop.widget.JDToolBar
import com.squareup.okhttp.Response
import dmax.dialog.SpotsDialog
import org.json.JSONObject


/**
 * Created by duxiaoming on 2017/7/28.
 * blog:m78snail.com
 * description:
 */
class RegSecondActivity : BaseActivity() {

    private var mToolBar: JDToolBar? = null

    private var mTxtTip: TextView? = null

    private var mBtnResend: Button? = null

    private var mEtCode: ClearEditText? = null

    private var phone: String? = null
    private var pwd: String? = null
    private var countryCode: String? = null


    private var countTimerView: CountTimerView? = null


    private var dialog: SpotsDialog? = null

    private val okHttpHelper = OkHttpHelper.mInstance

    private var evenHanlder: SMSEvenHanlder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_second)
        initView()
        initToolBar()

        phone = intent.getStringExtra("phone")
        pwd = intent.getStringExtra("pwd")
        countryCode = intent.getStringExtra("countryCode")

        val formatedPhone = "+" + countryCode + " " + splitPhoneNum(phone!!)


        val text = getString(R.string.smssdk_send_mobile_detail) + formatedPhone
        mTxtTip?.text = Html.fromHtml(text)


        val timerView = CountTimerView(mBtnResend)
        timerView.start()




        SMSSDK.initSDK(this, ManifestUtil.getMetaDataValue(this, "mob_sms_appKey"),
                ManifestUtil.getMetaDataValue(this, "mob_sms_appSecrect"))

        evenHanlder = SMSEvenHanlder()
        SMSSDK.registerEventHandler(evenHanlder)

        dialog = SpotsDialog(this)
        dialog = SpotsDialog(this, "正在校验验证码")


    }

    private fun initView() {
        mToolBar = findViewById(R.id.toolbar) as JDToolBar

        mTxtTip = findViewById(R.id.txtTip) as TextView

        mBtnResend = findViewById(R.id.btn_reSend) as Button

        mEtCode = findViewById(R.id.edittxt_code) as ClearEditText

        mBtnResend?.setOnClickListener { reSendCode() }

    }

    private fun initToolBar() {

        mToolBar?.setRightButtonOnClickListener(View.OnClickListener { submitCode() })

    }

    private fun submitCode() {

        val vCode = mEtCode?.text.toString().trim()

        if (TextUtils.isEmpty(vCode)) {
            Toast.makeText(this, R.string.smssdk_write_identify_code, Toast.LENGTH_SHORT).show()
            return
        }
        SMSSDK.submitVerificationCode(countryCode, phone, vCode)
        dialog?.show()
    }


    fun reSendCode() {

        SMSSDK.getVerificationCode("+" + countryCode, phone)
        countTimerView = CountTimerView(mBtnResend, R.string.smssdk_resend_identify_code)
        countTimerView?.start()

        dialog?.setMessage("正在重新获取验证码")
        dialog?.show()
    }

    /** 分割电话号码  */
    private fun splitPhoneNum(phone: String): String {
        val builder = StringBuilder(phone)
        builder.reverse()
        var i = 4
        val len = builder.length
        while (i < len) {
            builder.insert(i, ' ')
            i += 5
        }
        builder.reverse()
        return builder.toString()
    }

    inner class SMSEvenHanlder : EventHandler() {
        override fun afterEvent(event: Int, result: Int, data: Any?) {
            if (dialog != null && dialog?.isShowing as Boolean)
                dialog?.dismiss()

            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    doReg()
                    dialog?.setMessage("正在提交注册信息")
                    dialog?.show()

                } else {
                    // 根据服务器返回的网络错误，给toast提示
                    try {
                        (data as Throwable).printStackTrace()
                        val throwable = data

                        val objectTemp = JSONObject(throwable.message)
                        val des: String = objectTemp.optString("detail")
                        if (!TextUtils.isEmpty(des)) {
                            Toast.makeText(baseContext, des, Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: Exception) {
                        SMSLog.getInstance().w(e)
                    }

                }
            }
        }
    }

    private fun doReg() {

        val params = HashMap<String, String>(2)
        params.put("phone", phone!!)
        params.put("password", DESUtil.encode(Contants.DES_KEY, pwd)!!)

        okHttpHelper?.post(Contants.API.REG, params, object : SpotsCallBack<LoginRespMsg<User>>(baseContext) {


            override fun onSuccess(response: Response, t: LoginRespMsg<User>) {

                if (dialog != null && dialog?.isShowing as Boolean)
                    dialog?.dismiss()

                if (t.status == BaseRespMsg.STATUS_ERROR) {
                    Toast.makeText(context, "注册失败", Toast.LENGTH_SHORT).show()

                    return
                }
                val application = JDApplication.mInstance
                application?.putUser(t.data as User, t.token as String)

                startActivity(Intent(this@RegSecondActivity, MainActivity::class.java))
                finish()

            }

            override fun onError(response: Response, code: Int, e: Exception) {

            }

            override fun onTokenError(response: Response, code: Int) {
                Toast.makeText(mContext, R.string.smssdk_contacts_in_app, Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        SMSSDK.unregisterEventHandler(evenHanlder)
    }


}