package com.suntech.colorcall.helper

import android.content.SharedPreferences
import javax.inject.Inject


class PreferencesHelper @Inject constructor(val preferencesHelper: SharedPreferences) : PreferencesInterface {
    companion object {
        private const val DEFAULT_VALUE_LONG: Long = 0
        private const val DEFAULT_VALUE_INTEGER = 0
        private const val DEFAULT_VALUE_FLOAT = 0
    }

    override fun save(key: String?, value: Boolean) {
        preferencesHelper.edit().putBoolean(key, value).apply()
    }

    override fun save(key: String?, value: String?) {
        preferencesHelper.edit().putString(key, value).apply()
    }

    override fun save(key: String?, value: Float) {
        preferencesHelper.edit().putFloat(key, value).apply()
    }

    override fun save(key: String?, value: Int) {
        preferencesHelper.edit().putInt(key, value).apply()
    }

    override fun save(key: String?, value: Long) {
        preferencesHelper.edit().putLong(key, value).apply()
    }

    override fun getBoolean(key: String?): Boolean {
        return preferencesHelper.getBoolean(key, false)
    }

    override fun getString(key: String?): String? {
        return preferencesHelper.getString(key, null)
    }

    override fun getLong(key: String?): Long {
        return preferencesHelper.getLong(key, DEFAULT_VALUE_LONG)
    }

    override fun getInt(key: String?): Int {
        return preferencesHelper.getInt(key, DEFAULT_VALUE_INTEGER)
    }

    override fun getFloat(key: String?): Float {
        return preferencesHelper.getFloat(key, DEFAULT_VALUE_FLOAT.toFloat())
    }

    override fun remove(key: String?) {
        preferencesHelper.edit().remove(key).apply()
    }
}

interface PreferencesInterface {
    fun save(key: String?, value: Boolean)
    fun save(key: String?, value: String?)
    fun save(key: String?, value: Float)
    fun save(key: String?, value: Int)
    fun save(key: String?, value: Long)
    fun getBoolean(key: String?): Boolean
    fun getString(key: String?): String?
    fun getLong(key: String?): Long
    fun getInt(key: String?): Int
    fun getFloat(key: String?): Float
    fun remove(key: String?)
}