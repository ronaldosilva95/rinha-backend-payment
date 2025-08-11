package br.com.rinha.backend.payment.app.service;

import br.com.rinha.backend.payment.app.controller.model.SummaryResponse;
import br.com.rinha.backend.payment.app.controller.model.SummaryResponse.Metric;
import br.com.rinha.backend.payment.infra.dataprovider.PaymentDataProvider;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

  @Autowired
  private PaymentDataProvider paymentDataProvider;

  public SummaryResponse getPaymentSummary(final ZonedDateTime startDate, final ZonedDateTime endDate) {
    var paymentSummary = paymentDataProvider.getPaymentSummary(startDate, endDate);

    Metric defaultMetric = new Metric(0L, BigDecimal.ZERO);
    Metric fallbackMetric = new Metric(0L, BigDecimal.ZERO);
    if (!paymentSummary.isEmpty()) {
      for (var metric : paymentSummary) {
        if (metric.getProcessor().equals("DEFAULT")) {
          defaultMetric = new SummaryResponse.Metric(metric.getCount(), metric.getTotalAmount());
        } else {
          fallbackMetric = new SummaryResponse.Metric(metric.getCount(), metric.getTotalAmount());
        }
      }

    }
    return new SummaryResponse(defaultMetric, fallbackMetric);
  }

}
