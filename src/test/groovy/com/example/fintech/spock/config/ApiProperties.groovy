package com.example.fintech.spock.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = 'fintech.api')
class ApiProperties {
  String baseUrl
  int timeoutMs
}
