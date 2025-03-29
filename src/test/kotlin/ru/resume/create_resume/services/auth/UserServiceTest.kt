package ru.resume.create_resume.services.auth

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import ru.resume.create_resume.domain.entities.User
import ru.resume.create_resume.domain.repositories.UserRepository
import ru.resume.create_resume.enums.Role
import ru.resume.create_resume.exceptions.UserNotFoundException

class UserServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        userService = UserService(userRepository)
    }

    @Test
    fun `get should return user if exists`() = runTest {
        val userId = 1L
        val user = User(
            id = userId,
            username = "testUser",
            email = "test@example.com",
            password = "securePassword",
            role = Role.ROLE_USER
        )

        coEvery { userRepository.findById(userId) } returns user

        val result = userService.get(userId)

        StepVerifier.create(result)
            .expectNext(user)
            .verifyComplete()
    }

    @Test
    fun `get should throw UserNotFoundException if user not found`() = runTest {
        val userId = 2L

        coEvery { userRepository.findById(userId) } returns null

        val result = userService.get(userId)

        StepVerifier.create(result)
            .expectError(UserNotFoundException::class.java)
            .verify()
    }

    @Test
    fun `create should return user id if user is saved`() = runTest {
        val user = User(
            id = 1L,
            username = "newUser",
            email = "new@example.com",
            password = "securePassword",
            role = Role.ROLE_USER
        )

        coEvery { userRepository.existsByUsername(user.username) } returns false
        coEvery { userRepository.existsByEmail(user.email) } returns false
        coEvery { userRepository.save(user) } returns user

        val result = userService.create(user)

        StepVerifier.create(result)
            .expectNext(user.id)
            .verifyComplete()
    }

    @Test
    fun `create should throw exception if username already exists`() = runTest {
        val user = User(
            id = 1L,
            username = "existingUser",
            email = "test@example.com",
            password = "securePassword",
            role = Role.ROLE_USER
        )

        coEvery { userRepository.existsByUsername(user.username) } returns true

        val result = userService.create(user)

        StepVerifier.create(result)
            .expectErrorMatches { it.message == "User with this username: {existingUser}  already exists" }
            .verify()
    }

    @Test
    fun `create should throw exception if email already exists`() = runTest {
        val user = User(
            id = 1L,
            username = "testUser",
            email = "existing@example.com",
            password = "securePassword",
            role = Role.ROLE_USER
        )

        coEvery { userRepository.existsByUsername(user.username) } returns false
        coEvery { userRepository.existsByEmail(user.email) } returns true

        val result = userService.create(user)

        StepVerifier.create(result)
            .expectErrorMatches { it.message == "User with this email: {existing@example.com} already exists" }
            .verify()
    }
}
