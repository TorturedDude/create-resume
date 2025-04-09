package ru.resume.create_resume.proxy

import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import ru.resume.create_resume.domain.dtos.ai.AiFileContentDto
import ru.resume.create_resume.domain.dtos.ai.AiInputDto
import ru.resume.create_resume.domain.dtos.ai.AiRequestDto
import ru.resume.create_resume.domain.dtos.ai.AiTextContentDto

@Component
class AiProxy(
    @Qualifier("openAiClient")
    private val client: ObjectProvider<WebClient>
) {

    suspend fun createResume(filename: String, fileData: String, text: String): String? =
        kotlin.runCatching {
            client.ifAvailable
                ?.post()
                ?.uri { builder ->
                    builder.path(CREATE_RESUME_API)
                        .build()
                }
                ?.bodyValue(generateRequestBody(filename, fileData, text))
                ?.retrieve()
                ?.awaitBodyOrNull<String>()
        }.onFailure {

        }.getOrNull()

    private fun generateRequestBody(filename: String, fileData: String, text: String): AiRequestDto =
        AiRequestDto(
            AI_MODEL, listOf(
                AiInputDto(
                    AI_ROLE, listOf(
                        AiFileContentDto(AI_INPUT_FILE_TYPE, filename, fileData),
                        AiTextContentDto(AI_INPUT_TEXT_TYPE, text)
                    )
                )
            )
        )

    companion object {
        private const val CREATE_RESUME_API = "/responses"
        private const val AI_ROLE = "user"
        private const val AI_MODEL = "gpt-4o"
        private const val AI_INPUT_TEXT_TYPE = "input_text"
        private const val AI_INPUT_FILE_TYPE = "input_file"
    }
}