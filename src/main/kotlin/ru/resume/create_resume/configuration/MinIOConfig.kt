package ru.resume.create_resume.configuration

import io.minio.MinioClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(MinIOConfigProperties::class)
class MinIOConfig {

    @Bean
    fun minIOClient(properties: MinIOConfigProperties): MinioClient =
        MinioClient.builder()
            .endpoint(properties.minioUrl)
            .credentials(properties.accessKey, properties.secretKey)
            .build()
}