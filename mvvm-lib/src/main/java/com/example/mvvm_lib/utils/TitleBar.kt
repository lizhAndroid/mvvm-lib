package com.example.mvvm_lib.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.example.mvvm_lib.ActivityManager
import com.example.mvvm_lib.R
import com.example.mvvm_lib.databinding.TitleBarBinding

class TitleBar : RelativeLayout, View.OnClickListener {
    private var onLeftClickListener: OnLeftClickListener? = null
    private var onRightClickListener: OnRightClickListener? = null
    private var rlLayoutLeft: RelativeLayout? = null
    private var llLayoutRight: LinearLayout? = null
    private var tvTitle: TextView? = null
    private var textRight: TextView? = null
    private var imageRight: ImageView? = null
    private var imageLeft: ImageView? = null
    private var tvLeft: TextView? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {

        val binding: TitleBarBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.title_bar, this, false)
        rlLayoutLeft = binding.rlLeft
        llLayoutRight = binding.llRight
        imageRight = binding.imageRight
        textRight = binding.tvBarRight
        imageLeft = binding.imageLeft
        tvTitle = binding.tvTitle
        tvLeft = binding.tvLeft
        tvLeft?.setOnClickListener(this)
        imageLeft?.setOnClickListener(this)
        imageRight?.setOnClickListener(this)
        textRight?.setOnClickListener(this)
        setBackground(context.resources.getColor(R.color.white))
    }

    override fun onClick(v: View) {
        if (v.id == R.id.image_left) {
            if (onLeftClickListener != null) {
                onLeftClickListener!!.onClickLeft()
            } else {
                ActivityManager.mInstance.top.finish()
            }
        } else if (v.id == R.id.image_right) {
            if (onRightClickListener != null) {
                onRightClickListener!!.onClickRight()
            }
        } else if (v.id == R.id.tv_left) {
            if (onLeftClickListener != null) {
                onLeftClickListener!!.onClickLeftText()
            } else {
                ActivityManager.mInstance.top.finish()
            }
        } else if (v.id == R.id.tv_bar_right) {
            if (onRightClickListener != null) {
                onRightClickListener!!.onClickRightText()
            }
        }
    }

    /**
     * 左侧布局显示隐藏
     *
     * @param isVisible 是否显示
     */
    fun setLeftVisible(isVisible: Boolean): TitleBar {
        if (isVisible) {
            rlLayoutLeft!!.visibility = VISIBLE
        } else {
            rlLayoutLeft!!.visibility = GONE
        }
        return this
    }

    /**
     * 设置背景颜色
     *
     * @param color 颜色
     */
    private fun setBackground(color: Int): TitleBar {
        setBackgroundColor(color)
        return this
    }

    /**
     * 右侧布局显示隐藏
     *
     * @param isVisible 是否隐藏
     */
    fun setRightVisible(isVisible: Boolean): TitleBar {
        if (isVisible) {
            llLayoutRight!!.visibility = VISIBLE
        } else {
            llLayoutRight!!.visibility = GONE
        }
        return this
    }

    /**
     * 设置右侧文本内容
     *
     * @param text 文本
     */
    fun setRightText(text: String?): TitleBar {
        llLayoutRight!!.visibility = VISIBLE
        textRight!!.text = text
        return this
    }

    /**
     * 设置右侧文本大小
     *
     * @param size 尺寸
     */
    fun setRightTextSize(size: Int): TitleBar {
        textRight!!.textSize = size.toFloat()
        return this
    }

    /**
     * 设置右侧文本颜色
     *
     * @param color 颜色
     */
    fun setRightTextColor(color: Int): TitleBar {
        textRight!!.setTextColor(color)
        return this
    }

    /**
     * 设置右侧图片
     *
     * @param id 图片id
     */
    fun setRightImage(id: Int): TitleBar {
        imageRight!!.setImageResource(id)
        return this
    }

    /**
     * 设置左侧图片
     *
     * @param id 图片id
     */
    fun setLeftImage(id: Int): TitleBar {
        imageLeft!!.setImageResource(id)
        return this
    }

    fun setLeftText(content: String?): TitleBar {
        tvLeft!!.text = content
        return this
    }

    fun setLeftTextColor(color: Int): TitleBar {
        tvLeft!!.setTextColor(color)
        return this
    }

    /**
     * 设置中间标题文本内容
     *
     * @param text 文本
     */
    fun setCenterText(text: String?): TitleBar {
        tvTitle!!.text = text
        return this@TitleBar
    }

    /**
     * 设置中间标题文本颜色
     *
     * @param color 颜色
     */
    fun setCenterTextColor(color: Int): TitleBar {
        tvTitle!!.setTextColor(color)
        return this
    }

    fun setOnLeftClickListener(onLeftClickListener: OnLeftClickListener?): TitleBar {
        this.onLeftClickListener = onLeftClickListener
        return this
    }

    fun setOnRightClickListener(onRightClickListener: OnRightClickListener?): TitleBar {
        this.onRightClickListener = onRightClickListener
        return this
    }

    interface OnLeftClickListener {
        fun onClickLeft() {
            ActivityManager.mInstance.top.finish()
        }

        fun onClickLeftText() {
            ActivityManager.mInstance.top.finish()
        }
    }

    interface OnRightClickListener {
        fun onClickRight()
        fun onClickRightText()
    }
}