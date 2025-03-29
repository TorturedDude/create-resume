package ru.resume.create_resume.domain.dtos.user

/**
 * Модель создания пользователя
 */
data class UserCreateDto(
    val username: String,
    val email: String,
    val password: String,
)
