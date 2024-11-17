package com.aylar.chatty.domain.model

data class HTTPValidationError(
    val detail: List<ValidationError>
)

data class HTTPValidationErrorMessage(
    val detail: Message
)
