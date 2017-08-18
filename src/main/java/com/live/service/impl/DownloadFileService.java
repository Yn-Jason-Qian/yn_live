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

	private String downloadDir = "C:\\Users\\Administrator\\Downloads";
	
	
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
			System.out.printf("ת���ļ�Ŀ¼����ɾ���ļ� --- [%1$s] --- %2$s! %n", file.getName(), result?"�ɹ�":"ʧ��");
		}
	}
	
	@Override
	public void init() {
		downloadDir = "D:/�û�Ŀ¼/����";
		fileInfoTargetDirMapping = new HashMap<String, String>();
		fileInfoTargetDirMapping.put(".torrent", "G:/seed-��Ӱ�����ļ�");
		fileInfoTargetDirMapping.put(".exe", "D:/");
		fileInfoTargetDirMapping.put(".pdf", "F:/�ĵ�������");
		fileInfoTargetDirMapping.put(".doc", "F:/�ĵ�������");
		fileInfoTargetDirMapping.put(".pdf:Elem", "F:/EFEnglish");
		fileInfoTargetDirMapping.put(".pdf:Int", "F:/EFEnglish");
		fileInfoTargetDirMapping.put(".pdf:Upp", "F:/EFEnglish");
		fileInfoTargetDirMapping.put(".doc:Elem", "F:/EFEnglish");
		fileInfoTargetDirMapping.put(".doc:Int", "F:/EFEnglish");
		fileInfoTargetDirMapping.put(".doc:Upp", "F:/EFEnglish");
		fileInfoTargetDirMapping.put(".rar:Game.of.Thrones", "G:/Ȩ������Ϸ");
	}
	
	public static void main(String[] agrs) {
		Service service = new DownloadFileService();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("downloadDir", "C:\\Users\\Administrator\\Downloads");
		service.init();
		service.service(params);
	}

}
