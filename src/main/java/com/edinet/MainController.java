package com.edinet;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edinet.jacson.DocumentIdList;
import com.edinet.jacson.Result;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller // This means that this class is a Controller
@RequestMapping(path="/edinet")
public class MainController {

	// EdinetAPIのURL
	final String baseUrl = "https://disclosure.edinet-fsa.go.jp/api/v1/";

	@GetMapping(path="/getZipFile")
	public @ResponseBody String getEdinetData() throws Exception {

		/*
		// 取得する日付をどう処理するかここに書きたい
		// Edinetから取得する日付をテーブルからselect
		Dates dates = new Dates();
		Calendar edinetLastGetDate = dates.getLastGetDate();
		//
		if(edinetLastGetDate.toString().isEmpty()) {
		// nullの場合は、今日から5年前の日付をセットする
		edinetLastGetDate = Calendar.getInstance();
		edinetLastGetDate.add(Calendar.YEAR, -5);
		} else {
		// 1日プラス
		edinetLastGetDate.add(Calendar.DATE, 1);
		}
		 */

		// 今日の日付を取得
		Calendar date = Calendar.getInstance();
		// 日付の形式(yyyy-MM-dd形式)
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// リクエストする日付をフォーマット
		String reqDate = sdf.format(date.getTime());
//		String reqDate = "2020-12-09"; // TODO:確認用。あとで消す

		// 書類IDリスト取得
		DocumentIdList docIdList = getDocIdList(reqDate);

		// 書類IDが0件の場合は終了
		if(docIdList.getMetadata().getResultset().getCount() == 0) {
			System.out.println("書類の件数が0件なので処理終了");
			return "Success";
		}

		// 書類IDリストを返却
		List<Result> list = docIdList.getResults();

		// 日付ディレクトリを作成
		File dir = new File(reqDate);
		dir.mkdir();

		//-- 書類Zip取得処理
		getZipFile(reqDate, list);

		return "Success!";
	}

	/**
	 * Edinetから書類IDのリストを取得する
	 * @param date
	 * @return
	 */
	public DocumentIdList getDocIdList(String date){

		URL url;
		DocumentIdList docIdList = null;

		try {
			// 書類ID取得用URL生成
			url = generateUrl(baseUrl + "documents.json" + "?" + "date=" + date + "&" + "type=2");

			// サーバへ接続
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			// HTTPステータス異常の場合は処理終了
			if(con.getResponseCode() != HttpURLConnection.HTTP_OK) {
				System.out.println("HTTP Status Code:" + con.getResponseCode());
				System.exit(1);
			}

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
			docIdList = mapper.readValue(sb.toString(), DocumentIdList.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return docIdList;
	}

	/**
	 * ZIPファイルをダウンロードする
	 * @param date
	 * @param list
	 */
	public void getZipFile(String date ,List<Result> list) {

		URL url;
		String zipName = null;
		// Read Data
		byte[] b = new byte[4096];
		int readByte = 0;

		try {
			// 書類リストから書類IDを取り出す
			for(Result result : list) {
				// 書類ID
				String docId = result.getDocID();
				// 書類データ取得URL生成
				url = generateUrl(baseUrl + "documents" + "/" + docId + "?type=1");

				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				// 日付ディレクトリ配下にzipファイルを格納
				zipName = date + "/" + docId + ".zip";

				DataInputStream dataInStream = new DataInputStream(con.getInputStream());
				DataOutputStream dataOutStream =
						new DataOutputStream(
								new BufferedOutputStream(
										new FileOutputStream(zipName)
										)
								);
				//
				while (-1 != (readByte = dataInStream.read(b))) {
					dataOutStream.write(b, 0, readByte);
				}

				// Close Stream
				dataInStream.close();
				dataOutStream.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文字列からURLオブジェクトに変換する
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public URL generateUrl(String url) throws Exception {
		return new URL(url);
	}
}