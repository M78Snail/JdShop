package com.example.duxiaoming.jdshop.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.duxiaoming.jdshop.Contants
import com.example.duxiaoming.jdshop.JDApplication
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.activity.AddressListActivity
import com.example.duxiaoming.jdshop.activity.LoginActivity
import com.example.duxiaoming.jdshop.bean.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class MineFragment : BaseFragment() {


    private var mImageHead: CircleImageView? = null

    private var mTxtUserName: TextView? = null

    private var mbtnLogout: Button? = null


    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun init(view: View) {
        mImageHead = view.findViewById(R.id.img_head) as CircleImageView?
        mImageHead?.setOnClickListener {
            toLogin()
        }
        mTxtUserName = view.findViewById(R.id.txt_username) as TextView?

        mTxtUserName?.setOnClickListener {
            toLogin()
        }

        mbtnLogout = view.findViewById(R.id.btn_logout) as Button?
        mbtnLogout?.setOnClickListener {
            logout()
        }

        view.findViewById(R.id.txt_my_address).setOnClickListener { toAddressActivity() }

        val user = JDApplication.mInstance?.getUser()
        showUser(user)
    }


    private fun toLogin() {
        val intent = Intent(activity, LoginActivity::class.java)
        startActivityForResult(intent, Contants.REQUEST_CODE)
    }

    private fun toAddressActivity() {
        startActivity(Intent(activity, AddressListActivity::class.java), true)
    }

    private fun logout() {

        JDApplication.mInstance?.clearUser()
        showUser(null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val user = JDApplication.mInstance?.getUser()
        showUser(user)
    }

    private fun showUser(user: User?) {

        if (user != null) {

            if (!TextUtils.isEmpty(user.logo_url))
                showHeadImage(user.logo_url as String)

            mTxtUserName?.text = user.username

            mImageHead?.isClickable = false
            mTxtUserName?.isClickable = false

            mbtnLogout?.visibility = View.VISIBLE
        } else {
            mImageHead?.isClickable = true
            mTxtUserName?.isClickable = true
            mImageHead?.setImageDrawable(resources.getDrawable(R.drawable.default_head))
            mTxtUserName?.setText(R.string.to_login)
            mbtnLogout?.visibility = View.GONE
        }
    }

    private fun showHeadImage(url: String) {

        Picasso.with(activity).load(url).into(mImageHead)
    }

}
