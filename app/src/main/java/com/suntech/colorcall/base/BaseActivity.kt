package com.suntech.colorcall.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.suntech.colorcall.view.MainApp

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: T
    protected abstract fun layout(): T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = layout()
        setContentView(binding.root)
        val baseApp = application as MainApp
    }
}