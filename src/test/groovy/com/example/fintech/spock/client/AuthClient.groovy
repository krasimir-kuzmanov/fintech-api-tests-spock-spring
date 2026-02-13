package com.example.fintech.spock.client

import groovy.json.JsonOutput

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class AuthClient {

  private static final String REGISTER_PATH = '/auth/register'
  private static final String LOGIN_PATH = '/auth/login'

  private final HttpClient httpClient
  private final String baseUrl
  private final int timeoutMs

  AuthClient(HttpClient httpClient, String baseUrl, int timeoutMs) {
    this.httpClient = httpClient
    this.baseUrl = baseUrl
    this.timeoutMs = timeoutMs
  }

  HttpResponse<String> register(String username, String password) {
    return postJson(REGISTER_PATH, [username: username, password: password])
  }

  HttpResponse<String> login(String username, String password) {
    return postJson(LOGIN_PATH, [username: username, password: password])
  }

  private HttpResponse<String> postJson(String path, Map<String, Object> payload) {
    HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + path))
        .timeout(Duration.ofMillis(timeoutMs))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .POST(HttpRequest.BodyPublishers.ofString(JsonOutput.toJson(payload)))
        .build()
    return httpClient.send(request, HttpResponse.BodyHandlers.ofString())
  }
}
