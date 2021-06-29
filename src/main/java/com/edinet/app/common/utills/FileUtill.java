package com.edinet.app.common.utills;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.stereotype.Component;

@Component
public class FileUtill {

	// カレントディレクトリ
	final String currentDir = new File(".").getAbsoluteFile().getParent() + "/";
	// 拡張子xbrl
	final String extension = ".xbrl";
	// 一時ファイル置き場の名前
	final String temp = "temp";
	// Read Data
	byte[] b = new byte[4096];


	// カレントディレクトリを取得
	public String getCurrentDir() {
		return currentDir;
	}

	// ディレクトリを作成する
	public void makeDir(String dirName) {
		File dir = new File(dirName);
		dir.mkdir();
	}

	// 指定したディレクトリ配下のファイル一覧を取得する
	public File[] getListFiles(String dirName) {
		File dir = new File(dirName);
		return dir.listFiles();
	}

	/**
	 * Zipファイルを展開
	 * xbrlファイルのみを展開する
	 * @param date
	 */
	public void unzipFiles(String date) {

		int len;
		// ZIPファイル置き場のパス
		File files = new File(getCurrentDir() + date);
		File[] zipFileList = files.listFiles();
		ZipEntry zipEntry = null;

		// tempディレクトリへZIPファイルを展開
		for(File zipFile : zipFileList) {
			try(ZipInputStream zistr = new ZipInputStream(
					new FileInputStream(zipFile.getPath()));
					){
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
						// try-with-resourceに入れたほうがいい？
						bostr.close();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
