package br.com.rinha.backend.payment.app.configuration;

import br.com.rinha.backend.payment.app.consumer.PaymentConsumer;
import br.com.rinha.backend.payment.infra.dataprovider.PaymentDataProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PaymentConsumerConfig {

  @Value("${payment.queue.threads:}")
  private int queueThreads;

  @Bean
  public boolean initializeQueueExecutor(PaymentDataProvider paymentDataProvider) {
    PaymentConsumer.initialize(paymentDataProvider, queueThreads);
    return true;
  }
}
