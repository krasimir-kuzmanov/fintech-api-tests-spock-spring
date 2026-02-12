package com.example.fintech.spock.client

class AuthClient {
  private final String baseUrl

  AuthClient(String baseUrl) {
    this.baseUrl = baseUrl
  }

  String loginUrl() {
    return baseUrl + '/auth/login'
  }
}
