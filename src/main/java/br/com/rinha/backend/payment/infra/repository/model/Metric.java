package br.com.rinha.backend.payment.infra.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_metrics")
@Transactional
public class Metric {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "metric_id", nullable = false)
  private Long metricId;

  @Column(name = "correlation_id", nullable = false)
  private String correllationId;

  @Column(name = "amount", nullable = false)
  private BigDecimal amount;

  @Column(name = "processor", nullable = false)
  private String processor;

  @Column(name = "requested_at", nullable = false)
  private LocalDateTime requestedAt;

  public Metric() {
  }

  public Metric(String correllationId, BigDecimal amount, String processor,
      LocalDateTime requestedAt) {
    this.correllationId = correllationId;
    this.amount = amount;
    this.processor = processor;
    this.requestedAt = requestedAt;
  }

  public Long getMetricId() {
    return metricId;
  }

  public void setMetricId(Long metricId) {
    this.metricId = metricId;
  }

  public String getCorrellationId() {
    return correllationId;
  }

  public void setCorrellationId(String correllationId) {
    this.correllationId = correllationId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getProcessor() {
    return processor;
  }

  public void setProcessor(String processor) {
    this.processor = processor;
  }

  public LocalDateTime getRequestedAt() {
    return requestedAt;
  }

  public void setRequestedAt(LocalDateTime requestedAt) {
    this.requestedAt = requestedAt;
  }
}
