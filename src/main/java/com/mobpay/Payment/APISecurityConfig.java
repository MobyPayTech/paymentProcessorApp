package com.mobpay.Payment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;

import com.mobpay.Payment.Repository.PaymentProcessorAuthRepository;
import com.mobpay.Payment.Repository.PaymentProcessorConfigRepository;
import com.mobpay.Payment.Service.GlobalConstants;
import com.mobpay.Payment.dao.PaymentProcessorAuthDao;

@EnableWebSecurity
@Order(1)
public class APISecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	PaymentProcessorAuthRepository paymentProcessorAuthRepository;

	@Autowired
	PaymentProcessorConfigRepository paymentProcessorConfigRepository;

	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.antMatcher("/**").csrf().disable().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().addFilter(tokenProcessingFilter())
				.authorizeRequests().anyRequest().authenticated();
	}

	public String[] getSecretValueFromDB() {
		HashMap<String, String> keyMap = new HashMap<String, String>();
		List<PaymentProcessorAuthDao> configValues = paymentProcessorAuthRepository.findAll();
		String[] secretfromDb = new String[configValues.size() + 1];
		System.out.println("Size " + configValues.size());
		for (int i = 0; i < configValues.size(); i++) {
			secretfromDb[i] = configValues.get(i).getApi_secret();
		}
		System.out.println("secretfromDb " + secretfromDb);

		return secretfromDb;
	}

	private String getAuthEnableDetailsFromDB() {
		String configValues = paymentProcessorConfigRepository.findValueFromName(GlobalConstants.PLATFORM_AUTH);
		return configValues;
	}

	@Bean
	APIKeyAuthFilter tokenProcessingFilter() throws Exception {
		APIKeyAuthFilter tokenProcessingFilter = new APIKeyAuthFilter();
		tokenProcessingFilter.setAuthenticationManager(new AuthenticationManager() {
			@Override
			public Authentication authenticate(Authentication authentication) throws BadCredentialsException {
				String authEnableDetailsFromDB = getAuthEnableDetailsFromDB();
				if (StringUtils.equalsIgnoreCase(authEnableDetailsFromDB, "1")) {
					String principal = (String) authentication.getPrincipal();

					String[] secretfromDb = getSecretValueFromDB();
					List<String> secretList = Arrays.asList(secretfromDb);

					if (!secretList.contains(principal)) {
						throw new BadCredentialsException("401");
					}
					authentication.setAuthenticated(true);
					return authentication;
				} else {
					authentication.setAuthenticated(true);
					return authentication;
				}
			}
		});
		return tokenProcessingFilter;
	}

}