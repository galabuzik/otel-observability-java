package com.paymentology.orders_service.config;

import com.paymentology.orders_service.model.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Bean
    LettuceConnectionFactory jedisConnectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory
                = new LettuceConnectionFactory();
        lettuceConnectionFactory.setHostName("redis");
        lettuceConnectionFactory.setPort(6379);
        return lettuceConnectionFactory;
    }

    @Bean
    RedisTemplate<String, Order> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Order> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
