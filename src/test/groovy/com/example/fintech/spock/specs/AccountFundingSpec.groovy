package com.example.fintech.spock.specs

import java.net.http.HttpResponse

import static com.example.fintech.spock.constants.HttpStatusCodes.OK

class AccountFundingSpec extends BaseApiSpec {

  private static final BigDecimal FUND_AMOUNT = new BigDecimal('100.00')

  def 'should fund account and return updated balance'() {
    given:
    String username = newUsername()

    and:
    AuthSession authSession = registerAndLogin(username)
    String accountId = authSession.accountId()
    String token = authSession.token()

    when:
    HttpResponse<String> fundResponse = accountClient.fund(accountId, FUND_AMOUNT, token)
    HttpResponse<String> balanceResponse = accountClient.getBalance(accountId, token)

    then:
    fundResponse.statusCode() == OK
    balanceResponse.statusCode() == OK

    when:
    Map<String, Object> balanceJson = parseJsonMap(balanceResponse.body())
    BigDecimal balance = new BigDecimal(balanceJson.balance.toString())

    then:
    balance.compareTo(FUND_AMOUNT) == 0
  }
}
