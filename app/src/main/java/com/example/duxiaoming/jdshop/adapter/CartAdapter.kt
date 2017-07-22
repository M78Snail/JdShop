package com.example.duxiaoming.jdshop.adapter

import android.content.Context
import android.net.Uri
import android.widget.CheckBox
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.bean.ShoppingCart
import com.example.duxiaoming.jdshop.widget.NumberAddSubView
import com.facebook.drawee.view.SimpleDraweeView


/**
 * Created by duxiaoming on 2017/7/22.
 * blog:m78snail.com
 * description:
 */
class CartAdapter(context: Context, datas: MutableList<ShoppingCart>?) : SimpleAdapter<ShoppingCart>(context, R.layout.template_cart, datas) {
    override fun convert(viewHoder: BaseViewHolder, item: ShoppingCart) {
        viewHoder.getTextView(R.id.text_title).text = item.name
        viewHoder.getTextView(R.id.text_price).text = "￥" + item.price
        (viewHoder.getView(R.id.drawee_view) as SimpleDraweeView).setImageURI(Uri.parse(item.imgUrl))

        val checkBox = viewHoder.getView(R.id.checkbox) as CheckBox
        checkBox.isChecked = item.isChecked


        (viewHoder.getView(R.id.num_control) as NumberAddSubView).setValue(item.count)
    }


}