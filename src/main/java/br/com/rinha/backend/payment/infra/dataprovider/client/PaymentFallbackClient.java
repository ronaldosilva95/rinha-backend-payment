package br.com.rinha.backend.payment.infra.dataprovider.client;

import br.com.rinha.backend.payment.infra.dataprovider.model.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "paymentFallbackClient", url = "${payment.service.fallback}")
public interface PaymentFallbackClient {

  @PostMapping("/payments")
  Object createPayment(@RequestBody final PaymentRequest request);

}
