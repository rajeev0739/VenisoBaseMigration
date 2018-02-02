package com.vuclip.okvs.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vuclip.okvs.file.processing.FileProcesser;

@Controller
@RequestMapping("/BaseMigration")
public class BaseMigration {
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String recieve(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String path="C:\\Users\\IT000441\\Desktop\\Zain";
		try{
			new FileProcesser().processFile(path);
		}catch(Exception exception){
			System.out.println(exception);
			exception.printStackTrace();
		}	
	return "OK";
	}
	
	public static void main(String args[]){
		String path="//home//rajeev//Downloads//MobinilMigrationDataDump_02022018.xlsx";
		try{
			System.out.println("starting...");
			Date date =new Date();
			double  d1 = date.getTime();
			new FileProcesser().processFile(path);
			Date date1 =new Date();
			double d2 = date1.getTime();
			System.out.println("Total time taken: "+(d2-d1)/1000 +"sec");
		}catch(Exception exception){
			System.out.println(exception);
			exception.printStackTrace();
		}	
	}
	
}
