package com.live.service.quartz;


import java.util.Collections;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.live.service.impl.DownloadFileService;

@Component
public class DownloadFileJob implements Job {
	
	private DownloadFileService service = new DownloadFileService();

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		service.service(Collections.<String, Object> emptyMap());
	}

}
