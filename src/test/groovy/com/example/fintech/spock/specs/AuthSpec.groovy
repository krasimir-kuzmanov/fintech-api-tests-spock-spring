package com.example.fintech.spock.specs

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
    assertRegistrationSuccess(registerResponse)

    when:
    HttpResponse<String> loginResponse = authClient.login(username, DEFAULT_PASSWORD)

    then:
    assertLoginSuccess(loginResponse)
  }

  private static void assertRegistrationSuccess(HttpResponse<String> response) {
    assert response.statusCode() in [OK, CREATED]
    assert response.body().contains('"id"')
  }

  private static void assertLoginSuccess(HttpResponse<String> response) {
    assert response.statusCode() == OK
    assert response.body().contains('"token"')
  }
}
