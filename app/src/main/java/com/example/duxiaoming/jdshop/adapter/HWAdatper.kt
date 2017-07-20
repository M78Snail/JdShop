package com.example.duxiaoming.jdshop.adapter

import android.content.Context
import android.net.Uri
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.bean.Wares
import com.facebook.drawee.view.SimpleDraweeView



/**
 * Created by duxiaoming on 2017/7/20.
 * blog:m78snail.com
 * description:
 */
class HWAdatper (context:Context,datas:MutableList<Wares>) :SimpleAdapter<Wares>(context, R.layout.template_hot_wares,datas) {
    override fun convert(viewHolder: BaseViewHolder, wares: Wares) {
        val draweeView = viewHolder.getView(R.id.drawee_view) as SimpleDraweeView

        draweeView.setImageURI(Uri.parse(wares.imgUrl))

        viewHolder.getTextView(R.id.text_title).text = wares.name
        viewHolder.getTextView(R.id.text_price).text = "￥ " + wares.price
    }


}