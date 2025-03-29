package ru.resume.create_resume.domain.mappers

import org.mapstruct.Mapper
import ru.resume.create_resume.domain.dtos.user.UserCreateDto
import ru.resume.create_resume.domain.dtos.user.UserEditDto
import ru.resume.create_resume.domain.dtos.user.UserDto
import ru.resume.create_resume.domain.entities.User

/**
 * Маппер для Пользователей
 */
@Mapper
interface UserMapper {

    /**
     * [UserCreateDto] -> [User]
     */
    fun toEntity(dto: UserCreateDto): User

    /**
     * [User] -> [UserDto]
     */
    fun toDto(user: User): UserDto

    /**
     * [UserEditDto] -> [User]
     */
    fun toEntity(id: Long, dto: UserEditDto): User

}