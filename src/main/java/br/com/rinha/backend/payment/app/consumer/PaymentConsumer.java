package br.com.rinha.backend.payment.app.consumer;

import br.com.rinha.backend.payment.app.controller.model.PaymentRequest;
import br.com.rinha.backend.payment.infra.dataprovider.PaymentDataProvider;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentConsumer {

  private PaymentDataProvider paymentDataProvider;
  private static PaymentConsumer instance;
  private static LinkedBlockingQueue<PaymentRequest> queue = new LinkedBlockingQueue<>();

  private static final Logger logger = LoggerFactory.getLogger(PaymentConsumer.class);

  public static void addToQueue(PaymentRequest request) {
    queue.add(request);
  }

  public static void initialize(PaymentDataProvider dataprovider, int queueThreads) {
    instance = new PaymentConsumer();
    instance.paymentDataProvider = dataprovider;
    instance.start(queueThreads);
  }

  public void start(int queueThreads) {
    var executor = Executors.newFixedThreadPool(queueThreads);
    executor.submit(() -> {
      while(true) {
        var item = queue.take();
        if (item == null) {
          continue;
        }

        executor.execute(() -> {
          try {
            final var paymentDate = ZonedDateTime.now();
            var response = paymentDataProvider.createPayment(paymentDate, item.correlationId(), item.amount());
            paymentDataProvider.addMetrics(item.correlationId(), paymentDate, item.amount(), response);
            logger.info("Pagamento processado com sucesso :: {} :: {} :: {}", response, item.correlationId(), paymentDate.format(
                DateTimeFormatter.ISO_LOCAL_DATE_TIME));

          } catch (Exception e) {
            logger.error("Pagamento não processado:: {} :: Erro: {}", item.correlationId(), e.getMessage());
          }
        });
      }
    });
  }
}
