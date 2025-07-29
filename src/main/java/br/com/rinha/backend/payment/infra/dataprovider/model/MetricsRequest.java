package br.com.rinha.backend.payment.infra.dataprovider.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MetricsRequest(
    String paymentId,
    LocalDateTime paymentDate,
    BigDecimal amount,
    String processor) {

}
