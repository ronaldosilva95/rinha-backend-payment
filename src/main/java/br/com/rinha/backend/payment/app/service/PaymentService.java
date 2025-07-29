package br.com.rinha.backend.payment.app.service;

import br.com.rinha.backend.payment.app.controller.model.PaymentRequest;
import br.com.rinha.backend.payment.app.controller.model.SummaryResponse;
import br.com.rinha.backend.payment.app.controller.model.SummaryResponse.Metric;
import br.com.rinha.backend.payment.infra.repository.RedisRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

  @Autowired
  private RedisRepository redisRepository;

  public void createPayment(final PaymentRequest request) {
    redisRepository.addToQueue(request);
  }

  public SummaryResponse getPaymentSummary(final LocalDateTime startDate, final LocalDateTime endDate) {
    var paymentSummary = redisRepository.getPaymentSummary(startDate, endDate);

    if (!paymentSummary.isEmpty()) {
      Metric defaultMetric = null;
      Metric fallbackMetric = null;

      for (var metric : paymentSummary) {
        if (metric.getProcessor().equals("DEFAULT")) {
          defaultMetric = new SummaryResponse.Metric(metric.getCount(), metric.getTotalAmount());
        } else {
          fallbackMetric = new SummaryResponse.Metric(metric.getCount(), metric.getTotalAmount());
        }
      }

      return new SummaryResponse(defaultMetric, fallbackMetric);
    }
    return new SummaryResponse(null, null);
  }

}
