package com.example.duxiaoming.jdshop.adapter

import android.content.Context
import android.net.Uri
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.bean.ShoppingCart
import com.facebook.drawee.view.SimpleDraweeView


/**
 * Created by duxiaoming on 2017/7/29.
 * blog:m78snail.com
 * description:
 */
class WareOrderAdapter(context: Context, private var datasTemp: MutableList<ShoppingCart>?) : SimpleAdapter<ShoppingCart>(context, R.layout.template_order_wares, datasTemp) {
    override fun convert(viewHoder: BaseViewHolder, item: ShoppingCart) {
        val draweeView = viewHoder.getView(R.id.drawee_view) as SimpleDraweeView
        draweeView.setImageURI(Uri.parse(item.imgUrl))
    }

    fun getTotalPrice(): Float {

        var sum = 0f
        if (!isNull())
            return sum

        datasTemp?.forEach { cart -> sum += cart.count * cart.price!! }

        return sum

    }


    private fun isNull(): Boolean {
        if (datasTemp != null) {
            return datasTemp!!.size > 0
        }
        return true
    }

}