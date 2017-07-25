package com.example.duxiaoming.jdshop.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.duxiaoming.jdshop.R


/**
 * Created by [菜鸟窝](http://www.cniao5.com)
 * 一个专业的Android开发在线教育平台
 */
class ClearEditText : AppCompatEditText, View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {


    private var mClearTextIcon: Drawable? = null
    private var mOnFocusChangeListener: View.OnFocusChangeListener? = null
    private var mOnTouchListener: View.OnTouchListener? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {

        val drawable = ContextCompat.getDrawable(context, R.drawable.icon_delete_32)
        val wrappedDrawable = DrawableCompat.wrap(drawable) //Wrap the drawable so that it can be tinted pre Lollipop
        DrawableCompat.setTint(wrappedDrawable, currentHintTextColor)
        mClearTextIcon = wrappedDrawable
        mClearTextIcon!!.setBounds(0, 0, mClearTextIcon!!.intrinsicHeight, mClearTextIcon!!.intrinsicHeight)
        setClearIconVisible(false)
        super.setOnTouchListener(this)
        super.setOnFocusChangeListener(this)
        addTextChangedListener(this)
    }

    override fun setOnFocusChangeListener(l: View.OnFocusChangeListener) {
        mOnFocusChangeListener = l
    }

    override fun setOnTouchListener(l: View.OnTouchListener) {
        mOnTouchListener = l
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (hasFocus) {
            setClearIconVisible(text.isNotEmpty())
        } else {
            setClearIconVisible(false)
        }
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener!!.onFocusChange(v, hasFocus)
        }
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val x = motionEvent.x.toInt()
        if (mClearTextIcon!!.isVisible && x > width - paddingRight - mClearTextIcon!!.intrinsicWidth) {
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                error = null
                setText("")
            }
            return true
        }
        return mOnTouchListener != null && mOnTouchListener!!.onTouch(view, motionEvent)
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        if (isFocused) {
            setClearIconVisible(text.isNotEmpty())
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun afterTextChanged(s: Editable) {

    }

    private fun setClearIconVisible(visible: Boolean) {
        mClearTextIcon!!.setVisible(visible, false)
        val compoundDrawables = compoundDrawables
        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                if (visible) mClearTextIcon else null,
                compoundDrawables[3])
    }

}
