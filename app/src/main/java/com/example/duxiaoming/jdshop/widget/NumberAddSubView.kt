package com.example.duxiaoming.jdshop.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.widget.TintTypedArray
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.duxiaoming.jdshop.R


class NumberAddSubView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var mEtxtNum: TextView? = null
    private var mBtnAdd: Button? = null
    private var mBtnSub: Button? = null

    private var onButtonClickListener: OnButtonClickListener? = null


    private val mInflater: LayoutInflater


    private var value: Int = 0
    private var minValue: Int = 0
    private var maxValue = DEFUALT_MAX

    init {

        mInflater = LayoutInflater.from(context)
        initView()

        if (attrs != null) {

            val a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.NumberAddSubView, defStyleAttr, 0)


            val `val` = a.getInt(R.styleable.NumberAddSubView_value, 0)
            setValue(`val`)

            val maxVal = a.getInt(R.styleable.NumberAddSubView_maxValue, 0)
            if (maxVal != 0)
                setMaxValue(maxVal)

            val minVal = a.getInt(R.styleable.NumberAddSubView_minValue, 0)
            setMinValue(minVal)

            val etBackground = a.getDrawable(R.styleable.NumberAddSubView_editBackground)
            if (etBackground != null)
                setEditTextBackground(etBackground)


            val buttonAddBackground = a.getDrawable(R.styleable.NumberAddSubView_buttonAddBackgroud)
            if (buttonAddBackground != null)
                setButtonAddBackgroud(buttonAddBackground)

            val buttonSubBackground = a.getDrawable(R.styleable.NumberAddSubView_buttonSubBackgroud)
            if (buttonSubBackground != null)
                setButtonSubBackgroud(buttonSubBackground)




            a.recycle()
        }
    }


    private fun initView() {


        val view = mInflater.inflate(R.layout.widet_num_add_sub, this, true)

        mEtxtNum = view.findViewById(R.id.etxt_num) as TextView
        mEtxtNum!!.inputType = InputType.TYPE_NULL
        mEtxtNum!!.keyListener = null



        mBtnAdd = view.findViewById(R.id.btn_add) as Button
        mBtnSub = view.findViewById(R.id.btn_sub) as Button

        mBtnAdd!!.setOnClickListener(this)
        mBtnSub!!.setOnClickListener(this)


    }


    override fun onClick(v: View) {
        if (v.id == R.id.btn_add) {

            numAdd()

            if (onButtonClickListener != null) {
                onButtonClickListener!!.onButtonAddClick(v, this.value)
            }
        } else if (v.id == R.id.btn_sub) {
            numSub()
            if (onButtonClickListener != null) {
                onButtonClickListener!!.onButtonSubClick(v, this.value)
            }

        }
    }


    private fun numAdd() {


        getValue()

        if (this.value <= maxValue)
            this.value = +this.value + 1

        mEtxtNum!!.text = value.toString() + ""
    }


    private fun numSub() {


        getValue()

        if (this.value > minValue)
            this.value = this.value - 1

        mEtxtNum!!.text = value.toString() + ""
    }


    fun getValue(): Int {

        val value = mEtxtNum!!.text.toString()

        this.value = Integer.parseInt(value)

        return this.value
    }

    fun setValue(value: Int) {
        mEtxtNum!!.text = value.toString() + ""
        this.value = value
    }


    fun setMaxValue(maxValue: Int) {
        this.maxValue = maxValue
    }

    fun setMinValue(minValue: Int) {
        this.minValue = minValue
    }


    fun setEditTextBackground(drawable: Drawable) {

        mEtxtNum!!.setBackgroundDrawable(drawable)

    }


    fun setEditTextBackground(drawableId: Int) {

        setEditTextBackground(resources.getDrawable(drawableId))

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun setButtonAddBackgroud(backgroud: Drawable) {
        this.mBtnAdd!!.background = backgroud
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun setButtonSubBackgroud(backgroud: Drawable) {
        this.mBtnSub!!.background = backgroud
    }


    fun setOnButtonClickListener(onButtonClickListener: OnButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener
    }

    interface OnButtonClickListener {

        fun onButtonAddClick(view: View, value: Int)
        fun onButtonSubClick(view: View, value: Int)

    }

    companion object {


        val TAG = "NumberAddSubView"
        val DEFUALT_MAX = 1000
    }


}
