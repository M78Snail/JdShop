package com.example.duxiaoming.jdshop.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.bean.Campaign
import com.example.duxiaoming.jdshop.bean.HomeCampaign
import com.squareup.picasso.Picasso


/**
 * Created by duxiaoming on 2017/7/16.
 * blog:m78snail.com
 * description:
 */
class HomeCatgoryAdapter(val mDatas: List<HomeCampaign>, private val mContext: Context) : RecyclerView.Adapter<HomeCatgoryAdapter.ViewHolder>() {


    private var mInflater: LayoutInflater? = null

    private var mListener: OnCampaignClickListener? = null
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = mDatas[position]
        holder.textTitle!!.text = category.title
        Picasso.with(mContext).load(category.cpOne.imgUrl).into(holder.imageViewBig)
        Picasso.with(mContext).load(category.cpTwo.imgUrl).into(holder.imageViewSmallTop)
        Picasso.with(mContext).load(category.cpThree.imgUrl).into(holder.imageViewSmallBottom)


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


    fun setOnCampaignClickListener(listener: OnCampaignClickListener) {

        this.mListener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View) {
            if (mListener != null) {

                anim(v)
            }

        }

        var textTitle: TextView? = null
        var imageViewBig: ImageView? = null
        var imageViewSmallTop: ImageView? = null
        var imageViewSmallBottom: ImageView? = null

        init {
            textTitle = itemView.findViewById(R.id.text_title) as TextView
            imageViewBig = itemView.findViewById(R.id.imgview_big) as ImageView
            imageViewSmallTop = itemView.findViewById(R.id.imgview_small_top) as ImageView
            imageViewSmallBottom = itemView.findViewById(R.id.imgview_small_bottom) as ImageView

            imageViewBig!!.setOnClickListener(this)
            imageViewSmallTop!!.setOnClickListener(this)
            imageViewSmallBottom!!.setOnClickListener(this)
        }

        private fun anim(v: View) {

            val animator = ObjectAnimator.ofFloat(v, "rotationX", 0.0f, 360.0f)
                    .setDuration(200)
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {

                    val campaign: HomeCampaign = mDatas[layoutPosition]

                    when (v.id) {

                        R.id.imgview_big -> mListener?.onClick(v, campaign.cpOne)

                        R.id.imgview_small_top -> mListener?.onClick(v, campaign.cpTwo)

                        R.id.imgview_small_bottom -> mListener?.onClick(v, campaign.cpThree)
                    }

                }
            })
            animator.start()
        }

    }

    interface OnCampaignClickListener {
        fun onClick(view: View, campaign: Campaign)
    }
}