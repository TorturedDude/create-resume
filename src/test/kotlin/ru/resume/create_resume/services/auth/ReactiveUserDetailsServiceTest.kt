package ru.resume.create_resume.services.auth

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import reactor.test.StepVerifier
import ru.resume.create_resume.domain.entities.User
import ru.resume.create_resume.domain.repositories.UserRepository
import ru.resume.create_resume.enums.Role
import ru.resume.create_resume.exceptions.UserNotFoundException

class ReactiveUserDetailsServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var reactiveUserDetailsService: ReactiveUserDetailsService

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        reactiveUserDetailsService = ReactiveUserDetailsService(userRepository)
    }

    @Test
    fun `get by username should return user if exists`() = runTest {
        val userName = "testUser"
        val user = User(
            id = 1L,
            username = userName,
            email = "test@example.com",
            password = "securePassword",
            role = Role.ROLE_USER
        )

        coEvery { userRepository.findByUsername(userName) } returns user

        val result = reactiveUserDetailsService.findByUsername(userName)

        StepVerifier.create(result)
            .expectNext(user)
            .verifyComplete()

    }

    @Test
    fun `get by username should throw UserNotFoundException if user not found`() = runTest {
        val userName = "testN"

        coEvery { userRepository.findByUsername(userName) } returns null

        val result = reactiveUserDetailsService.findByUsername(userName)

        StepVerifier.create(result)
            .expectError(UserNotFoundException::class.java)
            .verify()
    }
}