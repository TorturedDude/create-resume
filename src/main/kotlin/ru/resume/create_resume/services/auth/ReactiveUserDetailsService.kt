package ru.resume.create_resume.services.auth

import kotlinx.coroutines.reactor.mono
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.resume.create_resume.domain.repositories.UserRepository
import ru.resume.create_resume.exceptions.UserNotFoundException

@Service
class ReactiveUserDetailsService(
    private val userRepository: UserRepository
) : ReactiveUserDetailsService{
    override fun findByUsername(username: String): Mono<UserDetails> = mono {
        userRepository.findByUsername(username)
            ?: throw UserNotFoundException("User not found with username: $username")
    }
}