package com.example.duxiaoming.jdshop.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.bean.HomeCategory


/**
 * Created by duxiaoming on 2017/7/16.
 * blog:m78snail.com
 * description:
 */
class HomeCatgoryAdapter(private val mDatas: List<HomeCategory>) : RecyclerView.Adapter<HomeCatgoryAdapter.ViewHolder>() {


    private var mInflater: LayoutInflater? = null
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = mDatas[position]
        holder.textTitle!!.text = category.name
        holder.imageViewBig!!.setImageResource(category.imgBig)
        holder.imageViewSmallTop!!.setImageResource(category.imgSmallTop)
        holder.imageViewSmallBottom!!.setImageResource(category.imgSmallBottom)
    }

    override fun getItemCount(): Int {

        return mDatas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        mInflater = LayoutInflater.from(parent!!.context)
        if (viewType == VIEW_TYPE_R) {
            return ViewHolder(mInflater!!.inflate(R.layout.template_home_cardview2, null))
        } else {
            return ViewHolder(mInflater!!.inflate(R.layout.template_home_cardview, null))
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position % 2 == 0) {
            return VIEW_TYPE_R
        } else return VIEW_TYPE_L
    }

    companion object {
        var VIEW_TYPE_L: Int = 0
        var VIEW_TYPE_R: Int = 1
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textTitle: TextView? = null
        var imageViewBig: ImageView? = null
        var imageViewSmallTop: ImageView? = null
        var imageViewSmallBottom: ImageView? = null

        init {
            textTitle = itemView.findViewById(R.id.text_title) as TextView
            imageViewBig = itemView.findViewById(R.id.imgview_big) as ImageView
            imageViewSmallTop = itemView.findViewById(R.id.imgview_small_top) as ImageView
            imageViewSmallBottom = itemView.findViewById(R.id.imgview_small_bottom) as ImageView

        }
    }
}