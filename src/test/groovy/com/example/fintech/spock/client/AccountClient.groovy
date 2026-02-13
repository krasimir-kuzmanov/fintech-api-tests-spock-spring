package com.example.fintech.spock.client

import groovy.json.JsonOutput

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class AccountClient {

  private final HttpClient httpClient
  private final String baseUrl
  private final int timeoutMs

  AccountClient(HttpClient httpClient, String baseUrl, int timeoutMs) {
    this.httpClient = httpClient
    this.baseUrl = baseUrl
    this.timeoutMs = timeoutMs
  }

  HttpResponse<String> fund(String accountId, BigDecimal amount, String token) {
    HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + '/account/' + accountId + '/fund'))
        .timeout(Duration.ofMillis(timeoutMs))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.AUTHORIZATION, 'Bearer ' + token)
        .POST(HttpRequest.BodyPublishers.ofString(JsonOutput.toJson([amount: amount])))
        .build()
    return httpClient.send(request, HttpResponse.BodyHandlers.ofString())
  }

  HttpResponse<String> getBalance(String accountId, String token) {
    HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + '/account/' + accountId))
        .timeout(Duration.ofMillis(timeoutMs))
        .header(HttpHeaders.AUTHORIZATION, 'Bearer ' + token)
        .GET()
        .build()
    return httpClient.send(request, HttpResponse.BodyHandlers.ofString())
  }
}
