package com.thunderwish.chapter1;

import java.io.IOException;
import java.io.InputStream;

/**
 * 封装请求信息类
 * 按照Http协议从InputStream中解析信息
 * @author sql
 *
 */
public class HttpRequest {

	private String uri;
	private InputStream inputStream;

	public HttpRequest(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getURI() {
		return this.uri;
	}

	public void parse() {

		StringBuffer sb = new StringBuffer(2048);
		int i;
		byte[] bytes = new byte[2048];

		try {
			i = inputStream.read(bytes);
		} catch (IOException e) {
			i = -1;
		}

		if (i > 0) {
			for (int j = 0; j < i; j++) {
				sb.append((char) bytes[j]);
			}
		}

		System.out.println("server recieve " + sb.toString());
		uri = parseURI(sb.toString());

	}

	private String parseURI(String requestString) {

		int index1, index2;

		index1 = requestString.indexOf(" ");
		if (index1 != -1) {
			index2 = requestString.indexOf(" ", index1 + 1);
			return requestString.substring(index1 + 1, index2);
		}
		return null;

	}
}
