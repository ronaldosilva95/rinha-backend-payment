package br.com.rinha.backend.payment.infra.repository;

import br.com.rinha.backend.payment.app.controller.model.PaymentRequest;
import br.com.rinha.backend.payment.infra.repository.model.Metric;
import br.com.rinha.backend.payment.infra.repository.model.Summary;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisRepository {

  @Autowired
  private StringRedisTemplate redisTemplate;

  @Autowired
  private MetricsRepository metricsRepository;

  @Value("${queue.name:payments-queue}")
  private String queueName;

  public void addToQueue(PaymentRequest request) {
    redisTemplate.opsForStream().add(queueName, Map.of("correlationId", request.correlationId(), "amount", String.valueOf(request.amount())));
  }

  public void addMetrics(String paymentId, LocalDateTime paymentDate, BigDecimal value, String processor) {
    metricsRepository.save(new Metric(paymentId, value, processor, paymentDate));
  }

  public List<Summary> getPaymentSummary(final LocalDateTime startDate, final LocalDateTime endDate) {
    var object = metricsRepository.findMetricsGroupByProcessorAndRequestedAtBetween(startDate, endDate);
    if (object != null && !object.isEmpty()) {
      return object.stream().map(a ->
        new Summary((Long) a[0], (BigDecimal) a[1], (String) a[2])).toList();
    }
    return Collections.emptyList();
  }

}
