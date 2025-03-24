package ru.resume.create_resume.services

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.resume.create_resume.domain.dtos.auth.JwtAuthResponse
import ru.resume.create_resume.domain.dtos.auth.SignInRequest
import ru.resume.create_resume.domain.dtos.auth.SignUpRequest
import ru.resume.create_resume.domain.entities.User
import ru.resume.create_resume.enums.Role

/**
 * Сервис для аутентификации
 */
@Service
class AuthService(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val userDetailsService: ReactiveUserDetailsService,
    @Autowired private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: ReactiveAuthenticationManager
) {

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    suspend fun signUp(request: SignUpRequest): Mono<JwtAuthResponse> = mono {
        val user = User(
            username = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            role = Role.ROLE_USER
        )

        userService.create(user).awaitSingle() // Ожидаем завершения создания пользователя

        val jwt = jwtService.generateToken(user).awaitSingle() // Ожидаем генерации токена
        JwtAuthResponse(jwt)
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    suspend fun signIn(request: SignInRequest): Mono<JwtAuthResponse> = mono {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.username,
                request.password
            )
        )

        val user = userDetailsService.findByUsername(request.username).awaitSingle()

        val jwt = jwtService.generateToken(user).awaitSingle()
        JwtAuthResponse(jwt)
    }
}