package br.com.rinha.backend.payment.app.consumer;

import br.com.rinha.backend.payment.infra.dataprovider.PaymentDataProvider;
import br.com.rinha.backend.payment.infra.dataprovider.model.PaymentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

public final class PaymentConsumer {

  private PaymentDataProvider paymentDataProvider;
  private static PaymentConsumer instance;
  private static final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
  private static ObjectMapper objectMapper = new ObjectMapper();

  private static final Logger logger = LoggerFactory.getLogger(PaymentConsumer.class);

  @Async
  public static void addToQueue(String request) {
    queue.add(request);
  }

  public static void initialize(PaymentDataProvider dataprovider, int queueThreads) {
    instance = new PaymentConsumer();
    instance.paymentDataProvider = dataprovider;
    instance.start(queueThreads);
  }

  private void start(int queueThreads) {
    for (int i = 0; i < queueThreads; i++) {
      var exec = Executors.newVirtualThreadPerTaskExecutor();
      exec.execute(() -> {
        while (true) {
          try {
            processPayment(queue.take());
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      });
    }
  }

  private void processPayment(String info) {
    try {
      final var paymentDate = ZonedDateTime.now();
      var paymentData = objectMapper.readValue(info, PaymentRequest.class);
      paymentData.setRequestedAt(ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

      var response = paymentDataProvider.createPayment(paymentData, 0);
      paymentDataProvider.addMetrics(paymentData.getCorrelationId(), paymentDate, paymentData.getAmount(), response);
//      logger.info("Pagamento processado com sucesso :: {} :: {} :: {}", response, paymentData.getCorrelationId(), paymentDate);

    } catch (Exception e) {
      logger.error("Pagamento não processado :: Erro: {}", e.getMessage());
    }
  }
}
