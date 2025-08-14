package br.com.rinha.backend.payment.infra.dataprovider;

import br.com.rinha.backend.payment.infra.dataprovider.client.PaymentClient;
import br.com.rinha.backend.payment.infra.dataprovider.client.PaymentFallbackClient;
import br.com.rinha.backend.payment.infra.dataprovider.model.PaymentRequest;
import br.com.rinha.backend.payment.infra.repository.MetricsRepository;
import br.com.rinha.backend.payment.infra.repository.model.Metric;
import br.com.rinha.backend.payment.infra.repository.model.Summary;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
//import io.github.resilience4j.retry.annotation.Retry;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PaymentDataProvider {

  private static final String PROCESSOR_DEFAULT = "DEFAULT";
  private static final String PROCESSOR_FALLBACK = "FALLBACK";

  @Autowired
  private PaymentClient paymentClient;

  @Autowired
  private PaymentFallbackClient paymentFallbackClient;

  @Autowired
  private MetricsRepository metricsRepository;

  @Value("${payment.retry.count}")
  private int retryCount;

  public String createPayment(PaymentRequest paymentRequest, int count) throws InterruptedException {
    if (count < retryCount) {
      try {
        return createPayment(paymentRequest);
      } catch (Exception e) {
        count++;
        Thread.currentThread().sleep(count < retryCount ? 1500L : 2000L);
        return createPayment(paymentRequest, count);
      }
    }
    return createPaymentFallback(paymentRequest);
  }

//  @Retry(name = "retryPaymentService")
//  @CircuitBreaker(name = "cirtcuitBreakerPaymentService", fallbackMethod = "createPaymentFallback")
  public String createPayment(PaymentRequest paymentRequest) {
//    PaymentRequest paymentRequest = new PaymentRequest(correlationId, amount, date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    paymentClient.createPayment(paymentRequest);
    return PROCESSOR_DEFAULT;
  }

  public String createPaymentFallback(PaymentRequest paymentRequest) {
//    PaymentRequest paymentRequest = new PaymentRequest(correlationId, amount, date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    paymentFallbackClient.createPayment(paymentRequest);
    return PROCESSOR_FALLBACK;
  }

  @Async
  public void addMetrics(String paymentId, ZonedDateTime paymentDate, BigDecimal value, String processor) {
    metricsRepository.save(new Metric(paymentId, value, processor, paymentDate));
  }

  public List<Summary> getPaymentSummary(final ZonedDateTime startDate, final ZonedDateTime endDate) {
    var object = startDate == null || endDate == null ?
        metricsRepository.findAllMetricsGroupByProcessor() :
        metricsRepository.findMetricsGroupByProcessorAndRequestedAtBetween(startDate, endDate);

    if (object != null && !object.isEmpty()) {
      return object.stream().map(a ->
          new Summary((Long) a[0], (BigDecimal) a[1], (String) a[2])).toList();
    }
    return Collections.emptyList();
  }

}
