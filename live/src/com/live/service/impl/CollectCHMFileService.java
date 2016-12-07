package com.live.service.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import com.live.service.Service;
import com.live.utils.FileUtils;

/**
 * �Ѽ������е�CHM�����ĵ��ļ������ɵ��������ĵ���Ŀ¼
 * 
 * collect the files which end with 'chm' to the dir named as 'help file' 
 * 
 * @author yn_qian
 *
 */
public class CollectCHMFileService implements Service {
	
	/**
	 * Ĭ��Ϊ"F:/������/�����ĵ�"
	 */
	@Value("")
	private String targetDirStr = "F:\\������\\�����ĵ�";
	
	private List<String> scannedDirs = new ArrayList<String>();

	@Override
	public void init() {
//		scannedDirs.add("C:/");
//		scannedDirs.add("D:/");
//		scannedDirs.add("E:/");
		scannedDirs.add("F:/");
//		scannedDirs.add("G:/");
	}

	@Override
	public void service(Map<String, Object> serviceParams) {
		Map<String, String> chmFilePaths = new HashMap<String, String>();
		
		//ɨ������Ŀ¼
		for(String rootDirPath : scannedDirs) {
			System.out.println("start to scan " + rootDirPath);
			
			File rootDir = new File(rootDirPath);
			scanDir(rootDir, chmFilePaths, false);
		}
		
		//ɨ��Ŀ��Ŀ¼
		File targetDir = new File(targetDirStr);
		scanDir(targetDir, chmFilePaths, true);
		
		System.out.println(chmFilePaths);
		System.out.println(chmFilePaths.size());
		
		copyChmFileToTargetDir(chmFilePaths);
	}
	
	/**
	 * ɨ��Ŀ¼
	 * 
	 * @param rootDir
	 * @param chmFilePaths ���������ռ�����chm�ļ���·��
	 * @param ifNeedScanTargetDir �Ƿ�Ҫ��ɨ���ļ����Ŀ��Ŀ¼
	 */
	private void scanDir(File dir, Map<String, String> chmFilePaths, boolean ifNeedScanTargetDir) {
		if(!dir.isDirectory()) {
			throw new IllegalArgumentException(String.format("%s isn't a directory!", dir.getAbsolutePath()));
		}
		
		//���Ҫ��ɨ��Ŀ��Ŀ¼�������ɨ�衣���Ҫ��ɨ��Ŀ��Ŀ¼�������Ѿ�ɨ�赽��ǰtargetĿ¼����ֱ��������
		if(!ifNeedScanTargetDir && dir.getAbsolutePath().startsWith(targetDirStr)) {
			return;
		}
		
		File[] files = dir.listFiles(new CollectChmFileFilter());
		if(files == null) {
			return;
		}
		for(File file : files) {
			if(file.isDirectory()) {
				scanDir(file, chmFilePaths, ifNeedScanTargetDir);
				continue;
			}
			
			chmFilePaths.put(file.getName(), file.getAbsolutePath());
		}
		
	}
	/**
	 * 
	 * 
	 * @param chmFilePaths
	 */
	private void copyChmFileToTargetDir(Map<String, String> chmFilePaths) {
		for(String chmFilePath : chmFilePaths.values()) {
			if(chmFilePath.startsWith(targetDirStr)) {
				continue;
			}
			
			FileUtils.copyFile(new File(chmFilePath), targetDirStr, FileUtils.MOVE_BY_STREAM);
			System.out.printf("copy file[%s] end%n", chmFilePath);
		}
	}
	
	private class CollectChmFileFilter implements FileFilter{

		@Override
		public boolean accept(File pathname) {
			if(pathname.isDirectory()) {
				return true;
			}
			
			if(pathname.getName().toLowerCase().endsWith(".chm")) {
				return true;
			}
			return false;
		}

	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
//		File dir = new File("F:/������");
//		File[] files = dir.listFiles(new CollectChmFileFilter());
//		for(File file : files) {
//			System.out.println(file.getName());
//		}
//		System.out.println(files.length);
		
		//����ɨ��
		CollectCHMFileService service = new CollectCHMFileService();
		service.init();
		service.service(Collections.EMPTY_MAP);
	}
	
	@Test
	public void testIsDir() {
		File dir = new File("C:/Windows/Microsoft.NET/assembly/GAC_MSIL/System.Net.Primitives");
		System.out.println(dir.isDirectory());
		System.out.println(dir.getAbsolutePath());
		
		File file = new File("F:/������/�����ĵ�/ajax�̳�.chm");
		System.out.println(file.equals(new File(file.getAbsolutePath())));
		System.out.println(file.getAbsolutePath().startsWith(targetDirStr));
	}

}
