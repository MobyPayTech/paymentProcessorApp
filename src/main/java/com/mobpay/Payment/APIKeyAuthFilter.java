package com.mobpay.Payment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
	private Map<String, String> keySecretMap = new HashMap<>();
	
	@Autowired          
    private RedisTemplate<String, PaymentProcessorAuthDao> redisTemplateForAuth;

	public APIKeyAuthFilter() {
	}

	public APIKeyAuthFilter(String[] key, HashMap<String, String> keySecretMap) {
		this.key = key;
		this.keySecretMap = keySecretMap;
	}

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) throws BadCredentialsException {
		Object returnValue = null;
//		HashMap<String, String> keySecretValueFromDB = getKeySecretValueFromDB();
		List<PaymentProcessorAuthDao> secretValuesToRedis = getSecretValuesToRedis();
		Map<String, String> keySecretValueFromDB = secretValuesToRedis.stream().collect(
				Collectors.toMap(e1 -> e1.getApi_key().toLowerCase(), e2 -> e2.getApi_secret()));
//		String[] keyValueFromDBString = getKeyValueFromDBStringToRedis();
		List<PaymentProcessorAuthDao> keyValuesToRedis = getKeyValueFromDBStringToRedis();
		List<String> apiKeyValuesFromRedis = keyValuesToRedis.stream().map(element -> element.getApi_key().toLowerCase()).collect(Collectors.toList());
		String[] keyValueFromDBString = apiKeyValuesFromRedis.stream().toArray(String[]::new);
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
	
	public List<PaymentProcessorAuthDao> getKeyValueFromDBStringToRedis() {
		List<PaymentProcessorAuthDao> configValues = paymentProcessorAuthRepository.findAll();
		System.out.println("Size " + configValues.size());
		List<PaymentProcessorAuthDao> redisData = new ArrayList<>();
		for (int i = 0; i < configValues.size(); i++) {
			redisTemplateForAuth.opsForValue().set("paymentProcessor/"+configValues.get(i).getId(), configValues.get(i));
		}
		for(int j=0;j<configValues.size();j++) {
			redisData.add(redisTemplateForAuth.opsForValue().get("paymentProcessor/"+configValues.get(j)));
			System.out.println(redisData.get(j));
		}
		return redisData;
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
	
	public List<PaymentProcessorAuthDao> getSecretValuesToRedis() {
		List<PaymentProcessorAuthDao> configValues = paymentProcessorAuthRepository.findAll();
		System.out.println("Size " + configValues.size());
		List<PaymentProcessorAuthDao> redisData = new ArrayList<>();
		for (int i = 0; i < configValues.size(); i++) {
			redisTemplateForAuth.opsForValue().set("paymentProcessor/"+configValues.get(i), configValues.get(i));
		}
		for(int j=0;j<configValues.size();j++) {
			redisData.add(redisTemplateForAuth.opsForValue().get("paymentProcessor/"+configValues.get(j)));
			System.out.println(redisData.get(j));
		}
		return redisData;
	}

}
