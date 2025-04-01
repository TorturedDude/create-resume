package ru.resume.create_resume.configuration

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "openai")
@ConditionalOnProperty(prefix = "openai.enabled", havingValue = "true")
data class OpenAIConfigProperties(
    val baseUrl: String
)