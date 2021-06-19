package com.edinet.domain.services;

import java.net.HttpURLConnection;
import java.net.URL;

abstract class HttpConnectionBase {

	// EdinetAPIのURL
	final String baseUrl = "https://disclosure.edinet-fsa.go.jp/api/v1/";

	public HttpURLConnection getConnection(String date, String param) throws Exception {
		URL url = new URL(baseUrl + param);;
		// サーバへ接続
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		return con;
	}
}
