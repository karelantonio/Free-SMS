/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.core.di

import com.google.gson.Gson
import cu.kareldv.android.freesms.core.api.io.TextbeltApi
import cu.kareldv.android.freesms.core.utils.MProxySelector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Module that provides some network data sources
 */
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): TextbeltApi {
        return retrofit.create(TextbeltApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        gson: Gson,
    ): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl("https://textbelt.com/")
            client(client)
            addConverterFactory(GsonConverterFactory.create(gson))
            addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        }.build()
    }

    @Provides
    @Singleton
    fun provideOkHttp(mProxySelector: MProxySelector): OkHttpClient {
        return OkHttpClient.Builder().apply {
            proxySelector(mProxySelector)
        }.build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideMProxySelector(): MProxySelector {
        return MProxySelector()
    }
}