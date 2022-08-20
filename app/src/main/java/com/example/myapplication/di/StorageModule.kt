package com.example.myapplication.di

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import com.example.myapplication.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Provides
    @AppName
    fun provideAppName(@ApplicationContext context: Context): String {
        return context.getString(R.string.app_name)
    }

    @Provides
    @AccountName
    fun provideAccountName(@ApplicationContext context: Context): String {
        return context.getString(R.string.app_account_type)
    }

    @Provides
    @Singleton
    fun provideAccountManager(@ApplicationContext context: Context): AccountManager {
        return AccountManager.get(context)
    }

    @Provides
    @Singleton
    fun provideAccount(
        @AppName appName: String,
        @AccountName accountName: String,
        accountManager: AccountManager
    ): Account {
        return accountManager.getAccountsByType(accountName).getOrNull(0)
            ?: Account(appName, accountName).also {
                accountManager.addAccountExplicitly(it, null, null)
            }
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppName

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AccountName
