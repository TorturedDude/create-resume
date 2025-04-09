package ru.resume.create_resume.domain.dtos.ai

data class AiRequestDto(
    val model: String = "gpt-4o",
    val input: List<AiInputDto>
)
