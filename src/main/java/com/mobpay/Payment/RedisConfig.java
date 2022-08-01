package com.mobpay.Payment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.mobpay.Payment.dao.PaymentProcessorAuthDao;
import com.mobpay.Payment.dao.PaymentProcessorsysconfig;

import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Setter
public class RedisConfig {

	private String host;
	private String password;
	private String username;

	@Bean
	@Primary
	public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(RedisConfiguration defaultRedisConfig) {
		LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder().useSsl().build();
		return new LettuceConnectionFactory(defaultRedisConfig, clientConfig);
	}

	@Bean
	public RedisConfiguration defaultRedisConfig() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		config.setHostName(host);
		config.setPassword(RedisPassword.of(password));
		config.setUsername(username);
		return config;
	}

	@Bean
	public RedisTemplate<String, PaymentProcessorsysconfig> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, PaymentProcessorsysconfig> template = new RedisTemplate<>();
		RedisTemplate<String, PaymentProcessorAuthDao> apiKeyValueTemplate = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		apiKeyValueTemplate.setConnectionFactory(connectionFactory);
		return template;
	}

	@Bean
	public RedisTemplate<Integer, PaymentProcessorAuthDao> apiKeyValueTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<Integer, PaymentProcessorAuthDao> apiKeyValueTemplate = new RedisTemplate<>();
		apiKeyValueTemplate.setConnectionFactory(connectionFactory);
		return apiKeyValueTemplate;
	}
}
