package com.example.duxiaoming.jdshop.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.duxiaoming.jdshop.Contants
import com.example.duxiaoming.jdshop.JDApplication
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.adapter.AddressAdapter
import com.example.duxiaoming.jdshop.adapter.decoration.DividerItemDecoration
import com.example.duxiaoming.jdshop.bean.Address
import com.example.duxiaoming.jdshop.http.OkHttpHelper
import com.example.duxiaoming.jdshop.http.SpotsCallBack
import com.example.duxiaoming.jdshop.msg.BaseRespMsg
import com.example.duxiaoming.jdshop.widget.JDToolBar
import com.squareup.okhttp.Response
import java.util.*


/**
 * Created by duxiaoming on 2017/8/1.
 * blog:m78snail.com
 * description:
 */
class AddressListActivity : BaseActivity() {
    private var mToolBar: JDToolBar? = null
    private var mRecyclerview: RecyclerView? = null
    private var mAdapter: AddressAdapter? = null

    private val mHttpHelper = OkHttpHelper.mInstance


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)

        initView()
        initToolbar()

        initView()

        initAddress()

    }

    private fun initView() {
        mToolBar = findViewById(R.id.toolbar) as JDToolBar?
        mRecyclerview = findViewById(R.id.recycler_view) as RecyclerView?
    }

    private fun initToolbar() {

        mToolBar?.setNavigationOnClickListener { finish() }

        mToolBar?.setRightButtonOnClickListener(View.OnClickListener { toAddActivity() })

    }


    private fun toAddActivity() {

        val intent = Intent(this, AddressAddActivity::class.java)
        startActivityForResult(intent, Contants.REQUEST_CODE)
    }

    private fun initAddress() {


        val params = HashMap<String, String>(1)
        params.put("user_id", JDApplication.mInstance?.getUser()?.id.toString())

        mHttpHelper?.get(Contants.API.ADDRESS_LIST, params, object : SpotsCallBack<MutableList<Address>>(this@AddressListActivity) {

            override fun onSuccess(response: Response, t: MutableList<Address>) {
                showAddress(t)
            }

            override fun onError(response: Response, code: Int, e: Exception) {
            }


        })
    }

    private fun showAddress(addresses: MutableList<Address>) {

        Collections.sort(addresses)
        if (mAdapter == null) {
            mAdapter = AddressAdapter(this, addresses, object : AddressAdapter.AddressLisneter {
                override fun setDefault(address: Address) {

                    updateAddress(address)

                }
            })
            mRecyclerview?.adapter = mAdapter
            mRecyclerview?.layoutManager = LinearLayoutManager(this@AddressListActivity)
            mRecyclerview?.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST))
        } else {
            mAdapter?.refreshData(addresses)
            mRecyclerview?.adapter = mAdapter
        }

    }

    fun updateAddress(address: Address) {

        val params = HashMap<String, String>(1)
        params.put("id", address.id.toString())
        params.put("consignee", address.consignee)
        params.put("phone", address.phone)
        params.put("addr", address.addr)
        params.put("zip_code", address.zipCode)
        params.put("is_default", address.isDefault.toString())

        mHttpHelper?.post(Contants.API.ADDRESS_UPDATE, params, object : SpotsCallBack<BaseRespMsg>(this@AddressListActivity) {

            override fun onSuccess(response: Response, t: BaseRespMsg) {
                if (t.status == BaseRespMsg.STATUS_SUCCESS) {

                    initAddress()
                }
            }

            override fun onError(response: Response, code: Int, e: Exception) {

            }
        })

    }


}