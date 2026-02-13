package com.example.fintech.spock.config

import com.example.fintech.spock.client.AuthClient
import com.example.fintech.spock.client.TestResetClient
import java.net.http.HttpClient
import java.time.Duration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(ApiProperties)
class TestConfig {

  @Bean
  HttpClient httpClient(ApiProperties apiProperties) {
    return HttpClient.newBuilder()
        .connectTimeout(Duration.ofMillis(apiProperties.timeoutMs))
        .build()
  }

  @Bean
  AuthClient authClient(HttpClient httpClient, ApiProperties apiProperties) {
    return new AuthClient(httpClient, apiProperties.baseUrl, apiProperties.timeoutMs)
  }

  @Bean
  TestResetClient testResetClient(HttpClient httpClient, ApiProperties apiProperties) {
    return new TestResetClient(httpClient, apiProperties.baseUrl, apiProperties.timeoutMs)
  }
}
