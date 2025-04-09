package ru.resume.create_resume.services

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier

import ru.resume.create_resume.proxy.AiProxy

class AiServiceTest {

    private lateinit var aiProxy: AiProxy
    private lateinit var aiService: AiService

    @BeforeEach
    fun setUp() {
        aiProxy = mockk()
        aiService = AiService(aiProxy)
    }

    @Test
    fun `create returns string answer`() = runTest {
        val filename = "test.pdf"
        val fileData = "rhghrhhrhtr"
        val text = "get all from file"

        coEvery { aiProxy.createResume(filename, fileData, text) } returns fileData

        val result = aiService.create(filename, fileData, text)

        StepVerifier.create(result)
            .expectNext(fileData)
            .verifyComplete()
    }

    @Test
    fun `create returns null`() = runTest {
        val filename = "test.pdf"
        val fileData = "rhghrhhrhtr"
        val text = "get all from file"

        coEvery { aiProxy.createResume(filename, fileData, text) } returns null

        val result = aiService.create(filename, fileData, text)

        StepVerifier.create(result)
            .expectComplete()
    }
}