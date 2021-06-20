package com.edinet.domain.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.edinet.app.controllers.EdinetController;
import com.edinet.domain.models.AssetEntity;
import com.edinet.domain.models.CompanyEntity;
import com.edinet.domain.models.RevenueEntity;
import com.edinet.jacson.DocumentInfoList;
import com.edinet.jacson.Result;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EdinetService  extends HttpConnectionBase{

	// Log4j2
	final Logger logger = LogManager.getLogger(EdinetController.class.getName());
	// EdinetAPIのURL
	//final String baseUrl = "https://disclosure.edinet-fsa.go.jp/api/v1/";
	// 府令コード
	final String ordinanceCode = "010";
	// 有価証券コード
	final String securitiesReport = "030000";
	// 四半期報告書コード
	final String quarterlyReport = "043000";
	// 一時ファイル置き場の名前
	final String temp = "temp";
	// カレントディレクトリ
	final String currentDir = new File(".").getAbsoluteFile().getParent() + "/";
	// Read Data
	byte[] b = new byte[4096];
	// 拡張子xbrl
	final String extension = ".xbrl";

	@Autowired
	private AssetEntity assetEntity;
	@Autowired
	private CompanyEntity companyEntity;
	@Autowired
	private RevenueEntity revenueEntity;
	@Autowired
	private AssetService assetService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private RevenueService revenueService;



	/**
	 * EdinetからAPIを使用してZIPファイルを取得し、DBに登録する
	 */
	public String getEdinetData() {

		String reqDate = "2020-12-09"; // TODO:確認用。あとで消す

		// 書類ID一覧を取得
		List<Result> docList = getDocIdList(reqDate);

		// 書類一覧が0件の場合や正常に取得できない場合は終了
		if(docList.size() == 0) {
			return "Success. There were no documents.";
		} else if(docList.isEmpty()) {
			return "Abnormal End. Check the Edinet Status.";
		}


		// 日付ディレクトリを作成
		File dir = new File(currentDir + reqDate);
		//System.out.println("getPath:"+dir.getPath());
		//System.out.println("getAbsolutePath:"+dir.getAbsolutePath());
		dir.mkdir();
		File tempDir = new File(currentDir + temp);
		tempDir.mkdir();

		//-- 書類Zip取得処理
		getZipFile(reqDate, docList);

		//-- Zipファイルを解凍
		unzipFiles(reqDate);

		dom();

		return "Success! documents:" + docList.size();
	}

	/**
	 * Edinetから書類一覧を取得する
	 * @param date
	 * @return
	 */
	public List<Result> getDocIdList(String date) {

		DocumentInfoList docInfoList = null;
		List<Result> docIdList = null;
		HttpURLConnection con = null;

		try {
			con = getConnection(date, "documents.json" + "?" + "date=" + date + "&" + "type=2");

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
		} finally {
			con.disconnect();
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

	/**
	 * EdinetAPIを使用して、ZIPファイルをダウンロードする
	 * @param date
	 * @param list
	 */
	public void getZipFile(String date ,List<Result> list) {

		HttpURLConnection con = null;
		String zipName = null;
		String docId = null;
		int readByte = 0;

		try {
			// 取得した書類IDからZIPファイルをダウンロードする
			for(Result result : list) {
				// 書類ID
				docId = result.getDocID();

				// 書類データ取得URL
				con = getConnection(date, "documents" + "/" + docId + "?type=1");

				// 日付ディレクトリ配下にzipファイルを格納
				zipName = currentDir + date + "/" + docId + ".zip";
				//System.out.println("zipName:" + zipName);
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
		} finally {
			con.disconnect();
		}
	}

	/**
	 * Zipファイルを展開
	 * xbrlファイルのみを展開する
	 * @param date
	 */
	public void unzipFiles(String date) {
		int len;
		// ZIPファイル置き場のパス
		//System.out.println("currentDir:" + currentDir);
		File files = new File(currentDir + date);
		File[] zipFileList = files.listFiles();
		//System.out.println("files:" + files);
		//System.out.println("zipFileList:" + zipFileList);

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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// DOMでXBRLファイルを読み込む
	public void dom() {
		File[] xbrlFiles = new File(currentDir + temp).listFiles();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

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

}
