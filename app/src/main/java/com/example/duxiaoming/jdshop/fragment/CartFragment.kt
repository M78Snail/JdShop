package com.example.duxiaoming.jdshop.fragment

import android.content.Intent.ACTION_EDIT
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.adapter.CartAdapter
import com.example.duxiaoming.jdshop.adapter.decoration.DividerItemDecoration
import com.example.duxiaoming.jdshop.bean.ShoppingCart
import com.example.duxiaoming.jdshop.utils.CartProvider
import com.example.duxiaoming.jdshop.widget.JDToolBar


class CartFragment : Fragment(), View.OnClickListener {


    private var mRecyclerView: RecyclerView? = null

    private var cartProvider: CartProvider? = null
    private var mAdapter: CartAdapter? = null
    protected var mToolbar: JDToolBar? = null


    private var carts: MutableList<ShoppingCart>? = mutableListOf()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater!!.inflate(R.layout.fragment_cart, container, false)
        initView(view)
        showData()
        return view
    }

    private fun initView(view: View) {
        mToolbar = view.findViewById(R.id.toolbar) as JDToolBar
        mRecyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        cartProvider = CartProvider(context)
    }

    private fun showData() {


        carts = cartProvider!!.getAll()

        mAdapter = CartAdapter(context, carts)

        mRecyclerView!!.adapter = mAdapter
        mRecyclerView!!.layoutManager = LinearLayoutManager(context)
        mRecyclerView!!.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST))


    }

    fun refData() {
        mAdapter!!.clear()
        carts = cartProvider?.getAll()
        mAdapter!!.addData(carts)

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

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
