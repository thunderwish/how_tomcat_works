package com.thunderwish.chapter1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 封装响应信息类
 * 将静态资源./index.html按照Http协议返回
 * 原书中并没有writeStatusLine()方法
 * @author sql
 *
 */
public class HttpResponse {

	private OutputStream outputStream;
	private HttpRequest request;

	public HttpResponse(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	private static final int BUFFER_SIZE = 1024;

	public void setHttpRequest(HttpRequest request) {
		this.request = request;
	}

	public void sendStaticResource() throws IOException {

		byte[] bytes = new byte[BUFFER_SIZE];
		InputStream inputStream = null;

		try {
			File file = new File(HttpServer.ROOT, request.getURI());
			if (file.exists()) {
				//写http响应状态行
				writeStatusLine();
				
				inputStream = new FileInputStream(file);
				int hasread = inputStream.read(bytes);
				while (hasread != -1) {
					outputStream.write(bytes, 0, hasread);
					hasread = inputStream.read(bytes);
				}

			} else {
				String errorMessage = 	"HTTP/1.1 404 File Not Found\r\n" + 
										"Content-Type: text/html\r\n"+
										"Content-Length: 23\r\n" + 
										"\r\n" + 
										"<h1>File Not Found</h1>";
				outputStream.write(errorMessage.getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}

	}
	
	/**
	 * 如果不写响应状态和头部信息，浏览器无法解析
	 * @throws IOException
	 */
	private void writeStatusLine() throws IOException{
		//注意http响应格式，否则浏览器无法解析
		String statusLine = "HTTP/1.1 200 OK\r\n"+
							"Content-Type:text/html; charset=utf-8\r\n"+
							"\r\n";
		outputStream.write(statusLine.getBytes());
	}

}
