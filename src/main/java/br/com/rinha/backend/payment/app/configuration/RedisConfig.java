package br.com.rinha.backend.payment.app.configuration;

import br.com.rinha.backend.payment.app.consumer.PaymentConsumer;
import br.com.rinha.backend.payment.infra.repository.RedisRepository;
import br.com.rinha.backend.payment.infra.dataprovider.PaymentDataProvider;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

@Configuration
public class RedisConfig {

  @Bean
  public StringRedisTemplate configureRedis() {
    LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
    connectionFactory.afterPropertiesSet();

    StringRedisTemplate template = new StringRedisTemplate();
    template.setConnectionFactory(connectionFactory);
    template.setDefaultSerializer(StringRedisSerializer.UTF_8);
    template.afterPropertiesSet();

    var teste = template.opsForStream().groups("payments-queue").isEmpty();
    if (teste) {
      template.opsForStream().createGroup("payments-queue", "my-consumer-instance");
    }

    return template;
  }

  @Bean
  public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer(
      RedisConnectionFactory redisConnectionFactory, PaymentDataProvider paymentDataProvider, RedisRepository redisService) {
    StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions
            .builder()
            .pollTimeout(Duration.ofSeconds(1))
            .build();

    StreamMessageListenerContainer<String, MapRecord<String, String, String>> container = StreamMessageListenerContainer.create(
        redisConnectionFactory, options);
    container.receive(
        Consumer.from("my-consumer-instance", "my-consumer-instance"),
        StreamOffset.create("payments-queue", ReadOffset.from(">")),
        new PaymentConsumer(paymentDataProvider, redisService)
    );

    container.start();
    return container;
  }

}
