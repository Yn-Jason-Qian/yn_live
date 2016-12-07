package com.live.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.live.service.Service;
import com.live.utils.FileUtils;

@Component
public class DownloadFileService implements Service{

	/** 下载文件所在文件目录 */
	private String downloadDir = "C:\\Users\\Administrator\\Downloads";
	
	
	/**
	 * 文件信息与要保存到的目标目录路径之间的映射
	 * 
	 * key为文件信息，以"suffix:keyword"形式
	 * value为目标目录的路径
	 */
	private Map<String, String> fileInfoTargetDirMapping;
	
	@Override
	public void service(Map<String, Object> ServiceParams) {
		System.out.println("switch file dir service start!");
		File dir = new File(downloadDir);
		if(dir.isDirectory()) {
			File[] files = dir.listFiles(new DownloadFileNameFilter());
			for(File file : files) {
				switchFile(file);
			}
		}
		System.out.println("service end!");
	}
	
	/**
	 * 根据文件名选择目标目录
	 * 
	 * @param filename
	 * @return
	 */
	private String chooseTargetDir(String filename) {
		String defaultTargetDir = null;
		for(Entry<String, String> mapping : fileInfoTargetDirMapping.entrySet()) {
			String[] fileInfo = mapping.getKey().split(":");
			String fileSuffix = fileInfo[0];
			
			if(fileInfo.length > 1) {
				String filenameKeyword = fileInfo[1];
				if(filename.endsWith(fileSuffix) && filename.contains(filenameKeyword)) {
					return mapping.getValue();
				}
			} else {
				if(filename.endsWith(fileSuffix)) {
					defaultTargetDir = mapping.getValue();
				}
			}
		}
		return defaultTargetDir;
	}
	
	private void switchFile(File file) {
		String filename = file.getName();
		
		String targetDir = chooseTargetDir(filename);
		
		if(targetDir != null) {
			FileUtils.copyFile(file, targetDir, FileUtils.MOVE_BY_STREAM);
			boolean result = file.delete();
			System.out.printf("转换文件目录，并删除文件 --- [%1$s] --- %2$s! %n", file.getName(), result?"成功":"失败");
		}
	}
	
	@Override
	public void init() {
		downloadDir = "D:/用户目录/下载";
		fileInfoTargetDirMapping = new HashMap<String, String>();
		fileInfoTargetDirMapping.put(".torrent", "G:/seed-电影种子文件");
		fileInfoTargetDirMapping.put(".exe", "D:/");
		fileInfoTargetDirMapping.put(".pdf", "F:/文档与资料");
		fileInfoTargetDirMapping.put(".doc", "F:/文档与资料");
		fileInfoTargetDirMapping.put(".pdf:Elem", "F:/EFEnglish");
		fileInfoTargetDirMapping.put(".pdf:Int", "F:/EFEnglish");
		fileInfoTargetDirMapping.put(".pdf:Upp", "F:/EFEnglish");
		fileInfoTargetDirMapping.put(".doc:Elem", "F:/EFEnglish");
		fileInfoTargetDirMapping.put(".doc:Int", "F:/EFEnglish");
		fileInfoTargetDirMapping.put(".doc:Upp", "F:/EFEnglish");
		fileInfoTargetDirMapping.put(".rar:Game.of.Thrones", "G:/权力的游戏");
	}
	
	public static void main(String[] agrs) {
		Service service = new DownloadFileService();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("downloadDir", "C:\\Users\\Administrator\\Downloads");
		service.init();
		service.service(params);
	}

}
