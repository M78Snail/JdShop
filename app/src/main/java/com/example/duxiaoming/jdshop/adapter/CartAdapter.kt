package com.example.duxiaoming.jdshop.adapter

import android.content.Context
import android.net.Uri
import android.text.Html
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.bean.ShoppingCart
import com.example.duxiaoming.jdshop.utils.CartProvider
import com.example.duxiaoming.jdshop.widget.NumberAddSubView
import com.facebook.drawee.view.SimpleDraweeView


/**
 * Created by duxiaoming on 2017/7/22.
 * blog:m78snail.com
 * description:
 */
class CartAdapter(context: Context, datas: MutableList<ShoppingCart>?, checkBox: CheckBox, tv: TextView) : SimpleAdapter<ShoppingCart>(context, R.layout.template_cart, datas), BaseAdapter.OnItemClickListener {

    private val mDataCarts: MutableList<ShoppingCart>?

    private var checkBox: CheckBox? = null
    private var textView: TextView? = null
    private var cartProvider: CartProvider? = null


    init {
        mDataCarts = datas
        setCheckBox(checkBox)
        setTextView(tv)
        cartProvider = CartProvider(context)

        setOnItemClickListener(this)
        showTotalPrice()

    }

    override fun convert(viewHoder: BaseViewHolder, item: ShoppingCart) {
        viewHoder.getTextView(R.id.text_title).text = item.name
        viewHoder.getTextView(R.id.text_price).text = "￥" + item.price
        (viewHoder.getView(R.id.drawee_view) as SimpleDraweeView).setImageURI(Uri.parse(item.imgUrl))

        val checkBox = viewHoder.getView(R.id.checkbox) as CheckBox
        checkBox.isChecked = item.isChecked


        val numberAddSubView = viewHoder.getView(R.id.num_control) as NumberAddSubView

        numberAddSubView.setValue(item.count)

        numberAddSubView.setOnButtonClickListener(object : NumberAddSubView.OnButtonClickListener {
            override fun onButtonAddClick(view: View, value: Int) {
                item.count = value
                cartProvider!!.update(item)
                showTotalPrice()
            }

            override fun onButtonSubClick(view: View, value: Int) {
                item.count = value
                cartProvider!!.update(item)
                showTotalPrice()
            }

        })
    }

    override fun onItemClick(view: View, position: Int) {
        val cart: ShoppingCart? = getItem(position)
        cart?.isChecked = !(cart?.isChecked)!!
        notifyItemChanged(position)

        checkListen()
        showTotalPrice()

    }

    private fun checkListen() {


        var count = 0
        var checkNum = 0
        if (mDataCarts != null) {
            count = mDataCarts.size

            for (cart in mDataCarts) {
                if (!cart.isChecked) {
                    checkBox?.isChecked = false
                    break
                } else {
                    checkNum += 1
                }
            }

            if (count == checkNum) {
                checkBox?.isChecked = true
            }

        }
    }


    fun showTotalPrice() {

        val total = getTotalPrice()

        textView?.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>$total</span>"), TextView.BufferType.SPANNABLE)
    }

    private fun getTotalPrice(): Float {

        var sum = 0f
        if (!isNull())
            return sum

        mDataCarts
                ?.filter { it.isChecked }
                ?.forEach { sum += it.count * it.price!! }

        return sum
    }


    private fun isNull(): Boolean {
        return (mDataCarts != null && mDataCarts.isNotEmpty())
    }

    fun setTextView(textview: TextView) {
        this.textView = textview
    }

    fun setCheckBox(ck: CheckBox) {
        this.checkBox = ck

        checkBox?.setOnClickListener {
            checkAll_None((checkBox as CheckBox).isChecked)
            showTotalPrice()
        }
    }

    fun checkAll_None(isChecked: Boolean) {


        if (!isNull())
            return

        mDataCarts?.withIndex()?.forEach { (i, cart) ->
            cart.isChecked = isChecked
            notifyItemChanged(i)
        }

    }

    fun delCart() {
        if (!isNull())
            return

        val iterator = mDataCarts?.iterator()
        while (iterator?.hasNext() as Boolean) {
            val cart = iterator.next()

            if (cart.isChecked) {
                val position = mDataCarts?.indexOf(cart)
                cartProvider?.delete(cart)
                iterator.remove()
                if (position != null) {
                    notifyItemRemoved(position)
                }
            }

        }


    }


}