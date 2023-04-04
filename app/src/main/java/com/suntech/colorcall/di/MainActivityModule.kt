package com.suntech.colorcall.di

import android.content.Context
import android.content.res.AssetManager
import com.suntech.colorcall.api.ApiServices
import com.suntech.colorcall.model.Contact
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import io.objectbox.Box
import io.objectbox.BoxStore
import retrofit2.Retrofit

@Module
@InstallIn(ActivityComponent::class)
object MainActivityModule {
    @Provides
    fun providesAPIServices(retrofit: Retrofit): ApiServices = retrofit.create(ApiServices::class.java)

    @Provides
    fun provideBox(boxStore: BoxStore): Box<Contact> = boxStore.boxFor(Contact::class.java)

    @Provides
    fun provideAssetsManager(context: Context): AssetManager {
        return context.assets
    }
}