package com.goto_delivery.pgoto.data.di

import com.goto_delivery.pgoto.domain.repository.FirebaseAuthenticationRepository
import com.goto_delivery.pgoto.domain.repository.AccountRepository
import com.goto_delivery.pgoto.domain.repository.RestaurantRepository
import com.goto_delivery.pgoto.domain.use_case.authentication.AuthenticationUseCases
import com.goto_delivery.pgoto.domain.use_case.authentication.GoogleAuthenticationUseCase
import com.goto_delivery.pgoto.domain.use_case.authentication.LoginUseCase
import com.goto_delivery.pgoto.domain.use_case.authentication.RegisterUseCase
import com.goto_delivery.pgoto.domain.use_case.restaurant.GetRestaurantById
import com.goto_delivery.pgoto.domain.use_case.restaurant.GetRestaurants
import com.goto_delivery.pgoto.domain.use_case.restaurant.RestaurantUseCases
import com.goto_delivery.pgoto.domain.use_case.user.AccountUseCases
import com.goto_delivery.pgoto.domain.use_case.user.UpdateAddressUseCase
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
            login = LoginUseCase(repository = repository),
            continueWithGoogle = GoogleAuthenticationUseCase(repository = repository)
        )

    @Provides
    @Singleton
    fun provideAccountUseCases(repository: AccountRepository) =
        AccountUseCases(
            updateAddress = UpdateAddressUseCase(repository = repository)
        )

    @Provides
    @Singleton
    fun provideRestaurantUseCases(repository: RestaurantRepository) =
        RestaurantUseCases(
            getRestaurants = GetRestaurants(repository = repository),
            getRestaurantById = GetRestaurantById(repository = repository)
        )
}