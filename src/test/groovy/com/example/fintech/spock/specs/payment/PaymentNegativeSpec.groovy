package com.example.fintech.spock.specs.payment

import com.example.fintech.spock.specs.base.BaseApiSpec

import spock.lang.Unroll

import java.net.http.HttpResponse

import static com.example.fintech.spock.constants.HttpStatusCodes.BAD_REQUEST
import static com.example.fintech.spock.constants.HttpStatusCodes.CONFLICT
import static com.example.fintech.spock.constants.HttpStatusCodes.OK

class PaymentNegativeSpec extends BaseApiSpec {

  private static final BigDecimal PAYMENT_AMOUNT = new BigDecimal('40.00')
  private static final BigDecimal SAME_ACCOUNT_PAYMENT_AMOUNT = new BigDecimal('10.00')

  private static final String INSUFFICIENT_FUNDS = 'INSUFFICIENT_FUNDS'
  private static final String INVALID_AMOUNT = 'INVALID_AMOUNT'
  private static final String SAME_ACCOUNT_TRANSFER = 'SAME_ACCOUNT_TRANSFER'

  def 'payment should fail with insufficient funds'() {
    given:
    AuthSession payer = registerAndLogin(newUsername())
    AuthSession receiver = registerAndLogin(newUsername())

    when:
    HttpResponse<String> response = transactionClient.makePayment(
        payer.accountId(),
        receiver.accountId(),
        PAYMENT_AMOUNT,
        payer.token()
    )

    then:
    response.statusCode() in [BAD_REQUEST, CONFLICT]
    Map<String, Object> responseJson = parseJsonMap(response.body())
    responseJson.error == INSUFFICIENT_FUNDS
  }

  def 'payment should fail when from and to accounts are the same'() {
    given:
    AuthSession payer = registerAndLogin(newUsername())

    and:
    HttpResponse<String> fundResponse = accountClient.fund(
        payer.accountId(),
        PAYMENT_AMOUNT,
        payer.token()
    )
    assert fundResponse.statusCode() == OK

    when:
    HttpResponse<String> response = transactionClient.makePayment(
        payer.accountId(),
        payer.accountId(),
        SAME_ACCOUNT_PAYMENT_AMOUNT,
        payer.token()
    )

    then:
    response.statusCode() == BAD_REQUEST
    Map<String, Object> responseJson = parseJsonMap(response.body())
    responseJson.error == SAME_ACCOUNT_TRANSFER
  }

  @Unroll
  def 'payment should fail with invalid amount: #caseName'() {
    given:
    AuthSession payer = registerAndLogin(newUsername())
    AuthSession receiver = registerAndLogin(newUsername())

    when:
    HttpResponse<String> response = transactionClient.makePaymentWithAmountValue(
        payer.accountId(),
        receiver.accountId(),
        amountValue,
        payer.token()
    )

    then:
    response.statusCode() == BAD_REQUEST
    Map<String, Object> responseJson = parseJsonMap(response.body())
    responseJson.error == INVALID_AMOUNT

    where:
    caseName      | amountValue
    'zero'        | '0'
    'negative'    | '-10'
    'non-numeric' | 'abc'
  }

}
