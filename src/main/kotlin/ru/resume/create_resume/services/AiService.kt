package ru.resume.create_resume.services

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class AiService(
    @Qualifier("openAiClient")
    private val client: WebClient
) {

}