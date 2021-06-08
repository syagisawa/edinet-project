package com.edinet;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.edinet.jacson.DocumentInfoList;
import com.edinet.jacson.Result;
import com.edinet.model.AssetEntity;
import com.edinet.model.CompanyEntity;
import com.edinet.model.RevenueEntity;
import com.edinet.service.AssetService;
import com.edinet.service.CompanyService;
import com.edinet.service.RevenueService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@SpringBootApplication
@Controller // This means that this class is a Controller
@RequestMapping(path="/edinet")
public class MainController extends SpringBootServletInitializer {

	// EdinetAPIのURL
	final String baseUrl = "https://disclosure.edinet-fsa.go.jp/api/v1/";
	// 府令コード
	final String ordinanceCode = "010";
	// 有価証券コード
	final String securitiesReport = "030000";
	// 四半期報告書コード
	final String quarterlyReport = "043000";
	// Read Data
	byte[] b = new byte[4096];
	// 拡張子xbrl
	final String extension = ".xbrl";
	// 一時ファイル置き場の名前
	final String temp = "temp";
	// カレントディレクトリ
	final String currentDir = new File(".").getAbsoluteFile().getParent() + temp + "/";
	@Autowired
	private AssetService assetService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private RevenueService revenueService;

	@GetMapping("/index")
	public String index(Model model){
		List<CompanyEntity> companyList = companyService.getCompanyList();
		model.addAttribute("companyList", companyList);
		model.addAttribute("assetEntity", new AssetEntity());
		return "index";
	}
	@GetMapping("/news")
	public String news(Model model){
		return "news";
	}

	@GetMapping(path="/getZipFile")
	public @ResponseBody String getEdinetData() throws Exception {

		logger.info("-----starting getZipFile-----");
		logger.debug("-----DEBUG starting getZipFile-----");
		// 今日の日付を取得
		Calendar date = Calendar.getInstance();
		// 日付の形式(yyyy-MM-dd形式)
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// リクエストする日付をフォーマット
//		String reqDate = sdf.format(date.getTime());
		String reqDate = "2020-12-09"; // TODO:確認用。あとで消す

		// 書類一覧取得
		List<Result> docList = getDocIdList(reqDate);

		// 書類一覧が0件の場合や正常に取得できない場合は終了
		if(docList.size() == 0) {
			return "Success. There were no documents.";
		} else if(docList.isEmpty()) {
			return "Abnormal End. Check the Edinet Status.";
		}

		// 日付ディレクトリを作成
		File dir = new File(currentDir + reqDate);
		System.out.println("getPath:"+dir.getPath());
		System.out.println("getAbsolutePath:"+dir.getAbsolutePath());
		dir.mkdir();

		//-- 書類Zip取得処理
		getZipFile(reqDate, docList);

		//-- Zipファイルを解凍
		unzipFiles(reqDate);

		dom();
		//sax();
		//registerData();
		System.out.println(assetService.assetList().size());

		return "Success! documents:" + docList.size();
	}

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
			url = generateUrl(baseUrl + "documents.json" + "?" + "date=" + date + "&" + "type=2");

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

