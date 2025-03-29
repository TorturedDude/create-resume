package ru.resume.create_resume

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CreateResumeApplication

fun main(args: Array<String>) {
	dotenv().entries().forEach { entry ->
		System.setProperty(entry.key, entry.value)
	}
	runApplication<CreateResumeApplication>(*args)
}
