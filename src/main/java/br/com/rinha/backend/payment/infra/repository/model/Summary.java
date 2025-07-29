package br.com.rinha.backend.payment.infra.repository.model;

import java.math.BigDecimal;

public class Summary {

  private Long count;
  private BigDecimal totalAmount;
  private String processor;

  public Summary() {
  }

  public Summary(Long count, BigDecimal totalAmount, String processor) {
    this.count = count;
    this.totalAmount = totalAmount;
    this.processor = processor;
  }

  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public String getProcessor() {
    return processor;
  }

  public void setProcessor(String processor) {
    this.processor = processor;
  }
}
