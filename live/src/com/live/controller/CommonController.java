package com.live.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Controller
@RequestMapping("/")
public class CommonController {
	
	@RequestMapping("/enter")
	public String index(HttpServletRequest request) {
		System.out.println(WebApplicationContextUtils.
				getWebApplicationContext(request.getServletContext()).getBean("download"));
		return "/index";
		
	}
	
	@RequestMapping("/download")
	public String download(HttpServletResponse response) {
		String file = "/home/qianyang/下载/体检机构和套餐内容.xlsx";
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;fileName="
					+ "体检机构和套餐内容.xlsx");
			FileInputStream in = new FileInputStream(file);
			OutputStream out = response.getOutputStream();
			byte[] b = new byte[2048];
			int length;
			while ((length = in.read(b)) > 0) {
				out.write(b, 0, length);
			}

			 // 这里主要关闭。
			out.close();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
