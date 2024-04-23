package com.example.mvvmlib.view

import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmlib.ActivityManager
import com.example.mvvmlib.R
import com.example.mvvmlib.view_model.BaseViewModel
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView
import java.lang.reflect.ParameterizedType
import java.util.*

abstract class BaseFragment<VB : ViewDataBinding?, VM : BaseViewModel<*>> : Fragment() {
    private var mLoadingPopupView: LoadingPopupView? = null
    protected var mFragmentList: List<Fragment> = ArrayList()
    protected var mCurrentFragment: Fragment? = null
    var mBinding: VB? = null
    var mViewModel: VM? = null
    private var mContext: Context? = null
    private var mActivity: Activity? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(TAG, "onCreateView: " + javaClass.simpleName)
        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mContext = context
        mActivity = activity
        initVM()
        return if (mBinding == null) {
            val inflate1 = LayoutInflater.from(context).inflate(layoutId, container, false)
            initView(inflate1)
            inflate1
        } else {
            initView(mBinding!!.root)
            mBinding!!.root
        }
    }

    protected abstract fun initView(rootView: View?)
    abstract val layoutId: Int
    protected fun showMsg(msg: CharSequence?) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
    }

    protected fun showLongMsg(msg: CharSequence?) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show()
    }

    /**
     * 跳转页面
     *
     * @param clazz 目标页面
     */
    protected fun goActivity(clazz: Class<*>?) {
        startActivity(Intent(mContext, clazz))
    }

    /**
     * 跳转页面并关闭当前页面
     *
     * @param clazz 目标页面
     */
    protected fun goActivityThenFinish(clazz: Class<*>?) {
        startActivity(Intent(mContext, clazz))
        requireActivity().finish()
    }

    /**
     * 退出应用程序
     */
    protected fun exitApp() {
        ActivityManager.mInstance?.finishAllActivity()
    }
    /**
     * @param message
     * @param isClose true 则点击其他区域弹窗关闭， false 不关闭。
     */
    /**
     * 显示加载弹窗
     */
    protected fun showLoading(message: String? = null, isClose: Boolean = true) {
        mLoadingPopupView =
            XPopup.Builder(mContext).dismissOnTouchOutside(isClose).asLoading(message)
        mLoadingPopupView?.show()
    }

    /**
     * 隐藏加载弹窗
     */
    protected fun dismissLoading() {
        if (mLoadingPopupView != null) {
            mLoadingPopupView!!.dismiss()
        }
    }

    /**
     * 请求外部存储管理 Android11版本时获取文件读写权限时调用 新的方式
     */
    protected fun requestManageExternalStorage(intentActivityResultLauncher: ActivityResultLauncher<Intent?>) {
        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        intent.data = Uri.parse("package:" + mContext!!.packageName)
        intentActivityResultLauncher.launch(intent)
    }

    /**
     * 是否是夜间模式
     *
     * @return
     */
    protected val isNightModel: Boolean
        protected get() {
            val uiModeManager =
                mContext!!.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            return uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
        }

    protected fun showFragment(position: Int, id: Int = R.id.fl_container) {
        if (mFragmentList.size == 0) return
        val fragment = mFragmentList[position] ?: return
        if (mCurrentFragment != null) {
            childFragmentManager.beginTransaction().hide(mCurrentFragment!!)
                .commitAllowingStateLoss()
        }
        mCurrentFragment = fragment
        if (!fragment.isAdded) {
            childFragmentManager.beginTransaction().add(id, fragment).commitAllowingStateLoss()
        } else {
            childFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun initVM() {
        val genericSuperclass = this.javaClass.genericSuperclass
        if (genericSuperclass is ParameterizedType) {
            val actualTypeArguments = genericSuperclass.actualTypeArguments
            val actualTypeArgument = actualTypeArguments[1]
            mViewModel = ViewModelProvider(this).get(actualTypeArgument as Class<VM>)
        }
    }

    companion object {
        private const val TAG = "BaseFragment"
    }
}