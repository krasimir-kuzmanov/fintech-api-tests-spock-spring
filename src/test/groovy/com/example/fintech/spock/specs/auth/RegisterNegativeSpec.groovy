package com.example.fintech.spock.specs.auth

import com.example.fintech.spock.specs.base.BaseApiSpec

import spock.lang.Unroll

import java.net.http.HttpResponse

import static com.example.fintech.spock.constants.HttpStatusCodes.BAD_REQUEST

class RegisterNegativeSpec extends BaseApiSpec {

  @Unroll
  def 'register should fail for invalid input: #caseName'() {
    when:
    HttpResponse<String> response = authClient.register(username, password)

    then:
    response.statusCode() == BAD_REQUEST
    Map<String, Object> responseJson = parseJsonMap(response.body())
    responseJson.error

    where:
    caseName         | username      | password
    'empty username' | EMPTY         | DEFAULT_PASSWORD
    'empty password' | newUsername() | EMPTY
    'both empty'     | EMPTY         | EMPTY
  }
}
