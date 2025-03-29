package ru.resume.create_resume.domain.dtos.auth

data class SignInRequest(
    val username: String,
    val password: String
)