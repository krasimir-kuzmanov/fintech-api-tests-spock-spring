package com.example.fintech.spock.specs.auth

import com.example.fintech.spock.specs.base.BaseApiSpec

import spock.lang.Unroll

import java.net.http.HttpResponse

import static com.example.fintech.spock.constants.HttpStatusCodes.UNAUTHORIZED

class LoginNegativeSpec extends BaseApiSpec {

  private static final String INVALID_CREDENTIALS = 'INVALID_CREDENTIALS'
  private static final String WRONG_PASSWORD = 'wrong-password'
  private static final String UNKNOWN_USERNAME = 'no_such_user'

  @Unroll
  def 'login should fail for invalid credentials: #caseName'() {
    given:
    String username = newUsername()
    authClient.register(username, DEFAULT_PASSWORD)
    String loginUsername = resolveLoginUsername(loginUsernameKind, username)

    when:
    HttpResponse<String> response = authClient.login(loginUsername, loginPassword)

    then:
    response.statusCode() == UNAUTHORIZED
    Map<String, Object> responseJson = parseJsonMap(response.body())
    responseJson.error == INVALID_CREDENTIALS

    where:
    caseName         | loginUsernameKind       | loginPassword
    'wrong password' | UsernameKind.REGISTERED | WRONG_PASSWORD
    'unknown user'   | UsernameKind.UNKNOWN    | DEFAULT_PASSWORD
    'empty username' | UsernameKind.EMPTY      | DEFAULT_PASSWORD
    'empty password' | UsernameKind.REGISTERED | EMPTY
  }

  private static String resolveLoginUsername(UsernameKind kind, String registeredUsername) {
    return switch (kind) {
      case UsernameKind.REGISTERED -> registeredUsername
      case UsernameKind.UNKNOWN -> UNKNOWN_USERNAME
      case UsernameKind.EMPTY -> EMPTY
    }
  }

  private enum UsernameKind {
    REGISTERED,
    UNKNOWN,
    EMPTY
  }
}
