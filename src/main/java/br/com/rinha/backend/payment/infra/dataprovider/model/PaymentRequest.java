package br.com.rinha.backend.payment.infra.dataprovider.model;

import java.math.BigDecimal;

public class PaymentRequest {

    private String correlationId;
    private BigDecimal amount;
    private String requestedAt;

  public PaymentRequest(String correlationId, BigDecimal amount, String requestedAt) {
    this.correlationId = correlationId;
    this.amount = amount;
    this.requestedAt = requestedAt;
  }

  public PaymentRequest() {
  }

  public PaymentRequest(String correlationId, BigDecimal amount) {
    this.correlationId = correlationId;
    this.amount = amount;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getRequestedAt() {
    return requestedAt;
  }

  public void setRequestedAt(String requestedAt) {
    this.requestedAt = requestedAt;
  }
}
