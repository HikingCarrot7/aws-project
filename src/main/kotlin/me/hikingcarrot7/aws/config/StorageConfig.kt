package me.hikingcarrot7.aws.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicSessionCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StorageConfig {
  @Value("\${cloud.aws.credentials.access-key}")
  lateinit var accessKey: String

  @Value("\${cloud.aws.credentials.secret-key}")
  lateinit var secretKey: String

  @Value("\${cloud.aws.credentials.session-token}")
  lateinit var sessionToken: String

  @Value("\${cloud.aws.region.static}")
  lateinit var region: String

  @Bean
  fun s3Client(): AmazonS3 {
    val credentials = BasicSessionCredentials(
      accessKey,
      secretKey,
      sessionToken
    )
    return AmazonS3ClientBuilder.standard()
      .withCredentials(AWSStaticCredentialsProvider(credentials))
      .withRegion(region)
      .build()
  }
}