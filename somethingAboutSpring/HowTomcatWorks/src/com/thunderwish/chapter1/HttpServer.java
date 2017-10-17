package com.thunderwish.chapter1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务器启动类
 * @author sql
 *
 */
public class HttpServer {

	public static final String ROOT = System.getProperty("user.dir") + File.separator + "webroot";
	private static final String SHUT_DOWM = "shutdown";
	private boolean shutdown;
	
	public static void main(String[] args) {
		
		HttpServer server = new HttpServer();
		server.await();
		
	}
	
	public void await(){
		
		ServerSocket server = null;
		int port = 9999;
		
		try {
			server = new ServerSocket(port, 2, InetAddress.getByName("127.0.0.1"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		while (!shutdown) {
			Socket socket = null;
			InputStream inputStream = null;
			OutputStream outputStream = null;

			try {
				socket = server.accept();
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();

				HttpRequest request = new HttpRequest(inputStream);
				request.parse();

				HttpResponse response = new HttpResponse(outputStream);
				response.setHttpRequest(request);
				response.sendStaticResource();
				
				shutdown = SHUT_DOWM.equals(request.getURI());
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}finally{
				//处理完请求应该关闭socket，否则浏览器会一直等待
				if(socket!=null){
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		
	}

}
