package ru.resume.create_resume.services.auth

import kotlinx.coroutines.reactor.mono
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.resume.create_resume.domain.entities.User
import ru.resume.create_resume.domain.mappers.UserMapper
import ru.resume.create_resume.domain.repositories.UserRepository
import ru.resume.create_resume.exceptions.UserNotFoundException

/**
 * Сервис для пользователей
 */
@Service
class UserService(
    private val userRepository: UserRepository
) {

    /**
     * Получение пользователя по [id]
     *
     * @param id - индентификатор пользователя
     * @return модель пользователя
     */
    fun get(id: Long): Mono<User> = mono {
        userRepository.findById(id)
            ?: throw UserNotFoundException("User not found with id: $id")
    }

    /**
     * Создание пользователя
     *
     * @param user - сущность пользователь
     * @return id пользователя
     */
    fun create(user: User): Mono<Long> = mono {

        if (userRepository.existsByUsername(user.username)) {
            throw RuntimeException("User with this username: {${user.username}}  already exists")
        }

        if (userRepository.existsByEmail(user.email)) {
            throw RuntimeException("User with this email: {${user.email}} already exists")
        }

        userRepository.save(user).id
    }

}