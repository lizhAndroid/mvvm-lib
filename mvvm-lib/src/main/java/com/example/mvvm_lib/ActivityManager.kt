package com.example.mvvm_lib

import android.app.Activity
import java.util.*

class ActivityManager {
    /**
     * 保存所有创建的Activity
     */
    private val activityList: MutableList<Activity> = ArrayList()

    /**
     * 添加Activity
     * @param activity
     */
    fun addActivity(activity: Activity?) {
        if (activity != null) {
            activityList.add(activity)
        }
    }

    /**
     * 移除Activity
     * @param activity
     */
    fun removeActivity(activity: Activity?) {
        if (activity != null) {
            activityList.remove(activity)
        }
    }

    /**
     * 关闭所有Activity
     */
    fun finishAllActivity() {
        for (activity in activityList) {
            activity.finish()
        }
    }

    val top: Activity
        get() = activityList[activityList.size - 1]

    companion object {
        val mInstance: ActivityManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { ActivityManager() }
    }
}