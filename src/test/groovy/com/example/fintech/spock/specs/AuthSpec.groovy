package com.example.fintech.spock.specs

import com.example.fintech.spock.client.AuthClient
import com.example.fintech.spock.config.ApiProperties
import com.example.fintech.spock.config.TestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ActiveProfiles('test')
@ContextConfiguration(classes = TestConfig)
class AuthSpec extends Specification {

  @Autowired
  ApiProperties apiProperties

  @Autowired
  AuthClient authClient

  def 'should wire spring test context and inject api client bean'() {
    expect:
    apiProperties.baseUrl == 'http://localhost:8080'
    apiProperties.timeoutMs == 5000
    authClient.loginUrl() == 'http://localhost:8080/auth/login'
  }
}
