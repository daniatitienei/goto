package com.goto_delivery.pgoto.domain.use_case.authentication

data class AuthenticationUseCases(
    val register: RegisterUseCase,
    val login: LoginUseCase,
    val continueWithGoogle: GoogleAuthenticationUseCase
)