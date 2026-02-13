package com.example.fintech.spock.specs

import spock.lang.Unroll

import java.net.http.HttpResponse

import static com.example.fintech.spock.constants.HttpStatusCodes.BAD_REQUEST

class RegisterNegativeSpec extends BaseApiSpec {

  private static final String EMPTY = ''

  @Unroll
  def 'register should fail for invalid input: #caseName'() {
    when:
    HttpResponse<String> response = authClient.register(username, password)

    then:
    response.statusCode() == BAD_REQUEST
    response.body().contains('"error"')

    where:
    caseName         | username      | password
    'empty username' | EMPTY         | DEFAULT_PASSWORD
    'empty password' | newUsername() | EMPTY
    'both empty'     | EMPTY         | EMPTY
  }
}
