package com.goto_delivery.pgoto.domain.use_case.authentication

import com.goto_delivery.pgoto.domain.repository.FirebaseAuthenticationRepository
import javax.inject.Inject

class GoogleAuthenticationUseCase @Inject constructor(
    private val repository: FirebaseAuthenticationRepository
) {
    operator fun invoke(idToken: String) = repository.continueWithGoogle(idToken)
}