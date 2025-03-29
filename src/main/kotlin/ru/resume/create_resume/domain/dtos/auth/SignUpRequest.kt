package ru.resume.create_resume.domain.dtos.auth

data class SignUpRequest(
    val username: String,
    val email: String,
    val password: String
)
