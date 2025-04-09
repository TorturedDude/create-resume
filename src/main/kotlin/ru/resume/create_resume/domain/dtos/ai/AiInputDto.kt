package ru.resume.create_resume.domain.dtos.ai

data class AiInputDto(
    val role: String,
    val content: List<Any>
)
