package com.example.mvvmlib.view_model

import com.example.mvvmlib.repository.MvvmRepository


/**
 * author: lizhihang
 * date: 2023/1/18
 * description:仅作测试
 */
class MVVMViewModel : BaseViewModel<MvvmRepository>() {
    var message:SingleLiveEvent<String?> = SingleLiveEvent()
    fun getMessage() {
        repository?.getMessage(message)
    }
}