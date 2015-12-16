package util;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Http
{
	private String cookie = "";

	public void setCookie(String cookie)
	{
		this.cookie = cookie;
	}

	public String getCookie()
	{
		return cookie;
	}

	public String get(String url,String charset)
	{
		StringBuilder sb = new StringBuilder();
		try{
			URL _url = new URL(url);
		if(url.toLowerCase().contains("https://"))
		{
			HttpsHandler httpsH = new HttpsHandler();
			httpsH.trustAllHttpsCertificates();
			HostnameVerifier hv = new HostnameVerifier(){
				public boolean verify(String urlHostName, SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
			HttpsURLConnection sconn = (HttpsURLConnection)_url.openConnection();
			sconn.setRequestProperty("Cookie",cookie);
			BufferedReader br = new BufferedReader(new InputStreamReader(sconn.getInputStream(),charset));
			String temp = null;
			while((temp = br.readLine())!=null)
			{
				sb.append(temp);
				sb.append("\n");
			}
			br.close();
			String cookie = sconn.getHeaderField("Set-Cookie");
			if(cookie !=null){
				this.cookie = cookie;
			}
		}
		else
		{
			HttpURLConnection conn = (HttpURLConnection)_url.openConnection();
			conn.setRequestProperty("Cookie",cookie);
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),charset));
			String temp = null;
			while((temp = br.readLine())!=null)
			{
				sb.append(temp);
				sb.append("\n");
			}
			br.close();
			String cookie = conn.getHeaderField("Set-Cookie");
			if(cookie !=null){
				this.cookie = cookie;
			}
		}
		}catch(Exception e){return null;}
		return sb.toString();
	}

	public String post(String url,String data,String charset)
	{
		StringBuilder sb = new StringBuilder();
		try{
		URL _url = new URL(url);
		if(url.toLowerCase().contains("https://"))
		{
			HttpsHandler httpsH = new HttpsHandler();
			httpsH.trustAllHttpsCertificates();
			HostnameVerifier hv = new HostnameVerifier(){
				public boolean verify(String urlHostName, SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
			HttpsURLConnection sconn = (HttpsURLConnection)_url.openConnection();
			sconn.setDoInput(true);
			sconn.setDoOutput(true);
			sconn.setRequestProperty("Cookie",cookie);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sconn.getOutputStream(),charset));
			bw.write(data);
			bw.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader(sconn.getInputStream(),charset));
			String temp = null;
			while((temp = br.readLine())!=null)
			{
				sb.append(temp);
				sb.append("\n");
			}
			br.close();
			String cookie = sconn.getHeaderField("Set-Cookie");
			if(cookie !=null){
				this.cookie = cookie;
			}
		}
		else
		{
			HttpURLConnection conn = (HttpURLConnection)_url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Cookie",cookie);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),charset));
			bw.write(data);
			bw.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),charset));
			String temp = null;
			while((temp = br.readLine())!=null)
			{
				sb.append(temp);
				sb.append("\n");
			}
			br.close();
			bw.close();
			String cookie = conn.getHeaderField("Set-Cookie");
			if(cookie !=null){
				this.cookie = cookie;
			}
		}
		}catch(Exception e){return null;}
		return sb.toString();
	}

	public byte[] file(String url)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try{
		URL _url = new URL(url);
		HttpURLConnection conn = (HttpURLConnection)_url.openConnection();
		conn.setRequestProperty("Cookie",cookie);
		BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
		byte[] b = new byte[1024];
		int len = -1;
		while((len = bis.read(b))!=-1)
		{
			bos.write(b,0,len);
			bos.flush();
		}
		bos.close();
		bis.close();
		String cookie = conn.getHeaderField("Set-Cookie");
		if(cookie !=null){
			this.cookie = cookie;
		}
		}catch(Exception e){return null;}
		return bos.toByteArray();
	}

	
	public static void downloadPics(List<String> paths){
	    Http http = new Http();
	    OutputStream out = null;
	    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
	    try {
		for (String string : paths) {
		 byte[] b = http.file(string);
		 String time = df.format(new Date());
		 String name = "d://dl//"+time +".jpg";
		 File file = new File(name);
		 out = new FileOutputStream(file);
		 out.write(b);
		 System.out.println("d://dl//"+time+".jpg Success");
		}
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		finally{
		    try {
			out.close();
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}
	}
	
	public static List<String> getUrls(String rules,String html){
	    Matcher m = Pattern.compile(rules).matcher(html);
    	    List<String> list = new ArrayList<String>();
    	    while (m.find()) 
    	        list.add(m.group(1));
    	    return list;
	}



	private class HttpsHandler {
		public void trustAllHttpsCertificates() throws Exception {
			TrustManager[] tm_array = new TrustManager[1];
			TrustManager tm = new MyTrustManager();
			tm_array[0] = tm;
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, tm_array, null);
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		}
		private class MyTrustManager implements TrustManager, X509TrustManager {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
			@SuppressWarnings("unused")
			public boolean isServerTrusted(X509Certificate[] certs) {
				return true;
			}
			@SuppressWarnings("unused")
			public boolean isClientTrusted(X509Certificate[] certs) {
				return true;
			}
			public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
				return;
			}
			public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
				return;
			}
		}
	}
}
