package ru.resume.create_resume.domain.repositories

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import ru.resume.create_resume.domain.entities.User

/**
 * Репозиторий для пользователей
 */
interface UserRepository : CoroutineCrudRepository<User, Long> {
    suspend fun findByUsername(username: String): User?
    suspend fun existsByUsername(username: String): Boolean
    suspend fun existsByEmail(email: String): Boolean
}