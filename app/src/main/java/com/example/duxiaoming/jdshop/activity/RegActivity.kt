package com.example.duxiaoming.jdshop.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK
import cn.smssdk.utils.SMSLog
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.utils.ManifestUtil
import com.example.duxiaoming.jdshop.widget.ClearEditText
import com.example.duxiaoming.jdshop.widget.JDToolBar
import org.json.JSONObject
import java.util.regex.Pattern


/**
 * Created by duxiaoming on 2017/7/28.
 * blog:m78snail.com
 * description:
 */
class RegActivity : BaseActivity() {

    private var mToolBar: JDToolBar? = null

    private var mTxtCountry: TextView? = null

    private var mTxtCountryCode: TextView? = null

    private var mEtxtPhone: ClearEditText? = null

    private var mEtxtPwd: ClearEditText? = null

    private var evenHanlder: SMSEvenHanlder? = null

    companion object {
        private val DEFAULT_COUNTRY_ID = "42"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_reg)
        initView()
        initToolBar()

        SMSSDK.initSDK(this, ManifestUtil.getMetaDataValue(this, "mob_sms_appKey"),
                ManifestUtil.getMetaDataValue(this, "mob_sms_appSecrect"))

        SMSSDK.registerEventHandler(evenHanlder)

        val country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID)

        if (country != null) {

            mTxtCountryCode?.text = "+" + country[1]

            mTxtCountry?.text = country[0]

        }

    }

    fun initView() {
        mToolBar = findViewById(R.id.toolbar) as JDToolBar

        mTxtCountry = findViewById(R.id.txtCountry) as TextView

        mTxtCountryCode = findViewById(R.id.txtCountryCode) as TextView

        mEtxtPhone = findViewById(R.id.edittxt_phone) as ClearEditText

        mEtxtPwd = findViewById(R.id.edittxt_pwd) as ClearEditText

        evenHanlder = SMSEvenHanlder()
    }

    private fun initToolBar() {


        mToolBar?.setRightButtonOnClickListener(View.OnClickListener { getCode() })

    }

    private fun getCode() {

        val phone = mEtxtPhone?.text.toString().trim().replace("\\s*", "")
        val code = mTxtCountryCode?.text.toString().trim()

        checkPhoneNum(phone, code)

        //not 86   +86
        SMSSDK.getVerificationCode(code, phone)

    }

    private fun checkPhoneNum(phone: String, codeTemp: String) {
        var code = codeTemp
        if (code.startsWith("+")) {
            code = code.substring(1)
        }

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show()
            return
        }

        if (code === "86") {
            if (phone.length != 11) {
                Toast.makeText(this, "手机号码长度不对", Toast.LENGTH_SHORT).show()
                return
            }

        }

        val rule = "^1(3|5|7|8|4)\\d{9}"
        val p = Pattern.compile(rule)
        val m = p.matcher(phone)

        if (!m.matches()) {
            Toast.makeText(this, "您输入的手机号码格式不正确", Toast.LENGTH_SHORT).show()
            return
        }

    }


    inner class SMSEvenHanlder : EventHandler() {
        override fun afterEvent(event: Int, result: Int, data: Any?) {
            runOnUiThread {
                Runnable {
                    if (result === SMSSDK.RESULT_COMPLETE) {
                        if (event === SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {

                            onCountryListGot(data as ArrayList<HashMap<String, Any>>)

                        } else if (event === SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            // 请求验证码后，跳转到验证码填写页面

                            afterVerificationCodeRequested()

                        } else if (event === SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                        }
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
    }

    private fun onCountryListGot(countries: ArrayList<HashMap<String, Any>>) {
        // 解析国家列表
        for (country in countries) {
            val code = country["zone"] as String
            val rule = country["rule"] as String
            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)) {
                continue
            }


        }

    }


    /** 请求验证码后，跳转到验证码填写页面  */
    private fun afterVerificationCodeRequested() {


        val phone = mEtxtPhone?.text.toString().trim().replace("\\s*", "")
        var code = mTxtCountryCode?.text.toString().trim()
        val pwd = mEtxtPwd?.text.toString().trim()

        if (code.startsWith("+")) {
            code = code.substring(1)
        }

        val intent = Intent(this, RegSecondActivity::class.java)
        intent.putExtra("phone", phone)
        intent.putExtra("pwd", pwd)
        intent.putExtra("countryCode", code)

        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()

        SMSSDK.unregisterEventHandler(evenHanlder)

    }


}