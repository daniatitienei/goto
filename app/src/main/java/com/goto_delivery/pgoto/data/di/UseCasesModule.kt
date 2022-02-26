package com.goto_delivery.pgoto.data.di

import com.goto_delivery.pgoto.domain.repository.FirebaseAuthenticationRepository
import com.goto_delivery.pgoto.domain.use_case.authentication.AuthenticationUseCases
import com.goto_delivery.pgoto.domain.use_case.authentication.LoginUseCase
import com.goto_delivery.pgoto.domain.use_case.authentication.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    @Singleton
    fun provideAuthenticationUseCases(repository: FirebaseAuthenticationRepository) =
        AuthenticationUseCases(
            register = RegisterUseCase(repository = repository),
            login = LoginUseCase(repository = repository)
        )

}