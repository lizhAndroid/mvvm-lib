package com.example.mvvmlib.view_model

import androidx.lifecycle.ViewModel
import com.example.mvvmlib.repository.BaseRepository
import java.lang.reflect.ParameterizedType

open class BaseViewModel<RP : BaseRepository?> : ViewModel() {
    val repository: RP?
        get() {
            val genericSuperclass = this.javaClass.genericSuperclass
            if (genericSuperclass is ParameterizedType) {
                val actualTypeArguments = genericSuperclass.actualTypeArguments
                val actualTypeArgument = actualTypeArguments[0]
                val repositoryClass = actualTypeArgument as Class<RP>
                var rp: RP? = null
                try {
                    rp = repositoryClass.newInstance()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: InstantiationException) {
                    e.printStackTrace()
                }
                return rp
            }
            return null
        }
}