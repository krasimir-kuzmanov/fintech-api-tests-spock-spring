package com.example.fintech.spock.config

import com.example.fintech.spock.client.AuthClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource('classpath:application-test.properties')
class TestConfig {

  @Bean
  ApiProperties apiProperties(
      @Value('${fintech.api.base-url}') String baseUrl,
      @Value('${fintech.api.timeout-ms}') int timeoutMs
  ) {
    return new ApiProperties(baseUrl, timeoutMs)
  }

  @Bean
  AuthClient authClient(ApiProperties apiProperties) {
    return new AuthClient(apiProperties.baseUrl)
  }
}
