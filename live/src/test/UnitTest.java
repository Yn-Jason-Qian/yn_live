package test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

import com.live.utils.FileUtils;

public class UnitTest {

	@Test
	public void testSystemProperties() {
		Properties props = System.getProperties();
		Set<Entry<Object, Object>> entrySet = props.entrySet();
		for(Entry<Object, Object> entry : entrySet) {
			System.out.printf("property name : %1$-30s; property value : %2$s%n", 
					new Object[]{entry.getKey(), entry.getValue()});
		}
	}

	@Test
	public void testFilePath() {
		File file = new File("config/quartz.xml");
		System.out.println(file.getAbsolutePath());
		System.out.println(file.getName());
	}
	
	@Test
	public void testCopyFileByNio() {
		FileUtils.copyFile("log_network.txt", "d:/", "f:/", FileUtils.MOVE_BY_NIO);
	}
}
