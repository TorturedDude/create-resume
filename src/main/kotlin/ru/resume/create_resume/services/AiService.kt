package ru.resume.create_resume.services

import kotlinx.coroutines.reactor.mono
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import ru.resume.create_resume.proxy.AiProxy

@Service
class AiService(
    private val aiProxy: AiProxy
) {

    suspend fun create(filename: String, fileData: String, text: String): Mono<String> = mono {
        aiProxy.createResume(filename,fileData,text)
    }

}