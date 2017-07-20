package com.example.duxiaoming.jdshop.adapter

import android.content.Context
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.bean.Category

/**
 * Created by duxiaoming on 2017/7/20.
 * blog:m78snail.com
 * description:
 */
class CategoryAdapter(context: Context, datas: MutableList<Category>) : SimpleAdapter<Category>(context, R.layout.template_single_text, datas) {
    override fun convert(viewHoder: BaseViewHolder, item: Category) {
        viewHoder.getTextView(R.id.textView).text = item.name
    }
}