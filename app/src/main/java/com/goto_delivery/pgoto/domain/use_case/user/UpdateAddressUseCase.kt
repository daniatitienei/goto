package com.goto_delivery.pgoto.domain.use_case.user

import com.goto_delivery.pgoto.domain.repository.AccountRepository
import javax.inject.Inject

class UpdateAddressUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    operator fun invoke(newAddress: String, city: String) =
        repository.updateAddress(newAddress, city)
}