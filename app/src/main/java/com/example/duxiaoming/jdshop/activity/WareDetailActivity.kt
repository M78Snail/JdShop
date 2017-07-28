package com.example.duxiaoming.jdshop.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.onekeyshare.OnekeyShare
import com.example.duxiaoming.jdshop.Contants
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.bean.Wares
import com.example.duxiaoming.jdshop.utils.CartProvider
import com.example.duxiaoming.jdshop.widget.JDToolBar
import dmax.dialog.SpotsDialog
import java.io.Serializable


/**
 * Created by duxiaoming on 2017/7/25.
 * blog:m78snail.com
 * description:
 */
class WareDetailActivity : BaseActivity(), View.OnClickListener {


    private var mWebView: WebView? = null
    private var mToolBar: JDToolBar? = null

    private var mWare: Wares? = null

    private var mAppInterfce: WebAppInterface? = null

    private var cartProvider: CartProvider? = null

    private var mDialog: SpotsDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ware_detail)
        val serializable: Serializable? = intent.getSerializableExtra(Contants.WARE)
        if (serializable == null) {
            finish()
        } else {
            mWare = serializable as Wares

        }

        initView()
        initWebView()
        initToolBar()
    }


    private fun initView() {
        mWebView = findViewById(R.id.webView) as WebView
        mToolBar = findViewById(R.id.toolbar) as JDToolBar
        cartProvider = CartProvider(this)
        mDialog = SpotsDialog(this, "loading....")
        mAppInterfce = WebAppInterface(this)
    }

    private fun initWebView() {

        val settings = mWebView?.settings

        settings?.javaScriptEnabled = true
        settings?.blockNetworkImage = false
        settings?.setAppCacheEnabled(true)


        mWebView?.loadUrl(Contants.API.WARES_DETAIL)

        mAppInterfce = WebAppInterface(this)
        mWebView?.addJavascriptInterface(mAppInterfce, "appInterface")
        mWebView?.setWebViewClient(WC())

    }

    private fun initToolBar() {

        mToolBar?.setNavigationOnClickListener(this)

        mToolBar?.setRightButtonText("分享")

        mToolBar?.setRightButtonOnClickListener(View.OnClickListener { showShare() })


    }

    private fun showShare() {
        ShareSDK.initSDK(this)

        val oks = OnekeyShare()
        //关闭sso授权
        oks.disableSSOWhenAuthorize()

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share))

        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://www.cniao5.com")

        // text是分享文本，所有平台都需要这个字段
        oks.text = mWare?.name

        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl(mWare?.imgUrl)

        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://www.cniao5.com")
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(mWare?.name)

        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name))

        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.cniao5.com")

        // 启动分享GUI
        oks.show(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ShareSDK.stopSDK(this)
    }


    override fun onClick(v: View?) {
        finish()
    }

    inner class WC : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            if (mDialog!!.isShowing)
                mDialog!!.dismiss()

            mAppInterfce?.showDetail()
        }
    }

    inner class WebAppInterface(private var mContext: Context) {


        @JavascriptInterface
        fun showDetail() {


            runOnUiThread { mWebView?.loadUrl("javascript:showDetail(" + mWare?.id + ")") }
        }


        @JavascriptInterface
        fun buy(id: Long) {

            if (mWare != null) {
                cartProvider?.put(mWare as Wares)
                Toast.makeText(mContext, "已添加到购物车", Toast.LENGTH_SHORT).show()
            }

        }

        @JavascriptInterface
        fun addFavorites(id: Long) {


        }

    }
}