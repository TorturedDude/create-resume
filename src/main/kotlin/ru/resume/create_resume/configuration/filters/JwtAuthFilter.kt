package ru.resume.create_resume.configuration.filters

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import ru.resume.create_resume.services.auth.JwtService
import ru.resume.create_resume.services.auth.ReactiveUserDetailsService

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: ReactiveUserDetailsService
) : WebFilter {

    companion object {
        const val BEARER_PREFIX = "Bearer "
        const val HEADER_NAME = HttpHeaders.AUTHORIZATION
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val authHeader = exchange.request.headers.getFirst(HEADER_NAME)

        if (authHeader.isNullOrEmpty() || !authHeader.startsWith(BEARER_PREFIX)) {
            return chain.filter(exchange)
        }

        val jwt = authHeader.substring(BEARER_PREFIX.length)
        val username = jwtService.extractUserName(jwt)

        if (username.isNotEmpty()) {
            return userDetailsService.findByUsername(username)
                .filter { userDetails -> jwtService.isTokenValid(jwt, userDetails) }
                .flatMap { userDetails ->
                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                    )

                    val context = ReactiveSecurityContextHolder.withAuthentication(authToken)
                    chain.filter(exchange).contextWrite(context)
                }
                .switchIfEmpty(chain.filter(exchange))
        }

        return chain.filter(exchange)
    }
}