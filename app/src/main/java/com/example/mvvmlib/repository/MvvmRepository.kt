package com.example.mvvmlib.repository

import com.example.mvvmlib.view_model.SingleLiveEvent

class MvvmRepository : BaseRepository() {
    fun getMessage(message: SingleLiveEvent<String?>) {
        message.postValue("欢迎使用mvvm")
    }
}