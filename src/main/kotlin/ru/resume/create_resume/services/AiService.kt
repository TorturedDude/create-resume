package ru.resume.create_resume.services

import kotlinx.coroutines.reactor.mono
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import ru.resume.create_resume.proxy.AiProxy
import ru.resume.create_resume.utils.logger

@Service
class AiService(
    private val aiProxy: AiProxy
) {

    private val log = logger()

    suspend fun create(filename: String, fileData: String, text: String): Mono<String> = mono {
        log.info("Creating new resume by example: filename - $filename text-request - $text")
        aiProxy.createResume(filename,fileData,text)
    }

}