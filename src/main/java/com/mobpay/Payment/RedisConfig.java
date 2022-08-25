package com.mobpay.Payment;

import java.util.Set;

import javax.annotation.PreDestroy;

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
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.mobpay.Payment.dao.PaymentProcessorAuthDao;
import com.mobpay.Payment.dao.PaymentProcessorsysconfig;

import lombok.Setter;
import redis.clients.jedis.Jedis;

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Setter
public class RedisConfig {

	private String host;
	private String password;
	private String username;
	private int port;

	@Bean
	@Primary
	public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(RedisConfiguration defaultRedisConfig) {
		LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder().useSsl().build();
		return new LettuceConnectionFactory(defaultRedisConfig, clientConfig);
	}

	
	public @PreDestroy void flushDb() {
		Jedis jedis = new Jedis(host, port);
		jedis.auth(username, password);
//		jedis.auth(password); //to run in local
		Set<String> keys = jedis.keys("paymentProcessor/*");
		for (String key : keys) {
			jedis.del(key);
		}
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
		template.setKeySerializer(new StringRedisSerializer());
		template.setConnectionFactory(connectionFactory);
		return template;
	}

	@Bean
	public RedisTemplate<String, PaymentProcessorAuthDao> apiKeyValueTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, PaymentProcessorAuthDao> apiKeyValueTemplate = new RedisTemplate<>();
		apiKeyValueTemplate.setKeySerializer(new StringRedisSerializer());
		apiKeyValueTemplate.setConnectionFactory(connectionFactory);
		return apiKeyValueTemplate;
	}
}
