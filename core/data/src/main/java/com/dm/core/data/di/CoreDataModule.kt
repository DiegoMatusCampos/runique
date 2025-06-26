package com.dm.core.data.di

import android.content.SharedPreferences
import android.se.omapi.Session
import com.dm.core.data.auth.EncryptedSessionStorage
import com.dm.core.data.networking.HttpClientFactory
import com.dm.core.domain.SessionStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single{
        HttpClientFactory(get()).build()
    }

    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()

}