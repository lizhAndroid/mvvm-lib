package com.example.mvvmlib.repository

import com.example.mvvm_lib.repository.BaseRepository
import com.example.mvvm_lib.view_model.SingleLiveEvent

class MvvmRepository : BaseRepository() {
    fun getMessage(message: SingleLiveEvent<String?>) {
        message.postValue("欢迎使用mvvm")
    }
}