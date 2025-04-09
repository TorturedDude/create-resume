package ru.resume.create_resume.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "minio")
data class MinIOConfigProperties(
    val minioUrl: String,
    val accessKey: String,
    val secretKey: String,
)
