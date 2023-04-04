package com.suntech.colorcall.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.app.NotificationManagerCompat
import com.suntech.colorcall.api.BASE_URL
import com.suntech.colorcall.constant.AppConstant.FILE_SETTING
import com.suntech.colorcall.helper.PreferencesHelper
import com.suntech.colorcall.model.MyObjectBox
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.objectbox.BoxStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun proviceContext(application: Application): Context = application

    @Singleton
    @Provides
    fun providesObjectBox(context: Context): BoxStore = MyObjectBox.builder().androidContext(context.applicationContext).build()

    @Singleton
    @Provides
    fun appPreferencesHelper(mPrefs: SharedPreferences) = PreferencesHelper(mPrefs)

    @Singleton
    @Provides
    fun sharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(FILE_SETTING, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun retrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun okHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun notificationManger(context: Context) = NotificationManagerCompat.from(context)

}