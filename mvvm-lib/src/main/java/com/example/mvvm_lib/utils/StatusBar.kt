package com.example.mvvm_lib.utils

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.example.mvvm_lib.R

class StatusBar : View {
    private var mStatusBarHeight = 0

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mStatusBarHeight = getStatusHeight(context)
        }
        setBackgroundColor(context.resources.getColor(R.color.transparent))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mStatusBarHeight)
    }

    /**
     * 获得状态栏的高度
     *
     * @param context 上下文
     * @return 高度
     */
    fun getStatusHeight(context: Context): Int {
        var result = 0
        val res = context.resources
        val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId)
        }
        return result
    }
}