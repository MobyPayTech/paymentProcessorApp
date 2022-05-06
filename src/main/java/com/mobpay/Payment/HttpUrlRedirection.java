package com.mobpay.Payment;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpUrlRedirection {

	  public static void main(String[] args) {

	    try {

		String url = "https://demo.curlec.com/chargeNow?merchantId=5354721&employeeId=536358&refNumber=MobyMoney123456&collectionAmount=10.00&invoiceNumber=INV001&method=chargenowOTP";
/*
		URL obj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		conn.setReadTimeout(5000);
		conn.addRequestProperty("Accept-Language", "application/json");
		conn.addRequestProperty("User-Agent", "Mozilla");
		conn.addRequestProperty("Referer", "google.com");

		System.out.println("Request URL ... " + url);

		boolean redirect = false;

		// normally, 3xx is redirect
		int status = conn.getResponseCode();
		if (status != HttpURLConnection.HTTP_OK) {
			if (status == HttpURLConnection.HTTP_MOVED_TEMP
				|| status == HttpURLConnection.HTTP_MOVED_PERM
					|| status == HttpURLConnection.HTTP_SEE_OTHER)
			redirect = true;
		}

		System.out.println("Response Code ... " + status);

		if (redirect) {

			// get redirect url from "location" header field
			String newUrl = conn.getHeaderField("Location");

			// get the cookie if need, for login
			String cookies = conn.getHeaderField("Set-Cookie");

			// open the new connnection again
			conn = (HttpURLConnection) new URL(newUrl).openConnection();
			conn.setRequestProperty("Cookie", cookies);
			conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
			conn.addRequestProperty("User-Agent", "Mozilla");
			conn.addRequestProperty("Referer", "google.com");
									
			System.out.println("Redirect to URL : " + newUrl);

		}

		BufferedReader in = new BufferedReader(
	                              new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer html = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			html.append(inputLine);
		}
		in.close();

		System.out.println("URL Content... \n" + html.toString());
		System.out.println("Done");

	    } catch (Exception e) {
		e.printStackTrace();
	    }

	  }
*/

		HttpClient client = HttpClient.newHttpClient();

	    HttpRequest request = HttpRequest.newBuilder()
	        .uri(URI.create(url))
	        .setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 12_3_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.41 Safari/537.36")
	        .build();
	 //  Object response =  client.send(request, null);
	    
	    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
	    
	    System.out.println("response " +response);
	}catch (Exception e) {
		e.printStackTrace();
    }
	  }
	  }