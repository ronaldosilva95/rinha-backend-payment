package br.com.rinha.backend.payment.app.controller;

import br.com.rinha.backend.payment.app.controller.model.PaymentRequest;
import br.com.rinha.backend.payment.app.controller.model.SummaryResponse;
import br.com.rinha.backend.payment.app.service.PaymentService;
import java.time.ZonedDateTime;
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
  public ResponseEntity<Void> payment(@RequestBody PaymentRequest request) {
    paymentService.createPayment(request);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
  @GetMapping("/payments-summary")
  public ResponseEntity<SummaryResponse> paymentsSummary(@RequestParam("from") ZonedDateTime startDate,
      @RequestParam("to") ZonedDateTime endDate) {
    return ResponseEntity.ok(paymentService.getPaymentSummary(startDate, endDate));
  }

}
