package ru.resume.create_resume.domain.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.resume.create_resume.enums.Role

/**
 * Сущность - пользователь
 */
@Table("users")
data class User(
    @Id
    val id: Long? = null,
    @JvmField
    val username: String,
    val email: String,
    @JvmField
    val password: String,
    val role: Role,
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableSetOf(SimpleGrantedAuthority(role.name))

    override fun getPassword(): String = password

    override fun getUsername(): String = username
}
