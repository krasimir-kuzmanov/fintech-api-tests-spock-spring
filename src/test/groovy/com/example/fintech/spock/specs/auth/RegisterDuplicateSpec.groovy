package com.example.fintech.spock.specs.auth

import com.example.fintech.spock.specs.base.BaseApiSpec

import java.net.http.HttpResponse

import static com.example.fintech.spock.constants.HttpStatusCodes.*

class RegisterDuplicateSpec extends BaseApiSpec {

  private static final String USER_ALREADY_EXISTS = 'USER_ALREADY_EXISTS'

  def 'registering the same username twice should fail'() {
    given:
    String username = 'dup_' + UUID.randomUUID()

    when:
    HttpResponse<String> firstRegistration = authClient.register(username, DEFAULT_PASSWORD)
    HttpResponse<String> secondRegistration = authClient.register(username, DEFAULT_PASSWORD)

    then:
    firstRegistration.statusCode() in [OK, CREATED]
    secondRegistration.statusCode() == BAD_REQUEST
    Map<String, Object> secondRegistrationJson = parseJsonMap(secondRegistration.body())
    secondRegistrationJson.error == USER_ALREADY_EXISTS
  }
}
