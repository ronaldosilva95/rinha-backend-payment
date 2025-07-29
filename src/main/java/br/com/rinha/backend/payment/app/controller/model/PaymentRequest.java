package br.com.rinha.backend.payment.app.controller.model;

import java.math.BigDecimal;

public record PaymentRequest(String correlationId, BigDecimal amount) {

}
