package com.example.fintech.spock.specs

import com.example.fintech.spock.client.AuthClient
import com.example.fintech.spock.client.AccountClient
import com.example.fintech.spock.client.TestResetClient
import com.example.fintech.spock.config.TestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

@ContextConfiguration(classes = TestConfig)
@TestPropertySource(locations = 'classpath:application-test.properties')
abstract class BaseApiSpec extends Specification {

  protected static final String USERNAME_PREFIX = 'spock_'
  protected static final String DEFAULT_PASSWORD = 'password'

  @Autowired
  protected TestResetClient resetClient

  @Autowired
  protected AuthClient authClient

  @Autowired
  protected AccountClient accountClient

  def setup() {
    resetClient.reset()
  }

  protected static String newUsername() {
    return USERNAME_PREFIX + UUID.randomUUID()
  }
}
