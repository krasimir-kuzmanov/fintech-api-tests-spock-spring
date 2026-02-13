package com.example.fintech.spock.client

import groovy.json.JsonOutput

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class TransactionClient {

  private final HttpClient httpClient
  private final String baseUrl
  private final int timeoutMs

  TransactionClient(HttpClient httpClient, String baseUrl, int timeoutMs) {
    this.httpClient = httpClient
    this.baseUrl = baseUrl
    this.timeoutMs = timeoutMs
  }

  HttpResponse<String> makePayment(String fromAccountId, String toAccountId, BigDecimal amount, String token) {
    return makePaymentWithAmountValue(fromAccountId, toAccountId, amount.toPlainString(), token)
  }

  HttpResponse<String> makePaymentWithAmountValue(
      String fromAccountId,
      String toAccountId,
      Object amountValue,
      String token
  ) {
    HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + '/transaction/payment'))
        .timeout(Duration.ofMillis(timeoutMs))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.AUTHORIZATION, 'Bearer ' + token)
        .POST(HttpRequest.BodyPublishers.ofString(JsonOutput.toJson([
          fromAccountId: fromAccountId,
          toAccountId: toAccountId,
          amount: amountValue,
        ])))
        .build()
    return httpClient.send(request, HttpResponse.BodyHandlers.ofString())
  }

  HttpResponse<String> getTransactions(String accountId, String token) {
    HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + '/transaction/' + accountId))
        .timeout(Duration.ofMillis(timeoutMs))
        .header(HttpHeaders.AUTHORIZATION, 'Bearer ' + token)
        .GET()
        .build()
    return httpClient.send(request, HttpResponse.BodyHandlers.ofString())
  }

  HttpResponse<String> getTransactionsWithoutAuth(String accountId) {
    HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + '/transaction/' + accountId))
        .timeout(Duration.ofMillis(timeoutMs))
        .GET()
        .build()
    return httpClient.send(request, HttpResponse.BodyHandlers.ofString())
  }
}
