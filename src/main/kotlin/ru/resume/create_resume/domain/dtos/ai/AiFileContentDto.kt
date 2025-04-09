package ru.resume.create_resume.domain.dtos.ai

data class AiFileContentDto(
    val type: String,
    val filename: String,
    val file_data: String
)
