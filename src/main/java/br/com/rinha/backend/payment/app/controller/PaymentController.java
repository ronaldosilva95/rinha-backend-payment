package br.com.rinha.backend.payment.app.controller;

import br.com.rinha.backend.payment.app.consumer.PaymentConsumer;
import br.com.rinha.backend.payment.app.controller.model.SummaryResponse;
import br.com.rinha.backend.payment.app.service.PaymentService;
import br.com.rinha.backend.payment.infra.dataprovider.model.PaymentRequest;
import java.time.ZonedDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

  @Autowired
  private PaymentService paymentService;

  @PostMapping("/payments")
  public ResponseEntity<Void> payment(@RequestBody String request) {
//    paymentService.createPayment(request);
    PaymentConsumer.addToQueue(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
  @GetMapping("/payments-summary")
  public ResponseEntity<SummaryResponse> paymentsSummary(@RequestParam("from") ZonedDateTime startDate,
      @RequestParam("to") ZonedDateTime endDate) {
    return ResponseEntity.ok(CompletableFuture.supplyAsync(() -> paymentService.getPaymentSummary(startDate, endDate)).join());
  }

}
