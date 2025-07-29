package br.com.rinha.backend.payment.infra.dataprovider.model;

import java.math.BigDecimal;

public record PaymentRequest(
    String correlationId,
    BigDecimal amount,
    String requestedAt
) {

}
