package com.live.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.live.exception.file.FileOperateErrorException;

public abstract class FileUtils {
	
	public static int bufferSize = 4096;
	
	public static int nioBufferSize = 1024;
	
	public final static String MOVE_BY_STREAM = "stream";
	public final static String MOVE_BY_NIO = "nio";

	public static void copyFile(String filename, String originalDir, String targetDir, String moveBy) {
		String originalFullFilePath = originalDir + Constants.FILE_SPERATOR + filename;
		
		String targetFullFilePath = targetDir = Constants.FILE_SPERATOR + filename;
		
		if(MOVE_BY_STREAM.equals(moveBy)) 
			copyFileByStream(originalFullFilePath, targetFullFilePath);
		else 
			copyFileByNio(originalFullFilePath, targetFullFilePath);
	}
	
	public static void copyFile(File file, String targetDir, String moveBy) {
		String originalFullFilePath = file.getAbsolutePath();
		
		String filename = file.getName();
		String targetFullFilePath = targetDir + Constants.FILE_SPERATOR + filename;
		
		if(MOVE_BY_STREAM.equals(moveBy)) 
			copyFileByStream(originalFullFilePath, targetFullFilePath);
		else 
			copyFileByNio(originalFullFilePath, targetFullFilePath);
	}
	
	/**
	 * 面向流的方式复制文件
	 * 
	 * @param originalFullFilePath
	 * @param targetFullFilePath
	 */
	public static void copyFileByStream(String originalFullFilePath, String targetFullFilePath) {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(originalFullFilePath);
			byte[] buffer = new byte[bufferSize];
			int len = 0;
			
			out = new FileOutputStream(targetFullFilePath);
			while((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("File path error!", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileOperateErrorException(e);
		} finally {
			try {
				if(in != null) {
					in.close();
				}
				if(out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new FileOperateErrorException("close stream error!", e);
			}
		}
	}
	
	
	/**
	 * 可用nio的bytebuffer和channel实现
	 * 
	 * @param originalFullFilePath
	 * @param targetFullFilePath
	 */
	public static void copyFileByNio(String originalFullFilePath, String targetFullFilePath) {
		RandomAccessFile originalFile = null;
		RandomAccessFile targetFile = null;
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {
			originalFile = new RandomAccessFile(originalFullFilePath, "rw");
			inChannel = originalFile.getChannel();
			
			targetFile = new RandomAccessFile(targetFullFilePath, "rw");
			outChannel = targetFile.getChannel();
			
			ByteBuffer buffer = ByteBuffer.allocate(nioBufferSize);
			buffer.clear();
			
			@SuppressWarnings("unused")
			int len = 0;
			while((len = inChannel.read(buffer)) != -1) {
				buffer.flip();
				
				while(buffer.hasRemaining()) {
					outChannel.write(buffer);
				}
				buffer.clear();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("File path error!", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileOperateErrorException(e);
		} finally {
			try {
				if(originalFile != null) {
					originalFile.close();
					originalFile = null;
				}
				if(targetFile != null) {
					targetFile.close();
					targetFile = null;
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
