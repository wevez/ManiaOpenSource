package wtf.mania.util.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import wtf.mania.util.login.AltUtils.Response;

public class NetworkUtils {

	private static final String USER__AGENT = "Mozilla/5.0",
			API_URL = "http://api.isla.jp:3579/api/mania/",
			API_KEY = "d1212736f46f45c72b10ce20a363f418";

	public static Response sendGet(String url) {

		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("User-Agent", USER__AGENT);

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
			return new Response(response.toString(), responseCode, con.getHeaderFields());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Response API() {
		try {
	        HttpPost post = new HttpPost(API_URL);

	        post.addHeader("Content-Type", "application/json");
	        post.addHeader("Pragma", "no-cache");

	        post.setEntity(new StringEntity("{\"API-Key\": \""+API_KEY+"\"}"));

	        try (CloseableHttpClient httpClient = HttpClients.createDefault();
	            CloseableHttpResponse response = httpClient.execute(post)) {
	        	return new Response(EntityUtils.toString(response.getEntity()), response.getStatusLine().getStatusCode(), null);
	        }
		} catch (Exception e) {
		}
		return null;
	}

}