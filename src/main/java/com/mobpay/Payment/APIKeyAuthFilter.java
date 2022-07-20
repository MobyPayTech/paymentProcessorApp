package com.mobpay.Payment;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import com.mobpay.Payment.Repository.PaymentProcessorAuthRepository;
import com.mobpay.Payment.dao.PaymentProcessorAuthDao;

public class APIKeyAuthFilter extends AbstractPreAuthenticatedProcessingFilter {

	private final String ERROR_CUSTOM = "403 Forbidden";

	@Autowired
	PaymentProcessorAuthRepository paymentProcessorAuthRepository;

	private String principalRequestHeader;
	private static String keyInput;
	private String[] key;
	private HashMap<String, String> keySecretMap = new HashMap<>();

	public APIKeyAuthFilter() {
	}

	public APIKeyAuthFilter(String[] key, HashMap<String, String> keySecretMap) {
		this.key = key;
		this.keySecretMap = keySecretMap;
	}

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) throws BadCredentialsException {
		Object returnValue = null;
		HashMap<String, String> keySecretValueFromDB = getKeySecretValueFromDB();
		String[] keyValueFromDBString = getKeyValueFromDBString();
		key = keyValueFromDBString;
		keySecretMap = keySecretValueFromDB;
		if (request.getHeaderNames().asIterator().hasNext()) {
			principalRequestHeader = request.getHeaderNames().asIterator().next();
		}
		List<String> keyList = Arrays.asList(key);
		if (!keyList.contains(principalRequestHeader)) {
			returnValue = ERROR_CUSTOM;
		} else if (keyList.contains(principalRequestHeader)) {

			if (keySecretMap.get(principalRequestHeader).equalsIgnoreCase(request.getHeader(principalRequestHeader))) {
				returnValue = request.getHeader(principalRequestHeader);
				keyInput = principalRequestHeader;
				setKeyAndValue();
			}
		} else {
			returnValue = ERROR_CUSTOM;
		}
		return returnValue;
	}

	public static HashMap<String, String> setKeyAndValue() {
		HashMap<String, String> keyValueMap = new HashMap<String, String>();
		keyValueMap.put("headerKey", keyInput);
		System.out.println("keyValueMap " + keyValueMap);
		return keyValueMap;

	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		return "N/A";
	}

	public String[] getKeyValueFromDBString() {
		HashMap<String, String> keyMap = new HashMap<String, String>();
		List<PaymentProcessorAuthDao> configValues = paymentProcessorAuthRepository.findAll();
		String[] keyfromDb = new String[configValues.size() + 1];
		System.out.println("Size " + configValues.size());
		for (int i = 0; i < configValues.size(); i++) {
			keyfromDb[i] = configValues.get(i).getApi_key().toLowerCase();
		}
		System.out.println("keyfromDb " + keyfromDb);
		return keyfromDb;
	}

	public HashMap<String, String> getKeySecretValueFromDB() {
		HashMap<String, String> keyMap = new HashMap<String, String>();
		List<PaymentProcessorAuthDao> configValues = paymentProcessorAuthRepository.findAll();
		String[] keyfromDb = new String[configValues.size() + 1];
		System.out.println("Size " + configValues.size());
		for (int i = 0; i < configValues.size(); i++) {
			keyMap.put(configValues.get(i).getApi_key().toLowerCase(), configValues.get(i).getApi_secret());
		}
		keyMap.entrySet().forEach(entry -> {
		    System.out.println("Api Keys---->"+entry.getKey() + "-----APi Values-----" + entry.getValue());
		});
		return keyMap;
	}

}
