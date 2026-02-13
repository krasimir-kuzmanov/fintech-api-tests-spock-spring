package com.example.fintech.spock.specs

import groovy.json.JsonSlurper

import java.net.http.HttpResponse

import static com.example.fintech.spock.constants.HttpStatusCodes.CREATED
import static com.example.fintech.spock.constants.HttpStatusCodes.OK

class AccountFundingSpec extends BaseApiSpec {

  private static final BigDecimal FUND_AMOUNT = new BigDecimal('100.00')

  def 'should fund account and return updated balance'() {
    given:
    String username = newUsername()

    and:
    HttpResponse<String> registerResponse = authClient.register(username, DEFAULT_PASSWORD)
    assert registerResponse.statusCode() in [OK, CREATED]

    and:
    AuthSession authSession = loginAndExtractAuth(username)
    String accountId = authSession.accountId()
    String token = authSession.token()

    when:
    HttpResponse<String> fundResponse = accountClient.fund(accountId, FUND_AMOUNT, token)
    HttpResponse<String> balanceResponse = accountClient.getBalance(accountId, token)

    then:
    fundResponse.statusCode() == OK
    balanceResponse.statusCode() == OK

    when:
    Map<String, Object> balanceJson = parseJson(balanceResponse.body())
    BigDecimal balance = new BigDecimal(balanceJson.balance.toString())

    then:
    balance.compareTo(FUND_AMOUNT) == 0
  }


  private AuthSession loginAndExtractAuth(String username) {
    HttpResponse<String> loginResponse = authClient.login(username, DEFAULT_PASSWORD)
    assert loginResponse.statusCode() == OK

    Map<String, Object> loginJson = parseJson(loginResponse.body())
    return new AuthSession(loginJson.userId as String, loginJson.token as String)
  }

  private static Map<String, Object> parseJson(String body) {
    return (Map<String, Object>) new JsonSlurper().parseText(body)
  }

  private record AuthSession(String accountId, String token) {
  }
}
