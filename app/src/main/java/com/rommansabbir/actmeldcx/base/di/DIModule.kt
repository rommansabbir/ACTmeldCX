package com.rommansabbir.actmeldcx.base.di

import com.rommansabbir.actmeldcx.data.LocalHistoryRepository
import com.rommansabbir.actmeldcx.data.LocalHistoryRepositoryImpl
import com.rommansabbir.actmeldcx.base.permission.PermissionManager
import com.rommansabbir.actmeldcx.base.permission.PermissionManagerImpl
import com.rommansabbir.storex.StoreXCore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object DIModule {
    @Provides
    fun providePermissionManager(): PermissionManager = PermissionManagerImpl()

    @Provides
    fun provideLocalHistoryRepository(): LocalHistoryRepository =
        LocalHistoryRepositoryImpl(StoreXCore.instance())
}