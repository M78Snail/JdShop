package com.example.duxiaoming.jdshop.activity

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.duxiaoming.jdshop.Contants
import com.example.duxiaoming.jdshop.JDApplication
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.adapter.WareOrderAdapter
import com.example.duxiaoming.jdshop.adapter.layoutmanager.FullyLinearLayoutManager
import com.example.duxiaoming.jdshop.http.OkHttpHelper
import com.example.duxiaoming.jdshop.http.SpotsCallBack
import com.example.duxiaoming.jdshop.msg.BaseRespMsg
import com.example.duxiaoming.jdshop.msg.CreateOrderRespMsg
import com.example.duxiaoming.jdshop.utils.CartProvider
import com.example.duxiaoming.jdshop.utils.JSONUtil
import com.pingplusplus.android.PaymentActivity
import com.squareup.okhttp.Response


/**
 * Created by duxiaoming on 2017/7/29.
 * blog:m78snail.com
 * description:
 */
class CreateOrderActivity : BaseActivity(), View.OnClickListener {

    companion object {
        /**
         * 银联支付渠道
         */
        private val CHANNEL_UPACP = "upacp"
        /**
         * 微信支付渠道
         */
        private val CHANNEL_WECHAT = "wx"
        /**
         * 支付支付渠道
         */
        private val CHANNEL_ALIPAY = "alipay"
        /**
         * 百度支付渠道
         */
        private val CHANNEL_BFB = "bfb"
        /**
         * 京东支付渠道
         */
        private val CHANNEL_JDPAY_WAP = "jdpay_wap"

    }

    private var txtOrder: TextView? = null
    private var mRecyclerView: RecyclerView? = null
    private var mLayoutAlipay: RelativeLayout? = null
    private var mLayoutWechat: RelativeLayout? = null
    private var mLayoutBd: RelativeLayout? = null
    private var mRbAlipay: RadioButton? = null
    private var mRbWechat: RadioButton? = null
    private var mRbBd: RadioButton? = null
    private var mBtnCreateOrder: Button? = null
    private var mTxtTotal: TextView? = null
    private var cartProvider: CartProvider? = null
    private var mAdapter: WareOrderAdapter? = null
    private var okHttpHelper = OkHttpHelper.mInstance

    private var orderNum: String? = null
    private var payChannel = CHANNEL_ALIPAY
    private var amount: Float = 0.toFloat()


