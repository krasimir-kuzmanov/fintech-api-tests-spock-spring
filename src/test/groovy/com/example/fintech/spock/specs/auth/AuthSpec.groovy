package com.example.fintech.spock.specs.auth

import com.example.fintech.spock.specs.base.BaseApiSpec

import java.net.http.HttpResponse

import static com.example.fintech.spock.constants.HttpStatusCodes.CREATED
import static com.example.fintech.spock.constants.HttpStatusCodes.OK

class AuthSpec extends BaseApiSpec {

  def 'should register and then login successfully'() {
    given:
    String username = newUsername()

    when:
    HttpResponse<String> registerResponse = authClient.register(username, DEFAULT_PASSWORD)

    then:
    registerResponse.statusCode() in [OK, CREATED]
    Map<String, Object> registerJson = parseJsonMap(registerResponse.body())
    registerJson.id

    when:
    HttpResponse<String> loginResponse = authClient.login(username, DEFAULT_PASSWORD)

    then:
    loginResponse.statusCode() == OK
    Map<String, Object> loginJson = parseJsonMap(loginResponse.body())
    loginJson.token
  }
}
