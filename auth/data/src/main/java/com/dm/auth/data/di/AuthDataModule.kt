package com.dm.auth.data.di

import com.dm.auth.data.AuthRepositoryImpl
import com.dm.auth.data.EmailPatternValidator
import com.dm.auth.domain.AuthRepository
import com.dm.auth.domain.PatternValidator
import com.dm.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    single <PatternValidator>{ EmailPatternValidator() }
    singleOf(::UserDataValidator)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}