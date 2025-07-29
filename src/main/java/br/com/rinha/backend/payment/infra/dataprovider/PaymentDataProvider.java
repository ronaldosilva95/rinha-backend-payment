package br.com.rinha.backend.payment.infra.dataprovider;

import br.com.rinha.backend.payment.infra.dataprovider.client.PaymentClient;
import br.com.rinha.backend.payment.infra.dataprovider.client.PaymentFallbackClient;
import br.com.rinha.backend.payment.infra.dataprovider.model.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentDataProvider {

  private static final String PROCESSOR_DEFAULT = "DEFAULT";
  private static final String PROCESSOR_FALLBACK = "FALLBACK";

  @Autowired
  private PaymentClient paymentClient;

  @Autowired
  private PaymentFallbackClient paymentFallbackClient;

  @CircuitBreaker(name = "paymentService", fallbackMethod = "createPaymentFallback")
  public String createPayment(LocalDateTime date, String correlationId, BigDecimal amount) {
    PaymentRequest paymentRequest = new PaymentRequest(correlationId, amount, date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    paymentClient.createPayment(paymentRequest);
    return PROCESSOR_DEFAULT;
  }

  public String createPaymentFallback(LocalDateTime date,String correlationId, BigDecimal amount, Throwable e) {
    PaymentRequest paymentRequest = new PaymentRequest(correlationId, amount, date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    paymentFallbackClient.createPayment(paymentRequest);
    return PROCESSOR_FALLBACK;
  }

}
