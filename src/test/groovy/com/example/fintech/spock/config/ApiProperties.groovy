package com.example.fintech.spock.config

class ApiProperties {
  final String baseUrl
  final int timeoutMs

  ApiProperties(String baseUrl, int timeoutMs) {
    this.baseUrl = baseUrl
    this.timeoutMs = timeoutMs
  }
}
