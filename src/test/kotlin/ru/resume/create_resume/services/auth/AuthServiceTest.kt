import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import ru.resume.create_resume.domain.dtos.auth.JwtAuthResponse
import ru.resume.create_resume.domain.dtos.auth.SignInRequest
import ru.resume.create_resume.domain.dtos.auth.SignUpRequest
import ru.resume.create_resume.domain.entities.User
import ru.resume.create_resume.enums.Role
import ru.resume.create_resume.exceptions.UserNotFoundException
import ru.resume.create_resume.services.auth.AuthService
import ru.resume.create_resume.services.auth.JwtService
import ru.resume.create_resume.services.auth.ReactiveUserDetailsService
import ru.resume.create_resume.services.auth.UserService

class AuthServiceTest {

    private lateinit var userService: UserService
    private lateinit var jwtService: JwtService
    private lateinit var userDetailsService: ReactiveUserDetailsService
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var authenticationManager: ReactiveAuthenticationManager
    private lateinit var service: AuthService

    @BeforeEach
    fun setUp() {
        userService = mockk()
        jwtService = mockk()
        userDetailsService = mockk()
        passwordEncoder = mockk()
        authenticationManager = mockk()
        service = AuthService(userService, jwtService, userDetailsService, passwordEncoder, authenticationManager)
    }

    @Test
    fun `signUp should create user and return JWT`() = runTest {
        val request = SignUpRequest(username = "testUser", email = "test@example.com", password = "password123")
        val encodedPassword = "encodedPassword"
        val user = User(username = request.username, email = request.email, password = encodedPassword, role = Role.ROLE_USER)
        val savedUser = user.copy(id = 1L)
        val jwtToken = "mockedJwtToken"

        every { passwordEncoder.encode(request.password) } returns encodedPassword
        coEvery { userService.create(user) } returns Mono.just(1L)
        coEvery { userService.get(1L) } returns Mono.just(savedUser)
        coEvery { jwtService.generateToken(savedUser) } returns Mono.just(jwtToken)

        val result = service.signUp(request)

        StepVerifier.create(result)
            .expectNext(JwtAuthResponse(jwtToken))
            .verifyComplete()
    }

    @Test
    fun `signIn should authenticate user and return JWT`() = runTest {
        val request = SignInRequest(username = "testUser", password = "password123")
        val jwtToken = "mockedJwtToken"
        val user = User(id = 1L, username = request.username, email = "test@example.com", password = "encodedPassword", role = Role.ROLE_USER)

        every {
            authenticationManager.authenticate(any())
        } returns Mono.just(UsernamePasswordAuthenticationToken(request.username, request.password))

        coEvery { userDetailsService.findByUsername(request.username) } returns Mono.just(user)
        coEvery { jwtService.generateToken(user) } returns Mono.just(jwtToken)

        val result = service.signIn(request)

        StepVerifier.create(result)
            .expectNext(JwtAuthResponse(jwtToken))
            .verifyComplete()
    }

    @Test
    fun `signIn should fail if user not found`() = runTest {
        val request = SignInRequest(username = "unknownUser", password = "password123")

        every { authenticationManager.authenticate(any()) } returns Mono.empty()
        coEvery { userDetailsService.findByUsername(request.username) } returns Mono.error(UserNotFoundException("User not found"))

        val result = service.signIn(request)

        StepVerifier.create(result)
            .expectError(UserNotFoundException::class.java)
            .verify()
    }
}