package com.aylar.chatty.domain.model

data class ValidationError(
    val loc: List<Location>,
    val msg: String,
    val type: String
)