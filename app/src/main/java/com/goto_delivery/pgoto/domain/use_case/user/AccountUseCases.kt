package com.goto_delivery.pgoto.domain.use_case.user

data class AccountUseCases(
    val updateAddress: UpdateAddressUseCase,
    val getAccountInfo: GetAccountInfo
)