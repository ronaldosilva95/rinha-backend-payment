package br.com.rinha.backend.payment.app.consumer;

import br.com.rinha.backend.payment.infra.repository.RedisRepository;
import br.com.rinha.backend.payment.infra.dataprovider.PaymentDataProvider;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentConsumer implements StreamListener<String, MapRecord<String, String, String>> {

  private final PaymentDataProvider paymentDataProvider;
  private final RedisRepository redisRepository;
  private static final Logger logger = LoggerFactory.getLogger(PaymentConsumer.class);

  public PaymentConsumer(PaymentDataProvider paymentDataProvider, RedisRepository redisRepository) {
    this.paymentDataProvider = paymentDataProvider;
    this.redisRepository = redisRepository;
  }

  @Override
  public void onMessage(MapRecord<String, String, String> message) {
    final var paymentDate = LocalDateTime.now();
    try {
      var amount = new BigDecimal(message.getValue().get("amount"));

      var response = paymentDataProvider.createPayment(paymentDate, message.getValue().get("correlationId"), amount);
      redisRepository.addMetrics(message.getValue().get("correlationId"), paymentDate, amount, response);
      logger.info("Pagamento processado com sucesso :: {} :: {}", response, message.getValue().get("correlationId"));

    } catch (Exception e) {
      logger.error("Pagamento não processado:: {} :: Erro: {}", message.getValue().get("correlationId"), e.getMessage());
    }
  }
}
