package ru.resume.create_resume.domain.dtos.user

/**
 * Модель для изменения пользователя
 */
data class UserEditDto(
    val email: String,
    val password: String,
)
