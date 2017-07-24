package com.example.duxiaoming.jdshop.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.TintTypedArray
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.duxiaoming.jdshop.R

class JDToolBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : Toolbar(context, attrs, defStyleAttr) {

    private var mInflater: LayoutInflater? = null
    private var mView: View? = null
    private var mTextTitle: TextView? = null
    private var mSearchView: EditText? = null
    var mRightButton: Button? = null
        private set

    init {
        initView()
        setContentInsetsRelative(10, 10)

        if (attrs != null) {
            val a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.JDToolBar, defStyleAttr, 0)
            val rightIcon = a.getDrawable(R.styleable.JDToolBar_rightButtonIcon)
            if (rightIcon != null) {
                setRightButtonIcon(rightIcon)
            }


            val isShowSearchView = a.getBoolean(R.styleable.JDToolBar_isShowSearchView, false)

            if (isShowSearchView) {

                showSearchView()
                hideTitleView()

            }


            val rightButtonText = a.getText(R.styleable.JDToolBar_rightButtonText)
            if (rightButtonText != null) {
                setRightButtonText(rightButtonText)
            }



            a.recycle()
        }
    }

    private fun initView() {
        if (mView == null) {
            mInflater = LayoutInflater.from(context)
            mView = mInflater!!.inflate(R.layout.toolbar, null)
            mTextTitle = mView!!.findViewById(R.id.toolbar_title) as TextView
            mSearchView = mView!!.findViewById(R.id.toolbar_searchview) as EditText
            mRightButton = mView!!.findViewById(R.id.toolbar_rightButton) as Button

            val lp: LayoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL)
            addView(mView, lp)
        }
    }


    fun setRightButtonIcon(icon: Drawable) {

        if (mRightButton != null) {

            mRightButton!!.background = icon
            mRightButton!!.visibility = View.VISIBLE
        }

    }

    fun setRightButtonIcon(icon: Int) {

        setRightButtonIcon(resources.getDrawable(icon))
    }


    fun setRightButtonOnClickListener(li: View.OnClickListener) {

        mRightButton!!.setOnClickListener(li)
    }

    fun setRightButtonText(text: CharSequence) {
        mRightButton!!.text = text
        mRightButton!!.visibility = View.VISIBLE
    }

    fun setRightButtonText(id: Int) {
        setRightButtonText(resources.getString(id))
    }


    override fun setTitle(resId: Int) {
        title = context.getText(resId)
    }

    override fun setTitle(title: CharSequence?) {
        initView()
        if (mTextTitle != null) {
            mTextTitle!!.text = title
            showTitleView()
        }
    }

    fun showSearchView() {

        if (mSearchView != null)
            mSearchView!!.visibility = View.VISIBLE

    }


    fun hideSearchView() {
        if (mSearchView != null)
            mSearchView!!.visibility = View.GONE
    }

    fun showTitleView() {
        if (mTextTitle != null)
            mTextTitle!!.visibility = View.VISIBLE
    }


    fun hideTitleView() {
        if (mTextTitle != null)
            mTextTitle!!.visibility = View.GONE

    }


    fun getRightButton(): Button? {

        return this.mRightButton
    }

}
