package com.live.service.impl;

import java.io.File;
import java.io.FilenameFilter;

public class DownloadFileNameFilter implements FilenameFilter {

	@Override
	public boolean accept(File dir, String name) {
		if(name.contains(".")) {
			return true;
		}
		return false;
	}

}