	/**
	 * EdinetAPIを使用して、ZIPファイルをダウンロードする
	 * @param date
	 * @param list
	 */
	public void getZipFile(String date ,List<Result> list) {

		URL url;
		String zipName = null;
		String docId = null;
		int readByte = 0;

		try {
			// 取得した書類IDからZIPファイルをダウンロードする
			for(Result result : list) {
				// 書類ID
				docId = result.getDocID();

				// 書類データ取得URL
				url = generateUrl(baseUrl + "documents" + "/" + docId + "?type=1");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				// 日付ディレクトリ配下にzipファイルを格納
				zipName = currentDir + date + "/" + docId + ".zip";
				System.out.println("zipName:" + zipName);

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

	/**
	 * Zipファイルを展開
	 * xbrlファイルのみを展開する
	 * @param date
	 */
	public void unzipFiles(String date) {
		int len;
		// ZIPファイル置き場のパス
		System.out.println("currentDir:" + currentDir);
		System.out.println("date:" + date);
		File files = new File(currentDir + "/" + date);
		File[] zipFileList = files.listFiles();

		// tmepディレクトリを作成
		File tempDir = new File(currentDir + temp);
		tempDir.mkdir();

		try {
			// tempディレクトリへZIPファイルを展開
			for(File zipFile : zipFileList) {
				// 解凍するZIPファイルのインプットストリーム
				ZipInputStream zistr = new ZipInputStream(new FileInputStream(zipFile.getPath()));
				// zipファイルのエントリの準備
				ZipEntry zipEntry = null;
				// エントリ単位に処理を行う
				while ((zipEntry = zistr.getNextEntry()) != null) {
					// 解凍先のファイル
					File uncompressFile = new File(zipEntry.getName());
					if (zipEntry.isDirectory()) {
						// ディレクトリの場合ディレクトリを作成
						uncompressFile.mkdirs();
					} else {
						// XBRLファイルを出力
						if(uncompressFile.getPath().endsWith(extension)) {
						// 解凍先ファイル用の出力ストリーム
						BufferedOutputStream bostr = new BufferedOutputStream(
								new FileOutputStream( temp + "/" + uncompressFile.getName()));
						// 解凍先ファイル用の出力ストリームへ書き込みをする
						while ((len = zistr.read(b)) != -1) {
							bostr.write(b, 0, len);
						}
						// 解凍先ファイル用の出力ストリームを閉じる
						bostr.close();
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// DOMでXBRLファイルを読み込む
	public void dom() {
		File[] xbrlFiles = new File(currentDir + "/" + temp).listFiles();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		AssetEntity assetEntity = new AssetEntity();
		CompanyEntity companyEntity = new CompanyEntity();
		RevenueEntity revenueEntity = new RevenueEntity();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			for(File xbrl : xbrlFiles) {
				//System.out.println(xbrl.getAbsolutePath());
				Document doc = builder.parse(xbrl);
				Element element = doc.getDocumentElement();
				NodeList nodeList = element.getChildNodes();
				for(int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					if(node.getNodeType() == Node.ELEMENT_NODE) {
						Element name = (Element)node;

						// ここの処理は別に書き出したい
						//----- 共通
						// EdinetCode
						if(name.getNodeName().equals("jpdei_cor:EDINETCodeDEI")) {
							String companyCode = name.getTextContent();
							// 共通化したい
							assetEntity.setCompanyCode(companyCode);
							companyEntity.setCompanyCode(companyCode);
							revenueEntity.setCompanyCode(companyCode);
						}
						// 期の開始日
						assetEntity.setStartPeriodDate("2020-08-01");
						companyEntity.setStartPeriodDate("2020-08-01");
						revenueEntity.setStartPeriodDate("2020-08-01");

						//----- Assetテーブル
						// 自己資本比率
						if(name.getNodeName().equals("jpcrp_cor:EquityToAssetRatioSummaryOfBusinessResults")
								&& name.getAttribute("contextRef").equals("Prior1YearInstant_NonConsolidatedMember")) {
							assetEntity.setCapitalAdequacyRatio(name.getTextContent());
						}
						//----- Companyテーブル
						// 会社名
						if(name.getNodeName().equals("jpdei_cor:FilerNameInJapaneseDEI")) {
							companyEntity.setCompanyName(name.getTextContent());
						}
						//----- Revenueテーブル
						// 売上高
						if(name.getNodeName().equals("jpcrp_cor:NetSalesSummaryOfBusinessResults")
								&& name.getAttribute("contextRef").equals("Prior1YearDuration_NonConsolidatedMember")) {
							revenueEntity.setNetSales(name.getTextContent());
						}
						// 営業利益
						if(name.getNodeName().equals("jppfs_cor:OperatingIncome")
								&& name.getAttribute("contextRef").equals("CurrentYTDDuration_NonConsolidatedMember")) {
							revenueEntity.setOperatingRevenue(name.getTextContent());
						}
						// 経常利益
						if(name.getNodeName().equals("jpcrp_cor:OrdinaryIncomeLossSummaryOfBusinessResults")
								&& name.getAttribute("contextRef").equals("Prior1YearDuration_NonConsolidatedMember")) {
							revenueEntity.setOrdinaryIncome(name.getTextContent());
						}
					}
				}

				// データ投入
				if(!assetEntity.getCompanyCode().isEmpty()) {
					assetService.insertAsset(assetEntity);
					companyService.insertCmpany(companyEntity);
					revenueService.insertRevenue(revenueEntity);
				}
			}
		} catch (IOException | ParserConfigurationException | SAXException e) {
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


	@GetMapping("/result")
	public String result(@ModelAttribute AssetEntity entity, Model model){
		AssetEntity asset = assetService.getAssetByCompanyCode(entity.getCompanyCode());
		CompanyEntity company = companyService.getCompanyDataByCompanyCode(entity.getCompanyCode());
		RevenueEntity revenue = revenueService.getRevenueByCompanyCode(entity.getCompanyCode());
		model.addAttribute("asset", asset);
		model.addAttribute("companyCode", company.getCompanyCode());
		model.addAttribute("companyName", company.getCompanyName());
		model.addAttribute("revenue", revenue);
		return "result";
	}

}