    private val channels = HashMap<String, RadioButton>(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_order)
        initView()
        showData()
    }

    private fun initView() {
        txtOrder = findViewById(R.id.txt_order) as TextView?
        mRecyclerView = findViewById(R.id.recycler_view) as RecyclerView?

        mLayoutAlipay = findViewById(R.id.rl_alipay) as RelativeLayout?

        mLayoutWechat = findViewById(R.id.rl_wechat) as RelativeLayout?
        mLayoutBd = findViewById(R.id.rl_bd) as RelativeLayout?
        mRbAlipay = findViewById(R.id.rb_alipay) as RadioButton?
        mRbWechat = findViewById(R.id.rb_webchat) as RadioButton?
        mRbBd = findViewById(R.id.rb_bd) as RadioButton?
        mBtnCreateOrder = findViewById(R.id.btn_createOrder) as Button?
        mBtnCreateOrder?.setOnClickListener { createNewOrder() }
        mTxtTotal = findViewById(R.id.txt_total) as TextView?
        cartProvider = CartProvider(this)
        mAdapter = WareOrderAdapter(this, cartProvider?.getAll())

        channels.put(CHANNEL_ALIPAY, mRbAlipay!!)
        channels.put(CHANNEL_WECHAT, mRbWechat!!)
        channels.put(CHANNEL_BFB, mRbBd!!)

        mLayoutAlipay?.setOnClickListener(this)
        mLayoutWechat?.setOnClickListener(this)
        mLayoutBd?.setOnClickListener(this)



        amount = (mAdapter as WareOrderAdapter).getTotalPrice()
        mTxtTotal?.text = "应付款： ￥" + amount

    }

    fun showData() {

        val layoutManager = FullyLinearLayoutManager(this)
        layoutManager.orientation = GridLayoutManager.HORIZONTAL
        mRecyclerView?.layoutManager = layoutManager

        mRecyclerView?.adapter = mAdapter

    }

    override fun onClick(v: View) {
        selectPayChannle(v.tag.toString())
    }

    private fun selectPayChannle(paychannel: String) {
        for ((key, rb) in channels) {
            payChannel = paychannel
            if (key == paychannel) {

                val isCheck = rb.isChecked
                rb.isChecked = !isCheck

            } else
                rb.isChecked = false


        }
    }

    fun createNewOrder() {

        postNewOrder()
    }

    private fun postNewOrder() {


        val carts = mAdapter?.getDatas()

        val items = ArrayList<WareItem>(carts?.size as Int)
        carts.mapTo(items) { WareItem(it.id, it.price!!.toInt()) }

        val item_json = JSONUtil.toJSON(items)

        val params = HashMap<String, String>(5)
        params.put("user_id", JDApplication.mInstance?.getUser()?.id.toString())
        params.put("item_json", item_json)
        params.put("pay_channel", payChannel)
        params.put("amount", amount.toInt().toString() + "")
        params.put("addr_id", 1.toString() + "")


        mBtnCreateOrder?.isEnabled = false

        okHttpHelper?.post(Contants.API.ORDER_CREATE, params, object : SpotsCallBack<CreateOrderRespMsg>(baseContext) {
            override fun onSuccess(response: Response, t: CreateOrderRespMsg) {


                mBtnCreateOrder?.isEnabled = true
                orderNum = t.data?.orderNum
                val charge = t.data?.charge

                openPaymentActivity(JSONUtil.toJSON(charge!!))

            }

            override fun onError(response: Response, code: Int, e: Exception) {
                mBtnCreateOrder?.isEnabled = true
            }
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //支付页面返回处理
        if (requestCode === Contants.REQUEST_CODE_PAYMENT && resultCode === Activity.RESULT_OK) {
            val result = data?.extras?.getString("pay_result")

            if (result == "success")
                changeOrderStatus(1)
            else if (result == "fail")
                changeOrderStatus(-1)
            else if (result == "cancel")
                changeOrderStatus(-2)
            else
                changeOrderStatus(0)

            /* 处理返回值
         * "success" - payment succeed
         * "fail"    - payment failed
         * "cancel"  - user canceld
         * "invalid" - payment plugin not installed
         *
         * 如果是银联渠道返回 invalid，调用 UPPayAssistEx.installUPPayPlugin(this); 安装银联安全支付控件。
         */
            //                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
            //                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息

        }
    }

    private fun openPaymentActivity(charge: String) {

        val intent = Intent()
        val packageName = packageName
        val componentName = ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity")
        intent.component = componentName
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, charge)
        startActivityForResult(intent, Contants.REQUEST_CODE_PAYMENT)
    }

    private fun changeOrderStatus(status: Int) {

        val params = HashMap<String, String>(5)
        params.put("order_num", orderNum as String)
        params.put("status", status.toString() + "")


        okHttpHelper?.post(Contants.API.ORDER_COMPLEPE, params, object : SpotsCallBack<BaseRespMsg>(baseContext) {
            override fun onSuccess(response: Response, t: BaseRespMsg) {

                toPayResultActivity(status)
            }

            override fun onError(response: Response, code: Int, e: Exception) {
                toPayResultActivity(-1)
            }
        })

    }

    private fun toPayResultActivity(status: Int) {

        val intent = Intent(this, PayResultActivity::class.java)
        intent.putExtra("status", status)

        startActivity(intent)
        this.finish()

    }


    internal inner class WareItem(var ware_id: Long?, var amount: Int)


}