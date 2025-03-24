package ru.resume.create_resume.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import ru.resume.create_resume.configuration.filters.JwtAuthenticationFilter
import ru.resume.create_resume.enums.Role
import ru.resume.create_resume.services.ReactiveUserDetailsService

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    private val userDetailsService: ReactiveUserDetailsService,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {
    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf { it.disable() }
            .cors { cors ->
                cors.configurationSource(corsConfigurationSource())
            }
            .authorizeExchange { authorize ->
                authorize
                    .pathMatchers("/auth/**").permitAll()
                    .pathMatchers("/swagger-ui/**", "/swagger-resources/*", "/v3/api-docs/**").permitAll()
                    .pathMatchers("/admin/**").hasRole(Role.ROLE_ADMIN.name)
                    .anyExchange().authenticated()
            }
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .authenticationManager(authenticationManager())
            .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration().apply {
            allowedOriginPatterns = listOf("*")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
            allowedHeaders = listOf("*")
            allowCredentials = true
        }
        source.registerCorsConfiguration("/**", config)
        return source
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(): ReactiveAuthenticationManager {
        return UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService).apply {
            setPasswordEncoder(passwordEncoder())
        }
    }
}