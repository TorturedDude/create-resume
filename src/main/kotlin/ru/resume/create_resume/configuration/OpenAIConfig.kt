package ru.resume.create_resume.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableConfigurationProperties(OpenAIConfigProperties::class)
class OpenAIConfig(
    @Value("\${openai.api.key}")
    private val apiKey: String
) {

    @Bean
    fun openAiClient(properties: OpenAIConfigProperties): WebClient =
        WebClient.builder()
            .baseUrl(properties.baseUrl)
            .defaultHeader("Authorization", "Bearer $apiKey")
            .defaultHeader("Content-Type", "application/json")
            .build()

}