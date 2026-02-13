package com.example.fintech.spock.specs

import com.example.fintech.spock.client.AuthClient
import com.example.fintech.spock.client.TestResetClient
import com.example.fintech.spock.config.TestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import java.net.http.HttpResponse

@ContextConfiguration(classes = TestConfig)
@TestPropertySource(locations = 'classpath:application-test.properties')
class AuthSpec extends Specification {

  private static final String USERNAME_PREFIX = 'spock_'
  private static final String DEFAULT_PASSWORD = 'password'

  private static final int HTTP_OK = 200
  private static final int HTTP_CREATED = 201

  @Autowired
  private TestResetClient resetClient

  @Autowired
  private AuthClient authClient

  def setup() {
    resetClient.reset()
  }

  def 'should register and then login successfully'() {
    given:
    String username = newUsername()

    when:
    HttpResponse<String> registerResponse = authClient.register(username, DEFAULT_PASSWORD)

    then:
    assertRegistrationSuccess(registerResponse)

    when:
    HttpResponse<String> loginResponse = authClient.login(username, DEFAULT_PASSWORD)

    then:
    assertLoginSuccess(loginResponse)
  }

  private static String newUsername() {
    return USERNAME_PREFIX + UUID.randomUUID()
  }

  private static void assertRegistrationSuccess(HttpResponse<String> response) {
    assert response.statusCode() in [HTTP_OK, HTTP_CREATED]
    assert response.body().contains('"id"')
  }

  private static void assertLoginSuccess(HttpResponse<String> response) {
    assert response.statusCode() == HTTP_OK
    assert response.body().contains('"token"')
  }

}
