/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.core.di

import android.content.Context
import cu.kareldv.android.freesms.data.database.HistoryDatabase
import cu.kareldv.android.freesms.data.database.ProxiesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module that provides some data sources
 */
@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideHistoryDB(@ApplicationContext context: Context): HistoryDatabase {
        return HistoryDatabase(context)
    }

    @Provides
    @Singleton
    fun provideProxiesDB(@ApplicationContext context: Context): ProxiesDatabase {
        return ProxiesDatabase(context)
    }
}