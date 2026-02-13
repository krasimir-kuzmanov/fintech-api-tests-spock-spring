package com.example.fintech.spock.client


import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

class TestResetClient {

  private static final String RESET_PATH = '/test/reset'

  private final HttpClient httpClient
  private final String baseUrl
  private final int timeoutMs

  TestResetClient(HttpClient httpClient, String baseUrl, int timeoutMs) {
    this.httpClient = httpClient
    this.baseUrl = baseUrl
    this.timeoutMs = timeoutMs
  }

  HttpResponse<String> reset() {
    return post(RESET_PATH)
  }

  private HttpResponse<String> post(String path) {
    HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + path))
        .timeout(Duration.ofMillis(timeoutMs))
        .POST(HttpRequest.BodyPublishers.noBody())
        .build()
    return httpClient.send(request, HttpResponse.BodyHandlers.ofString())
  }
}
