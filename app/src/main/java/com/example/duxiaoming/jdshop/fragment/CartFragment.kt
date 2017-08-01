package com.example.duxiaoming.jdshop.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.example.duxiaoming.jdshop.Contants
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.adapter.CartAdapter
import com.example.duxiaoming.jdshop.adapter.decoration.DividerItemDecoration
import com.example.duxiaoming.jdshop.bean.ShoppingCart
import com.example.duxiaoming.jdshop.bean.User
import com.example.duxiaoming.jdshop.http.OkHttpHelper
import com.example.duxiaoming.jdshop.http.SpotsCallBack
import com.example.duxiaoming.jdshop.utils.CartProvider
import com.example.duxiaoming.jdshop.widget.JDToolBar
import com.squareup.okhttp.Response


open class CartFragment : BaseFragment(), View.OnClickListener {
    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_cart, container, false)

        return view
    }

    override fun init(view: View) {
        mToolbar = view.findViewById(R.id.toolbar) as JDToolBar
        mRecyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        mCheckBox = view.findViewById(R.id.checkbox_all) as CheckBox
        mTextTotal = view.findViewById(R.id.txt_total) as TextView
        mBtnDel = view.findViewById(R.id.btn_del) as Button
        mBtnOrder = view.findViewById(R.id.btn_order) as Button

        mBtnOrder?.setOnClickListener {
            httpHelper?.get(Contants.API.USER_DETAIL, object : SpotsCallBack<User>(context) {
                override fun onSuccess(response: Response, t: User) {
                    Toast.makeText(context, "获取数据成功", Toast.LENGTH_SHORT).show()

                }

                override fun onError(response: Response, code: Int, e: Exception) {
                }

            })
        }
        mBtnDel?.setOnClickListener {
            mAdapter?.delCart()
        }

        cartProvider = CartProvider(context)

        showData()
        changeToolbar()
    }


    private var mRecyclerView: RecyclerView? = null

    private var cartProvider: CartProvider? = null
    private var mAdapter: CartAdapter? = null
    protected var mToolbar: JDToolBar? = null
    private var mCheckBox: CheckBox? = null
    private var mTextTotal: TextView? = null
    private var mBtnDel: Button? = null

    private var mBtnOrder: Button? = null
    private var carts: MutableList<ShoppingCart>? = mutableListOf()

    private var httpHelper: OkHttpHelper? = OkHttpHelper.mInstance

    companion object {
        val ACTION_EDIT = 1

        val ACTION_CAMPLATE = 2
    }


    private fun showData() {


        carts = cartProvider!!.getAll()

        mAdapter = CartAdapter(context, carts, mCheckBox!!, mTextTotal!!)

        mRecyclerView!!.adapter = mAdapter
        mRecyclerView!!.layoutManager = LinearLayoutManager(context)
        mRecyclerView!!.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST))


    }

    fun refData() {
        mAdapter!!.clear()
        carts = cartProvider?.getAll()
        mAdapter!!.addData(carts)
        mAdapter!!.showTotalPrice()


    }

    fun changeToolbar() {

        mToolbar!!.hideSearchView()
        mToolbar!!.showTitleView()
        mToolbar!!.setTitle(R.string.cart)
        mToolbar!!.getRightButton()!!.visibility = View.VISIBLE
        mToolbar!!.setRightButtonText("编辑")

        mToolbar!!.getRightButton()!!.setOnClickListener(this)

        mToolbar!!.getRightButton()!!.tag = ACTION_EDIT


    }

    private fun showDelControl() {
        mToolbar?.getRightButton()?.text = "完成"
        mTextTotal?.visibility = View.GONE
        mBtnDel?.visibility = View.VISIBLE
        mToolbar?.getRightButton()?.tag = ACTION_CAMPLATE

        mAdapter?.checkAll_None(false)
        mCheckBox?.isChecked = false

    }

    private fun hideDelControl() {

        mTextTotal?.visibility = View.VISIBLE
        mBtnDel?.visibility = View.GONE
        mToolbar?.setRightButtonText("编辑")
        mToolbar?.getRightButton()?.tag = ACTION_EDIT

        mAdapter?.checkAll_None(true)
        mAdapter?.showTotalPrice()

        mCheckBox?.isChecked = true
    }


    override fun onClick(v: View?) {
        val action = v?.tag as Int
        if (action === ACTION_EDIT) {

            showDelControl()
        } else if (action === ACTION_CAMPLATE) {

            hideDelControl()
        }
    }

}
