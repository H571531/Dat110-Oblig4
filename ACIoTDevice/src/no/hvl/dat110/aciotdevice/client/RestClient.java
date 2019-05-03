package no.hvl.dat110.aciotdevice.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.google.gson.Gson;

public class RestClient {

	public RestClient() {
		// TODO Auto-generated constructor stub
	}

	private static String logpath = "/accessdevice/log/";
	
	public void doPostAccessEntry(String message) {

		// TODO: implement a HTTP POST on the service to post the message
		
		try(Socket s = new Socket(Configuration.host, Configuration.port)){
			
			AccessMessage accessmessage = new AccessMessage(message);
			Gson gson = new Gson();
			String body = gson.toJson(accessmessage);
			
			String httpPostRequest = "POST " + logpath + " HTTP/1.1\r\n"
					+ "Host: " + Configuration.host + "\r\n"
					+ "Content-type: application/json\r\n"
					+ "Content-length: " + body.length() + "\r\n"
					+ "Connection: close\r\n"
					+ "\r\n"
					+ gson.toJson(accessmessage)
					+"\r\n";
			OutputStream output = s.getOutputStream();
			
			PrintWriter pw = new PrintWriter(output, false);
			
			pw.print(httpPostRequest);
			pw.flush();
			
			
			//Ikke gitt i oppgave om response skal skrives ut

		
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static String codepath = "/accessdevice/code/";
	
	public AccessCode doGetAccessCode() {

		AccessCode code = null;
		
		// TODO: implement a HTTP GET on the service to get current access code
		
		try (Socket s = new Socket(Configuration.host, Configuration.port)){
			String httpGetRequest = "GET " + codepath + " HTTP/1.1\r\n" + "Accept: application/json\r\n"
					+ "Host: " + Configuration.host + "\r\n"
					+ "Connection: close\r\n"
					+ "\r\n";
			
			OutputStream output = s.getOutputStream();
			
			PrintWriter pw = new PrintWriter(output, false);
			
			pw.print(httpGetRequest);
			pw.flush();
			
			InputStream in = s.getInputStream();
			
			Scanner scanner = new Scanner(in);
			
			StringBuilder jsonResponse = new StringBuilder();
			boolean header = true;
			
			while(scanner.hasNext()) {
				String nextLine = scanner.nextLine();
				
				//Ikke gitt i oppgave om response skal skrives ut
				if(!header) {
					jsonResponse.append(nextLine);
				}
				
				if(nextLine.isEmpty()) {
					header = false;
				}
			}
			
			Gson gson = new Gson();
			code = gson.fromJson(jsonResponse.toString(), AccessCode.class);
			
			scanner.close();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return code;
	}
}
