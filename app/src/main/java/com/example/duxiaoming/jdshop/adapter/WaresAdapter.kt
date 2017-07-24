package com.example.duxiaoming.jdshop.adapter

import android.content.Context
import android.net.Uri
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.bean.Wares
import com.facebook.drawee.view.SimpleDraweeView


/**
 * Created by duxiaoming on 2017/7/21.
 * blog:m78snail.com
 * description:
 */
class WaresAdapter(context: Context, mData: MutableList<Wares>) : SimpleAdapter<Wares>(context, R.layout.template_grid_wares, mData) {
    override fun convert(viewHoder: BaseViewHolder, item: Wares) {

        viewHoder.getTextView(R.id.text_title)?.text = item.name
        viewHoder.getTextView(R.id.text_price)?.text = "￥" + item.price
        val draweeView = viewHoder.getView(R.id.drawee_view) as SimpleDraweeView
        draweeView.setImageURI(Uri.parse(item.imgUrl))

    }

}