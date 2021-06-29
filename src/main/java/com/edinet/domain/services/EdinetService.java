package com.edinet.domain.services;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.edinet.app.common.utills.EdinetConstants;
import com.edinet.app.common.utills.FileUtill;
import com.edinet.app.controllers.EdinetController;
import com.edinet.domain.models.AssetEntity;
import com.edinet.domain.models.CompanyEntity;
import com.edinet.domain.models.RevenueEntity;
import com.edinet.domain.services.handler.EdinetAPIHandler;
import com.edinet.jacson.Result;

@Service
public class EdinetService {

	// Log4j2
	final Logger logger = LogManager.getLogger(EdinetController.class.getName());
	// EdinetAPIのURL
	//final String baseUrl = "https://disclosure.edinet-fsa.go.jp/api/v1/";

	// 一時ファイル置き場の名前
	final String temp = "temp";
	// Read Data
	byte[] b = new byte[4096];
	// 拡張子xbrl
	final String extension = ".xbrl";

	@Autowired
	private FileUtill fileUtill;
	@Autowired
	private AssetService assetService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private RevenueService revenueService;

	/**
	 * EdinetからAPIを使用してZIPファイルを取得し、DBに登録する
	 * @throws Exception
	 */
	public String getEdinetData() throws Exception {

		String reqDate = "2020-12-09"; // TODO:確認用。あとで消す

		EdinetAPIHandler edinetApiHandler = new EdinetAPIHandler();
		List<Result> docList = edinetApiHandler.getDocIdList(reqDate);

		// 書類一覧が0件の場合や正常に取得できない場合は終了
		if(docList.size() == 0) {
			return "Success. There were no documents.";
		} else if(docList.isEmpty()) {
			return "Abnormal End. Check the Edinet Status.";
		}

		// 日付ディレクトリを作成
		fileUtill.makeDir(reqDate);
		// 一時置き場を作成
		fileUtill.makeDir(temp);
		//-- 書類Zip取得処理
		edinetApiHandler.getZipFile(reqDate, docList);
		//-- Zipファイルを解凍
		fileUtill.unzipFiles(reqDate);
		// xbrlファイルからDBへ登録する
		dom();

		return "Success! documents:" + docList.size();
	}

	/**
	 *  XBRLファイルを読み込み、DBへ登録する
	 */
	private void dom() {
		AssetEntity assetEntity = new AssetEntity();
		CompanyEntity companyEntity = new CompanyEntity();
		RevenueEntity revenueEntity = new RevenueEntity();
		FileUtill fileUtill = new FileUtill();
		File[] xbrlFiles = fileUtill.getListFiles(fileUtill.getCurrentDir() + temp);
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
						Element ele = (Element)node;
						setEntity(ele, assetEntity, companyEntity, revenueEntity);
					}
				}

				// データ投入
				if(!assetEntity.getCompanyCode().isEmpty()) {
					assetService.insertAsset(assetEntity);
					companyService.insertCmpany(companyEntity);
					revenueService.insertRevenue(revenueEntity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 各種Entityに値をセットする
	 * @param element
	 * @param assetEntity
	 * @param companyEntity
	 * @param revenueEntity
	 */
	private void setEntity(Element element, AssetEntity assetEntity,
			CompanyEntity companyEntity, RevenueEntity revenueEntity) {

		//----- 共通
		// EdinetCode
		if(element.getNodeName().equals(EdinetConstants.companyCd)) {
			String companyCode = element.getTextContent();
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
		if(element.getNodeName().equals(EdinetConstants.capitalAdequacyRatio)
				&& element.getAttribute("contextRef").equals(EdinetConstants.onePriorInst)) {
			assetEntity.setCapitalAdequacyRatio(element.getTextContent());
		}
		//----- Companyテーブル
		// 会社名
		if(element.getNodeName().equals(EdinetConstants.companyName)) {
			companyEntity.setCompanyName(element.getTextContent());
		}
		//----- Revenueテーブル
		// 売上高
		if(element.getNodeName().equals(EdinetConstants.netSales)
				&& element.getAttribute("contextRef").equals(EdinetConstants.onePriorDur)) {
			revenueEntity.setNetSales(element.getTextContent());
		}
		// 営業利益
		if(element.getNodeName().equals(EdinetConstants.operatingIncome)
				&& element.getAttribute("contextRef").equals(EdinetConstants.currentYTD)) {
			revenueEntity.setOperatingRevenue(element.getTextContent());
		}
		// 経常利益
		if(element.getNodeName().equals(EdinetConstants.ordinaryIncome)
				&& element.getAttribute("contextRef").equals(EdinetConstants.onePriorDur)) {
			revenueEntity.setOrdinaryIncome(element.getTextContent());
		}
	}

}
