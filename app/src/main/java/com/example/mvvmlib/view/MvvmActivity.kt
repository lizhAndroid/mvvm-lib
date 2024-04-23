package com.example.mvvmlib.view

import android.os.Bundle
import android.util.Log
import com.example.mvvmlib.R
import com.example.mvvmlib.databinding.ActivityMvvmBinding
import com.example.mvvmlib.view_model.MVVMViewModel

/**
 * author: lizhihang
 * date: 2023/1/18
 * description:仅作测试
 */
class MvvmActivity : BaseActivity<ActivityMvvmBinding?, MVVMViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: ")
    }

    public override fun initView() {
        setTitle("测试mvvm", resources.getColor(R.color.black))
        mBinding!!.tvTip.text = "欢迎使用mvvm"
        mBinding!!.tvTip.setOnClickListener {
            mViewModel!!.getMessage()
            mViewModel!!.message.observe(this) { msg: String? -> showMsg(msg) }
        }
    }

    override val layoutId: Int = R.layout.activity_mvvm

    override fun hideLeft(): Boolean {
        return true
    }

    companion object {
        private const val TAG = "MvvmActivity"
    }
}