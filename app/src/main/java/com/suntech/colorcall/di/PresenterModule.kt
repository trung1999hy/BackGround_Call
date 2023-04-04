package com.suntech.colorcall.di

import com.suntech.colorcall.view.activity.apply.ApplyContract
import com.suntech.colorcall.view.activity.apply.ApplyPresenter
import com.suntech.colorcall.view.activity.button.ButtonContract
import com.suntech.colorcall.view.activity.button.ButtonPresenter
import com.suntech.colorcall.view.activity.download.DownloadContract
import com.suntech.colorcall.view.activity.download.DownloadPresenter
import com.suntech.colorcall.view.activity.history.HistoryContract
import com.suntech.colorcall.view.activity.history.HistoryPresenter
import com.suntech.colorcall.view.activity.incoming.InComingContract
import com.suntech.colorcall.view.activity.incoming.InComingPresenter
import com.suntech.colorcall.view.activity.incoming.old.OldComingContract
import com.suntech.colorcall.view.activity.incoming.old.OldComingPresenter
import com.suntech.colorcall.view.activity.main.MainContract
import com.suntech.colorcall.view.activity.main.MainPresenter
import com.suntech.colorcall.view.activity.select.SelectContract
import com.suntech.colorcall.view.activity.select.SelectPresenter
import com.suntech.colorcall.view.activity.setting.SettingContract
import com.suntech.colorcall.view.activity.setting.SettingPresenter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class PresenterModule {
    @Binds
    abstract fun providesSettingPresenter(presenter: SettingPresenter): SettingContract.Presenter

    @Binds
    abstract fun providesDownloadPresenter(presenter: DownloadPresenter): DownloadContract.Presenter

    @Binds
    abstract fun providesMainPresenter(presenter: MainPresenter): MainContract.Presenter

    @Binds
    abstract fun providesSelectPresenter(presenter: SelectPresenter): SelectContract.Presenter

    @Binds
    abstract fun providesInComingPresenter(presenter: InComingPresenter): InComingContract.Presenter

    @Binds
    abstract fun providesApplyPresenter(presenter: ApplyPresenter): ApplyContract.Presenter

    @Binds
    abstract fun providesHistoryPresenter(presenter: HistoryPresenter): HistoryContract.Presenter

    @Binds
    abstract fun provideButtonPresenter(presenter: ButtonPresenter): ButtonContract.Presenter

    @Binds
    abstract fun provideOldApiComingCall(presenter: OldComingPresenter): OldComingContract.Presenter
}