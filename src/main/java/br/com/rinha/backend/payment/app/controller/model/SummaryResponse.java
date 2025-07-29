package br.com.rinha.backend.payment.app.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record SummaryResponse(
    @JsonProperty("default")
    Metric defaultMetrics,
    @JsonProperty("fallback")
    Metric fallbackMetrics) {

  public record Metric(
      Long totalRequests,
      BigDecimal totalAmount
  ) {

  }

}
