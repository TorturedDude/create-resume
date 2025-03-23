package ru.resume.create_resume.domain.dtos.user

/**
 * Модель получения пользователя
 */
data class UserDto(
    val id: Long,
    val username: String,
    val email: String,
)
