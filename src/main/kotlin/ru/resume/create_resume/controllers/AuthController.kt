package ru.resume.create_resume.controllers

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import ru.resume.create_resume.domain.dtos.auth.JwtAuthResponse
import ru.resume.create_resume.domain.dtos.auth.SignInRequest
import ru.resume.create_resume.domain.dtos.auth.SignUpRequest
import ru.resume.create_resume.services.AuthService

/**
 * Контроллер для аунтентификации
 */
@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/sign-up")
    suspend fun signUp(@RequestBody signUpRequest: SignUpRequest): Mono<JwtAuthResponse> =
        authService.signUp(signUpRequest)

    @PostMapping("/sign-in")
    suspend fun signIn(@RequestBody signInRequest: SignInRequest): Mono<JwtAuthResponse> =
        authService.signIn(signInRequest)
}