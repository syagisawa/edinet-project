package com.edinet.domain.services.utills;

import java.io.File;

import org.springframework.stereotype.Component;

@Component
public class FileUtill {

	// カレントディレクトリ
	final String currentDir = new File(".").getAbsoluteFile().getParent() + "/";

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
}
