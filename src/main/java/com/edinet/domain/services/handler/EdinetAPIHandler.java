package com.edinet.domain.services.handler;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.edinet.app.common.utills.FileUtill;
import com.edinet.jacson.DocumentInfoList;
import com.edinet.jacson.Result;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EdinetAPIHandler {

	// EdinetAPIのURL
	final String baseUrl = "https://disclosure.edinet-fsa.go.jp/api/v1/";
	// 府令コード
	final String ordinanceCode = "010";
	// 有価証券コード
	final String securitiesReport = "030000";
	// 四半期報告書コード
	final String quarterlyReport = "043000";

	/**
	 * EdinetAPIへ接続を行う
	 * @param date
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public HttpURLConnection getConnection(String date, String param) throws Exception {
		URL url = new URL(baseUrl + param);;
		// サーバへ接続
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		return con;
	}

	/**
	 * Edinetから書類リストをJSONで取得し、書類IDのみを取り出す
	 * @param date
	 * @return List<Rsult> docIdList
	 * @throws Exception
	 */
	public List<Result> getDocIdList(String date) throws Exception {
		DocumentInfoList docInfoList = null;
		List<Result> docIdList = null;
		String param = "documents.json" + "?" + "date=" + date + "&" + "type=2";
		String inputLine;
		StringBuilder sb = new StringBuilder();
		ObjectMapper mapper = new ObjectMapper();


		try (
				BufferedReader in = new BufferedReader(
						new InputStreamReader(
								getConnection(date, param).getInputStream()));
				){
			// リクエストの結果を取り出す
			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
			}

			docInfoList = mapper.readValue(sb.toString(), DocumentInfoList.class);

			// 書類IDを取得
			docIdList = getTargetDocList(docInfoList.getResults());
		}
		 catch (Exception e) {
			e.printStackTrace();
		 }

		return docIdList;
	}

	public void getZipFile(String date, List<Result> docList) throws Exception {

		String zipName = null;
		String docId = null;
		int readByte = 0;
		FileUtill fileUtill = new FileUtill();
		String param = "documents" + "/" + docId + "?type=1";
		// Read Data
		byte[] b = new byte[4096];

		// 取得した書類IDからZIPファイルをダウンロードする
		for(Result result : docList) {

			// 書類ID
			docId = result.getDocID();
			// 日付ディレクトリ配下にzipファイルを格納
			zipName = fileUtill.getCurrentDir() + date + "/" + docId + ".zip";

			try(
					DataInputStream dataInStream = new DataInputStream(
							getConnection(date, param).getInputStream());
					DataOutputStream dataOutStream =
							new DataOutputStream(
									new BufferedOutputStream(
											new FileOutputStream(zipName)
											)
									);
					){

				while (-1 != (readByte = dataInStream.read(b))) {
					dataOutStream.write(b, 0, readByte);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}





	// 有価証券報告書/四半期報告書の書類IDリストを返却する
	private List<Result> getTargetDocList(List<Result> docIdList){
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
