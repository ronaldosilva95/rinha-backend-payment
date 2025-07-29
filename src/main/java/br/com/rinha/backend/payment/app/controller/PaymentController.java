package br.com.rinha.backend.payment.app.controller;

import br.com.rinha.backend.payment.app.controller.model.PaymentRequest;
import br.com.rinha.backend.payment.app.controller.model.SummaryResponse;
import br.com.rinha.backend.payment.app.service.PaymentService;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class PaymentController {

  @Autowired
  private PaymentService paymentService;

  @GetMapping("/payment")
  public ResponseEntity<Void> payment(@RequestBody PaymentRequest request) {
    paymentService.createPayment(request);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
  @GetMapping("/payments-summary")
  public ResponseEntity<SummaryResponse> paymentsSummary(@RequestParam("from") LocalDateTime startDate,
      @RequestParam("to") LocalDateTime endDate) {
    paymentService.getPaymentSummary(startDate, endDate);
    return ResponseEntity.ok(paymentService.getPaymentSummary(startDate, endDate));
  }

}
