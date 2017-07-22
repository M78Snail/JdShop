package com.example.duxiaoming.jdshop.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.bean.Wares
import com.example.duxiaoming.jdshop.utils.CartProvider
import com.facebook.drawee.view.SimpleDraweeView


/**
 * Created by duxiaoming on 2017/7/20.
 * blog:m78snail.com
 * description:
 */
class HWAdatper(context: Context, datas: MutableList<Wares>) : SimpleAdapter<Wares>(context, R.layout.template_hot_wares, datas) {

    private var provider: CartProvider = CartProvider(context)

    override fun convert(viewHoder: BaseViewHolder, item: Wares) {
        val draweeView = viewHoder.getView(R.id.drawee_view) as SimpleDraweeView

        draweeView.setImageURI(Uri.parse(item.imgUrl))

        viewHoder.getTextView(R.id.text_title).text = item.name
        viewHoder.getTextView(R.id.text_price).text = "￥ " + item.price


        viewHoder.getButton(R.id.btn_add).setOnClickListener {
            provider.put(item)
            Toast.makeText(context, "已经加入购物车", Toast.LENGTH_SHORT).show()
        }


    }


}