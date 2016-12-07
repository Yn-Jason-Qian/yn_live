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
 * 搜集电脑中的CHM帮助文档文件，归纳到“帮助文档”目录
 * 
 * collect the files which end with 'chm' to the dir named as 'help file' 
 * 
 * @author yn_qian
 *
 */
public class CollectCHMFileService implements Service {
	
	/**
	 * 默认为"F:/开发包/帮助文档"
	 */
	@Value("")
	private String targetDirStr = "F:\\开发包\\帮助文档";
	
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
		
		//扫描其他目录
		for(String rootDirPath : scannedDirs) {
			System.out.println("start to scan " + rootDirPath);
			
			File rootDir = new File(rootDirPath);
			scanDir(rootDir, chmFilePaths, false);
		}
		
		//扫描目标目录
		File targetDir = new File(targetDirStr);
		scanDir(targetDir, chmFilePaths, true);
		
		System.out.println(chmFilePaths);
		System.out.println(chmFilePaths.size());
		
		copyChmFileToTargetDir(chmFilePaths);
	}
	
	/**
	 * 扫描目录
	 * 
	 * @param rootDir
	 * @param chmFilePaths 用来保存收集到的chm文件的路径
	 * @param ifNeedScanTargetDir 是否要求扫描文件存放目标目录
	 */
	private void scanDir(File dir, Map<String, String> chmFilePaths, boolean ifNeedScanTargetDir) {
		if(!dir.isDirectory()) {
			throw new IllegalArgumentException(String.format("%s isn't a directory!", dir.getAbsolutePath()));
		}
		
		//如果要求扫描目标目录，则继续扫描。如果要求不扫描目标目录，但是已经扫描到当前target目录，则直接跳过。
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
//		File dir = new File("F:/开发包");
//		File[] files = dir.listFiles(new CollectChmFileFilter());
//		for(File file : files) {
//			System.out.println(file.getName());
//		}
//		System.out.println(files.length);
		
		//测试扫描
		CollectCHMFileService service = new CollectCHMFileService();
		service.init();
		service.service(Collections.EMPTY_MAP);
	}
	
	@Test
	public void testIsDir() {
		File dir = new File("C:/Windows/Microsoft.NET/assembly/GAC_MSIL/System.Net.Primitives");
		System.out.println(dir.isDirectory());
		System.out.println(dir.getAbsolutePath());
		
		File file = new File("F:/开发包/帮助文档/ajax教程.chm");
		System.out.println(file.equals(new File(file.getAbsolutePath())));
		System.out.println(file.getAbsolutePath().startsWith(targetDirStr));
	}

}
