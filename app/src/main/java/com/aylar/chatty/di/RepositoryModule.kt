package com.aylar.chatty.di

import com.aylar.chatty.data.repository.AuthRepositoryImp
import com.aylar.chatty.data.repository.UserRepositoryImpl
import com.aylar.chatty.domain.repository.AuthRepository
import com.aylar.chatty.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImp
    ): AuthRepository
}