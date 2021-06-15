package com.domain.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.edinet.controllers.MainController;
import com.edinet.jacson.DocumentInfoList;
import com.edinet.jacson.Result;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GetDocIdListService {

	// Log4j2
	final Logger logger = LogManager.getLogger(MainController.class.getName());
	// EdinetAPIのURL
	final String baseUrl = "https://disclosure.edinet-fsa.go.jp/api/v1/";
	// 府令コード
	final String ordinanceCode = "010";
	// 有価証券コード
	final String securitiesReport = "030000";
	// 四半期報告書コード
	final String quarterlyReport = "043000";

	/**
	 * Edinetから書類一覧を取得する
	 * @param date
	 * @return
	 */
	public List<Result> getDocIdList(String date){

		URL url;
		DocumentInfoList docInfoList = null;
		List<Result> docIdList = null;

		try {
			// 書類一覧取得用URL生成
			url = new URL(baseUrl + "documents.json" + "?" + "date=" + date + "&" + "type=2");

			// サーバへ接続
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			// HTTPステータスOKの場合に処理する
			if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {

				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuilder sb = new StringBuilder();

				// リクエストの結果を取り出す
				while ((inputLine = in.readLine()) != null) {
					sb.append(inputLine);
				}
				in.close();

				// JSON -> Javaに変換
				ObjectMapper mapper = new ObjectMapper();
				docInfoList = mapper.readValue(sb.toString(), DocumentInfoList.class);

				// 書類IDを取得
				docIdList = getTargetDocList(docInfoList.getResults());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return docIdList;
	}

	// 有価証券報告書/四半期報告書の書類IDリストを返却する
	public List<Result> getTargetDocList(List<Result> docIdList){
		// 処理対象ドキュメントリスト
		List<Result> targetDocList = new ArrayList<Result>();

		for(Result result : docIdList) {
			// 府令コードが10で、有価証券報告書/四半期報告書の場合
			if(ordinanceCode.equals(result.getOrdinanceCode()) && securitiesReport.equals(result.getFormCode())
					|| ordinanceCode.equals(result.getOrdinanceCode()) && quarterlyReport.equals(result.getFormCode())) {
				targetDocList.add(result);
			}
		}
		return targetDocList;
	}
}
