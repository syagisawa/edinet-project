package com.edinet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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


	  // 書類取得APIのURL
	  String baseURL = "https://disclosure.edinet-fsa.go.jp/api/v1/documents.json";
	  // リクエストする日付
	  String requestDate = "date=" + sdf.format(date.getTime());
	  // Getするドキュメントタイプ(type: 1 でメタデータのみ、 2 で提出書類一覧およびメタデータを取得)
	  String requestDocType = "type=2";
	  // URL生成
	  String getDocListUrl = baseURL + "?" + requestDate + "&" + requestDocType;
	  URL url = new URL(getDocListUrl);

	  HttpURLConnection con = (HttpURLConnection) url.openConnection();
      BufferedReader in = new BufferedReader(
    		  new InputStreamReader(con.getInputStream()));
      String inputLine;

      while ((inputLine = in.readLine()) != null) {
          System.out.println(inputLine);
      }
      in.close();

	return "Success!";

  }
}