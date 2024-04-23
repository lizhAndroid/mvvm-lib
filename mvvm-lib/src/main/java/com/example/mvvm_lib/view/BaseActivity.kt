package com.example.mvvm_lib.view
import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm_lib.ActivityManager
import com.example.mvvm_lib.R
import com.example.mvvm_lib.utils.TitleBar
import com.example.mvvm_lib.view_model.BaseViewModel

import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView
import java.lang.reflect.ParameterizedType
import java.util.*
import com.example.mvvm_lib.databinding.ActivityBaseTitleBinding

abstract class BaseActivity<VB : ViewDataBinding?, VM : BaseViewModel<*>> : AppCompatActivity() {
    private var mLoadingPopupView: LoadingPopupView? = null
    private var mFragmentList: List<Fragment> = ArrayList<Fragment>()
    private var mCurrentFragment: Fragment? = null
    private var mBaseDataBinding: ActivityBaseTitleBinding? = null

    @JvmField
    var mBinding: VB? = null

    @JvmField
    var mViewModel: VM? = null
    private var mContext: Context? = null
    private var mActivity: Activity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        mActivity = this
        mLoadingPopupView = XPopup.Builder(this).asLoading()
        ActivityManager.mInstance.addActivity(this)
        setStatusBar(true)
        mBaseDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_base_title)
        val inflate = LayoutInflater.from(this).inflate(layoutId, null)
        mBaseDataBinding!!.llBaseView.addView(inflate)
        initVM()
        mBinding = DataBindingUtil.bind(inflate)
        mBaseDataBinding!!.includeTitle.titleBar.setLeftVisible(!hideLeft())
        mBaseDataBinding!!.includeTitle.titleBar.setRightVisible(!hideRight())
        mBaseDataBinding!!.includeTitle.titleBar.visibility =
            if (isShowTitle()) View.VISIBLE else View.GONE
        initView()
    }

    protected abstract fun initView()
    protected abstract val layoutId: Int
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
        finish()
    }

    protected fun isShowStatusBar(isShow: Boolean) {
        if (isShow) {
            mBaseDataBinding!!.includeTitle.stStatus.visibility = View.VISIBLE
        } else {
            mBaseDataBinding!!.includeTitle.stStatus.visibility = View.GONE
        }
    }

    /**
     * 状态栏文字图标颜色
     *
     * @param dark 深色 false 为浅色
     */
    protected open fun setStatusBar(dark: Boolean) {
        val decor = window.decorView
        if (dark) {
            decor.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decor.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    /**
     * 退出应用程序
     */
    protected fun exitApp() {
        ActivityManager.mInstance.finishAllActivity()
    }

    /**
     * 显示加载弹窗
     */
    protected fun showLoading(message: String? = null) {
        mLoadingPopupView!!.setTitle(message)
        mLoadingPopupView!!.show()
    }

    /**
     * 隐藏加载弹窗
     */
    protected fun dismissLoading() {
        if (mLoadingPopupView != null) {
            mLoadingPopupView!!.dismiss()
        }
    }

    protected fun setTitleBarVisible(isShow: Boolean) {
        mBaseDataBinding!!.includeTitle.titleView.visibility =
            if (isShow) View.VISIBLE else View.GONE
    }

    /**
     * 请求外部存储管理 Android11版本时获取文件读写权限时调用 新的方式
     */
    @RequiresApi(Build.VERSION_CODES.R)
    protected fun requestManageExternalStorage(intentActivityResultLauncher: ActivityResultLauncher<Intent?>) {
        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        intent.data = Uri.parse("package:$packageName")
        intentActivityResultLauncher.launch(intent)
    }

    /**
     * 是否是夜间模式
     *
     * @return
     */
    protected val isNightModel: Boolean
        get() {
            val uiModeManager = mContext!!.getSystemService(UI_MODE_SERVICE) as UiModeManager
            return uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
        }

    /**
     * 设置标题栏左边
     */
    protected fun setTitleLeft(
        leftStr: String?,
        leftTextColor: Int,
        leftImg: Int,
        onLeftClickListener: TitleBar.OnLeftClickListener?
    ) {
        mBaseDataBinding!!.includeTitle.titleBar.setLeftVisible(true).setLeftImage(leftImg)
            .setLeftText(leftStr).setLeftTextColor(leftTextColor)
            .setOnLeftClickListener(onLeftClickListener)
    }

    protected fun setTitle(title: String?, color: Int) {
        mBaseDataBinding!!.includeTitle.titleBar.setCenterText(title).setCenterTextColor(color)
    }

    protected fun setRight(
        rightStr: String?,
        rightTextColor: Int,
        rightImg: Int,
        onRightClickListener: TitleBar.OnRightClickListener?
    ) {
        mBaseDataBinding!!.includeTitle.titleBar.setOnRightClickListener(onRightClickListener)
            .setRightText(rightStr)
            .setRightImage(rightImg).setRightTextColor(rightTextColor)
    }

    protected open fun hideLeft(): Boolean {
        return false
    }

    protected open fun hideRight(): Boolean {
        return true
    }

    protected open fun isShowTitle(): Boolean {
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityManager.mInstance.removeActivity(this)
    }

    protected fun initFragment() {
        if (mFragmentList.isEmpty()) return
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        if (supportFragmentManager.fragments.size > 0) {
            val list: ArrayList<Fragment> = ArrayList<Fragment>(
                supportFragmentManager.fragments
            )
            for (f in list) {
                transaction.remove(f)
            }
            supportFragmentManager.fragments
        }
        for (i in mFragmentList.indices) {
            if (!mFragmentList[i].isAdded) {
                transaction.add(R.id.fl_container, mFragmentList[i])
            }
        }
        transaction.commit()
        switchTo(0)
    }

    /**
     * 通过事务管理，显示当前Fragment
     *
     * @param position
     */
    protected open fun switchTo(position: Int) {
        if (position > mFragmentList.size) {
            return
        }
        val transaction = supportFragmentManager.beginTransaction()
        for (i in mFragmentList.indices) {
            val currentFragment: Fragment = mFragmentList[i]
            if (i == position) {
                transaction.show(currentFragment)
                mCurrentFragment = currentFragment
            } else {
                transaction.hide(currentFragment)
            }
        }
        transaction.commitAllowingStateLoss()
    }

    private fun initVM() {
        val genericSuperclass = this.javaClass.genericSuperclass
        if (genericSuperclass is ParameterizedType) {
            val actualTypeArguments = genericSuperclass.actualTypeArguments
            val actualTypeArgument = actualTypeArguments[1]
            mViewModel = ViewModelProvider(this)[actualTypeArgument as Class<VM>]
        }
    }
}