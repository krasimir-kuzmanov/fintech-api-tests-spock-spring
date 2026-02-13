package com.example.fintech.spock.specs

import java.net.http.HttpResponse

import static com.example.fintech.spock.constants.HttpStatusCodes.FORBIDDEN
import static com.example.fintech.spock.constants.HttpStatusCodes.UNAUTHORIZED

class SecuritySmokeSpec extends BaseApiSpec {

  def 'should return 401 for protected endpoints without token'() {
    given:
    AuthSession user = registerAndLogin(newUsername())

    when:
    HttpResponse<String> balanceResponse = accountClient.getBalanceWithoutAuth(user.accountId())
    HttpResponse<String> transactionsResponse = transactionClient.getTransactionsWithoutAuth(user.accountId())

    then:
    balanceResponse.statusCode() == UNAUTHORIZED
    transactionsResponse.statusCode() == UNAUTHORIZED
  }

  def 'should return 403 when accessing another users account with valid token'() {
    given:
    AuthSession alice = registerAndLogin(newUsername())
    AuthSession bob = registerAndLogin(newUsername())

    when:
    HttpResponse<String> response = accountClient.getBalance(bob.accountId(), alice.token())

    then:
    response.statusCode() == FORBIDDEN
  }
}
