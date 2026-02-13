package com.example.fintech.spock.specs.base

import com.example.fintech.spock.client.AuthClient
import com.example.fintech.spock.client.AccountClient
import com.example.fintech.spock.client.TestResetClient
import com.example.fintech.spock.client.TransactionClient
import com.example.fintech.spock.config.TestConfig
import groovy.json.JsonSlurper
import java.net.http.HttpResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import static com.example.fintech.spock.constants.HttpStatusCodes.CREATED
import static com.example.fintech.spock.constants.HttpStatusCodes.OK

@ContextConfiguration(classes = TestConfig)
@TestPropertySource(locations = 'classpath:application-test.properties')
abstract class BaseApiSpec extends Specification {

  protected static final String USERNAME_PREFIX = 'spock_'
  protected static final String DEFAULT_PASSWORD = 'password'
  protected static final String EMPTY = ''

  @Autowired
  protected TestResetClient resetClient

  @Autowired
  protected AuthClient authClient

  @Autowired
  protected AccountClient accountClient

  @Autowired
  protected TransactionClient transactionClient

  def setup() {
    resetClient.reset()
  }

  protected static String newUsername() {
    return USERNAME_PREFIX + UUID.randomUUID()
  }

  protected AuthSession registerAndLogin(String username) {
    HttpResponse<String> registerResponse = authClient.register(username, DEFAULT_PASSWORD)
    assert registerResponse.statusCode() in [OK, CREATED]

    HttpResponse<String> loginResponse = authClient.login(username, DEFAULT_PASSWORD)
    assert loginResponse.statusCode() == OK

    Map<String, Object> loginJson = parseJsonMap(loginResponse.body())
    return new AuthSession(loginJson.userId as String, loginJson.token as String)
  }

  protected static Map<String, Object> parseJsonMap(String body) {
    return (Map<String, Object>) new JsonSlurper().parseText(body)
  }

  protected static List<Map<String, Object>> parseJsonList(String body) {
    return (List<Map<String, Object>>) new JsonSlurper().parseText(body)
  }

  protected static record AuthSession(String accountId, String token) {
  }
}
