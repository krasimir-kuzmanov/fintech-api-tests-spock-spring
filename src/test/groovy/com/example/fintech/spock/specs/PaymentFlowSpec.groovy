package com.example.fintech.spock.specs

import groovy.json.JsonSlurper

import java.math.BigDecimal
import java.net.http.HttpResponse

import static com.example.fintech.spock.constants.HttpStatusCodes.CREATED
import static com.example.fintech.spock.constants.HttpStatusCodes.OK

class PaymentFlowSpec extends BaseApiSpec {

  private static final BigDecimal INITIAL_FUND = new BigDecimal('100.00')
  private static final BigDecimal PAYMENT_AMOUNT = new BigDecimal('40.00')
  private static final String TRANSACTION_STATUS_SUCCESS = 'SUCCESS'

  def 'should make payment and expose transaction to both accounts'() {
    given:
    String aliceUsername = newUsername()
    String bobUsername = newUsername()

    and:
    AuthSession alice = registerAndLogin(aliceUsername)
    AuthSession bob = registerAndLogin(bobUsername)

    and:
    HttpResponse<String> fundResponse = accountClient.fund(alice.accountId(), INITIAL_FUND, alice.token())
    assert fundResponse.statusCode() == OK

    and:
    BigDecimal aliceBalanceBefore = fetchBalance(alice.accountId(), alice.token())
    BigDecimal bobBalanceBefore = fetchBalance(bob.accountId(), bob.token())

    when:
    HttpResponse<String> paymentResponse = transactionClient.makePayment(
        alice.accountId(),
        bob.accountId(),
        PAYMENT_AMOUNT,
        alice.token()
    )
    Map<String, Object> paymentJson = parseJsonMap(paymentResponse.body())
    String transactionId = paymentJson.transactionId as String

    then:
    paymentResponse.statusCode() == OK
    paymentJson.status == TRANSACTION_STATUS_SUCCESS
    transactionId

    when:
    BigDecimal aliceBalanceAfter = fetchBalance(alice.accountId(), alice.token())
    BigDecimal bobBalanceAfter = fetchBalance(bob.accountId(), bob.token())

    and:
    List<Map<String, Object>> aliceTransactions = fetchTransactions(alice.accountId(), alice.token())
    List<Map<String, Object>> bobTransactions = fetchTransactions(bob.accountId(), bob.token())

    then:
    assertBalanceDelta(aliceBalanceBefore, aliceBalanceAfter, PAYMENT_AMOUNT.negate())
    assertBalanceDelta(bobBalanceBefore, bobBalanceAfter, PAYMENT_AMOUNT)

    and:
    assertContainsTransaction(aliceTransactions, transactionId)
    assertContainsTransaction(bobTransactions, transactionId)
  }

  private AuthSession registerAndLogin(String username) {
    HttpResponse<String> registerResponse = authClient.register(username, DEFAULT_PASSWORD)
    assert registerResponse.statusCode() in [OK, CREATED]

    HttpResponse<String> loginResponse = authClient.login(username, DEFAULT_PASSWORD)
    assert loginResponse.statusCode() == OK

    Map<String, Object> loginJson = parseJsonMap(loginResponse.body())
    return new AuthSession(loginJson.userId as String, loginJson.token as String)
  }

  private BigDecimal fetchBalance(String accountId, String token) {
    HttpResponse<String> balanceResponse = accountClient.getBalance(accountId, token)
    assert balanceResponse.statusCode() == OK

    Map<String, Object> balanceJson = parseJsonMap(balanceResponse.body())
    return new BigDecimal(balanceJson.balance.toString())
  }

  private List<Map<String, Object>> fetchTransactions(String accountId, String token) {
    HttpResponse<String> transactionsResponse = transactionClient.getTransactions(accountId, token)
    assert transactionsResponse.statusCode() == OK

    return parseJsonList(transactionsResponse.body())
  }

  private static void assertBalanceDelta(BigDecimal before, BigDecimal after, BigDecimal delta) {
    after.compareTo(before.add(delta)) == 0
  }

  private static void assertContainsTransaction(List<Map<String, Object>> transactions, String transactionId) {
    transactions.any { Map<String, Object> transaction -> transaction.transactionId == transactionId }
  }

  private static Map<String, Object> parseJsonMap(String body) {
    return (Map<String, Object>) new JsonSlurper().parseText(body)
  }

  private static List<Map<String, Object>> parseJsonList(String body) {
    return (List<Map<String, Object>>) new JsonSlurper().parseText(body)
  }
}
