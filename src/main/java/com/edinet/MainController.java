package com.edinet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
@RequestMapping(path="/edinet") // This means URL's start with /demo (after Application path)
public class MainController {

  @GetMapping(path="/get")
  public @ResponseBody String getEdinetData() throws IOException {

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
	  // 日付フォーマット(yyyy-MM-dd形式)
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


	  // EdinetAPIのURL
	  String baseUrl = "https://disclosure.edinet-fsa.go.jp/api/v1/";
	  // 書類ID一覧取得
	  String reqGetDocIdList = "documents.json";
	  // リクエストする日付
//	  String reqDate = "date=" + sdf.format(date.getTime());
	  String reqDate = "date=2019-04-16"; // test
	  // Getするドキュメントタイプ(type: 1 でメタデータのみ、 2 で提出書類一覧およびメタデータを取得)
	  String reqDocType = "type=2";
	  // URL生成
	  String docIdListUrl = baseUrl + reqGetDocIdList + "?" + reqDate + "&" + reqDocType;
	  URL url = new URL(docIdListUrl);

	  // リクエスト実行
	  HttpURLConnection con = (HttpURLConnection) url.openConnection();
	  BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	  String inputLine;
	  StringBuilder sb = new StringBuilder();
	  // リクエストの結果を取り出す
	  while ((inputLine = in.readLine()) != null) {
		  sb.append(inputLine);
	  }
	  in.close();

	  // JSON -> Java
	  ObjectMapper mapper = new ObjectMapper();
	  DocumentIdList docIdList = mapper.readValue(sb.toString(), DocumentIdList.class);

	  List<Result> list = new ArrayList<Result>();
	  list = docIdList.getResults();

	  // 書類IDを取り出す
	  for(Result result : list) {
		  System.out.println(result.getDocID());
	  }

	  // 書類Zip取得処理
	  // 書類ID
	  String docId = null;
	  // 書類取得
	  String reqGetDoc = "documents";
	  // 書類データ取得URL生成
	  String getDocUrl = baseUrl + reqGetDoc + "/" + docId + "?" + reqDocType;

	  return "Success!";
  }
}