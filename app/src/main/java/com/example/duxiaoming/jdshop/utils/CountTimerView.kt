package com.example.duxiaoming.jdshop.utils

import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import com.example.duxiaoming.jdshop.R


class CountTimerView : CountDownTimer {
    private var btn: TextView? = null
    private var endStrRid: Int = 0


    /**
     * 参数 millisInFuture       倒计时总时间（如60S，120s等）
     * 参数 countDownInterval    渐变时间（每次倒计1s）
     * 参数 btn   点击的按钮(因为Button是TextView子类，为了通用我的参数设置为TextView）
     * 参数 endStrRid   倒计时结束后，按钮对应显示的文字
     */
    constructor(millisInFuture: Long, countDownInterval: Long, btn: TextView, endStrRid: Int) : super(millisInFuture, countDownInterval) {
        this.btn = btn
        this.endStrRid = endStrRid
    }


    /**

     * 参数上面有注释
     */
    constructor(btn: Button?, endStrRid: Int) : super(TIME_COUNT.toLong(), 1000) {
        this.btn = btn
        this.endStrRid = endStrRid
    }

    constructor(btn: Button?) : super(TIME_COUNT.toLong(), 1000) {
        this.btn = btn
        this.endStrRid = R.string.smssdk_resend_identify_code
    }


    // 计时完毕时触发
    override fun onFinish() {

        btn!!.setText(endStrRid)
        btn!!.isEnabled = true
    }

    // 计时过程显示
    override fun onTick(millisUntilFinished: Long) {
        btn!!.isEnabled = false
        btn!!.text = (millisUntilFinished / 1000).toString() + " 秒后可重新发送"

    }

    companion object {

        val TIME_COUNT = 61000//时间防止从59s开始显示（以倒计时60s为例子）
    }
}
