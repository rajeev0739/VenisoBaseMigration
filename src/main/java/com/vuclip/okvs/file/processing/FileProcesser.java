package com.vuclip.okvs.file.processing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.vuclip.okvs.db.processing.*;
import com.vuclip.okvs.model.BillingCodeEnum;
import com.vuclip.okvs.model.LanguageEnum;
import com.vuclip.okvs.model.SubscriptionStatusEnum;
import com.vuclip.okvs.model.UserSubscription;

public class FileProcesser {

	private static final int TIME_ZONE = 2; // venisio gives data in carrier local time but we need to keep data in  db in GMT
	private static final String VUCLIP_TIME_FORMAT="yyyy-MM-dd HH:mm:ss"; 
	private static final String VENISO_NULL_DATE = "0000-00-00 00:00:00";
	private static final String VENISO_NULL_DATE_1 = "NULL";
	
	
/*	 
 * Excel File Header Format ....( .xlsx file only get processed) 
index 0     MSISDN	
index 1		SubscriberID	
index 2		Subscription_pricepoint	
index 3		Parent_PP	
index 4		Current_PP	
index 5		Current_user_status	
index 6		Start_date	
index 7		Last_billing_Date	
index 8		Last_notification_date	
index 9 	End_date	
index 10	Next_billing_date	
index 11	Subscription_Date	
index 12	User_Source	
index 13	Ad_Network_id	
index 14	Create_Date	
index 15	User_Language
*/
	
	public void processFile(String inputPath) throws IOException, ParseException{
		FileInputStream file = new FileInputStream(new File(inputPath));
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		List<UserSubscription> userSubscriptionList=new ArrayList<UserSubscription>();
		XSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		Calendar c =Calendar.getInstance();
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		
		int noOfRecordsSkippedDueToInvalidMsisdn=0;
		int noOfRecordsSkippedDueToInvalidSubScriptionBillingCode=0;
		int noOfRecordsSkippedDueToInvalidCustomerTransactionId=0;
		int noOfRecordsSkippedDueToInvalidParentBillingCode=0;
		int noOfRecordsSkippedDueToInvalidCurrentBillingCode=0;
		int noOfRecordsSkippedDueToInvalidStatus=0;
		int noOfRecords=0;
		int noOfRecordsSkipped=0;
		int noOfRecordsProcessed=0;
		int columnindex=0;
		boolean skipRecord;
		BillingCodeEnum billingCodeEnumTemp = null;
		SubscriptionStatusEnum tempSubscriptionStatusEnum =null;
		LanguageEnum languageEnum = null;
		String tempCellValue = null;
		String tempMsisdn = null;
		
		try{
				while (rowIterator.hasNext()) 
				{
					Row row = rowIterator.next();	
					noOfRecords++;
					skipRecord=false;
					Iterator<Cell> cellIterator = row.cellIterator();
					UserSubscription userSubscription=new UserSubscription();
					columnindex=0;
					while (cellIterator.hasNext()) 
					{
						Cell cell = cellIterator.next();
						if (skipRecord==true){
							continue;
						}
						if (columnindex==0){
							// index 0     MSISDN	
							cell.setCellType(Cell.CELL_TYPE_STRING);
							tempMsisdn = cell.getStringCellValue().trim();
							if (StringUtils.isNumeric(tempMsisdn)){
								userSubscription.setMsisdn(cell.getStringCellValue().trim());
							}else{
								noOfRecordsSkippedDueToInvalidMsisdn++;
								skipRecord=true;
							}
						}
						else if(columnindex==1){
							// index 1		SubscriberID	Mapped to CustomerTransactionId in BaaS
							tempCellValue = "";
							cell.setCellType(Cell.CELL_TYPE_STRING);
							tempCellValue=cell.getStringCellValue().trim();
							if(StringUtils.isNumeric(tempCellValue)){
								userSubscription.setCustomerTransactionId(tempCellValue);
							}else{
								noOfRecordsSkippedDueToInvalidCustomerTransactionId++;
								skipRecord=true;
							}
						}
						else if(columnindex==2){
							// index 2		Subscription_pricepoint	
							tempCellValue = "";
							if(cell.getCellType()==Cell.CELL_TYPE_STRING){
								tempCellValue = cell.getStringCellValue().trim();
							}else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
								tempCellValue =  String.valueOf(cell.getNumericCellValue());
							}
							billingCodeEnumTemp = BillingCodeEnum.getBillingCodeEnum(tempCellValue);
							if (billingCodeEnumTemp!=null){
								userSubscription.setSubscriptionBillingCode(billingCodeEnumTemp.getBillingCode());
							}else{
								noOfRecordsSkippedDueToInvalidSubScriptionBillingCode++;
								skipRecord=true;
							}
						}else if(columnindex==3){
							// index 3 parent price point
							tempCellValue = "";
							if(cell.getCellType()==Cell.CELL_TYPE_STRING){
								tempCellValue = cell.getStringCellValue().trim();
							}else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
								tempCellValue =  String.valueOf(cell.getNumericCellValue());
							}
							billingCodeEnumTemp = BillingCodeEnum.getBillingCodeEnum(tempCellValue);
							if (billingCodeEnumTemp!=null){
								userSubscription.setBillingCode(billingCodeEnumTemp.getBillingCode());
							}else{
								noOfRecordsSkippedDueToInvalidParentBillingCode++;
								skipRecord=true;
							}
						}else if(columnindex==4){
							// index 3 current price point
							tempCellValue = "";
							if(cell.getCellType()==Cell.CELL_TYPE_STRING){
								tempCellValue = cell.getStringCellValue().trim();
							}else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
								tempCellValue =  String.valueOf(cell.getNumericCellValue());
							}
							billingCodeEnumTemp = BillingCodeEnum.getBillingCodeEnum(tempCellValue);
							if (billingCodeEnumTemp!=null){
								userSubscription.setCurrentBillingCode(billingCodeEnumTemp.getBillingCode());
								userSubscription.setCredits(billingCodeEnumTemp.getCredits());
							}else{
								userSubscription.setCurrentBillingCode(userSubscription.getBillingCode());
							    userSubscription.setCredits(99999);
							}
						}/*else if(columnindex==5){
							cell.setCellType(Cell.CELL_TYPE_STRING);
							languageEnum = LanguageEnum.getLanguageEnum(cell.getStringCellValue().trim());
							userSubscription.setPreferredLanguage(languageEnum.getLanguageId());
						}*/else if(columnindex==5){
							cell.setCellType(Cell.CELL_TYPE_STRING);
							tempSubscriptionStatusEnum = SubscriptionStatusEnum.getSubscriptionStatusEnum(cell.getStringCellValue().trim());
							userSubscription.setSubscriptionStatusId(tempSubscriptionStatusEnum.getSubscriptionStatusId());
							if(tempSubscriptionStatusEnum.getSubscriptionStatusId()==7 || tempSubscriptionStatusEnum.getSubscriptionStatusId()==6 ){
								// for egypt orange we have grace as well as suspend
								userSubscription.setCurrentBillingCode(BillingCodeEnum.POST_CHARGE.getBillingCode()); // need to verify
								
								if(tempSubscriptionStatusEnum.getSubscriptionStatusId()==6 ) {
									userSubscription.setCredits(99999); // needs to verify
								}else {
									userSubscription.setCredits(BillingCodeEnum.POST_CHARGE.getCredits()); 
								}
							}else if(tempSubscriptionStatusEnum.getSubscriptionStatusId()==5){
								
								userSubscription.setCurrentBillingCode(BillingCodeEnum.PRE_CHARGE.getBillingCode()); // need to verify
								userSubscription.setCredits(BillingCodeEnum.PRE_CHARGE.getCredits()); 
							}
						}else if(columnindex==6){
							if(cell.getCellType()==Cell.CELL_TYPE_STRING && !(cell.getStringCellValue().trim()).contains(VENISO_NULL_DATE) && !(cell.getStringCellValue().trim()).contains(VENISO_NULL_DATE_1)){
								String Date1=cell.getStringCellValue().trim();
								Date date=new SimpleDateFormat(VUCLIP_TIME_FORMAT).parse(Date1);
								c.setTime(date);
								//c.add(Calendar.HOUR_OF_DAY, -TIME_ZONE); //data will be in GMT from venisio and our db also is in GMT
								Timestamp sqlDate=new Timestamp(c.getTime().getTime());
								userSubscription.setStartDate(sqlDate);
							}else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)){
								Date date = cell.getDateCellValue(); 
								c.setTime(date);
								//c.add(Calendar.HOUR_OF_DAY, -TIME_ZONE); // data will be in GMT from venisio and our db also is in GMT
								Timestamp sqlDate=new Timestamp(c.getTime().getTime());
								userSubscription.setStartDate(sqlDate);
							}else{
								Date date = new Date();
								Timestamp sqlDate=new Timestamp(date.getTime());
								userSubscription.setStartDate(sqlDate);
							}
						}else if(columnindex==7){
							if(cell.getCellType()==Cell.CELL_TYPE_STRING && !(cell.getStringCellValue().trim()).contains(VENISO_NULL_DATE) && ! (cell.getStringCellValue().trim()).contains(VENISO_NULL_DATE_1)){
								String Date1=cell.getStringCellValue().trim();
								Date date=new SimpleDateFormat(VUCLIP_TIME_FORMAT).parse(Date1);
								c.setTime(date);
								//c.add(Calendar.HOUR_OF_DAY, -TIME_ZONE);
								Timestamp sqlDate=new Timestamp(c.getTime().getTime());
								userSubscription.setLastBillingRequestDate(sqlDate);
								userSubscription.setLastBillingResponseDate(sqlDate);
								userSubscription.setLastSuccessfulBillingDate(sqlDate);
							}else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)){
								Date date = cell.getDateCellValue(); 
								c.setTime(date);
								//c.add(Calendar.HOUR_OF_DAY, -TIME_ZONE);
								Timestamp sqlDate=new Timestamp(c.getTime().getTime());
								userSubscription.setLastBillingRequestDate(sqlDate);
								userSubscription.setLastBillingResponseDate(sqlDate);
								userSubscription.setLastSuccessfulBillingDate(sqlDate);
							}
						}else if(columnindex==8){
							if(cell.getCellType()==Cell.CELL_TYPE_STRING && !(cell.getStringCellValue().trim()).contains(VENISO_NULL_DATE) && !(cell.getStringCellValue().trim()).contains(VENISO_NULL_DATE_1)){
								String Date1=cell.getStringCellValue().trim();
								Date date=new SimpleDateFormat(VUCLIP_TIME_FORMAT).parse(Date1);
								c.setTime(date);
								//c.add(Calendar.HOUR_OF_DAY, -TIME_ZONE);
								Timestamp sqlDate=new Timestamp(c.getTime().getTime());
								userSubscription.setLastNotificationDate(sqlDate);
							}else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)){
								Date date = cell.getDateCellValue(); 
								c.setTime(date);
								//c.add(Calendar.HOUR_OF_DAY, -TIME_ZONE);
								Timestamp sqlDate=new Timestamp(c.getTime().getTime());
								userSubscription.setLastNotificationDate(sqlDate);
							}
						}else if(columnindex==9){
							if(cell.getCellType()==Cell.CELL_TYPE_STRING){
								String Date1=cell.getStringCellValue().trim();
								Date date=new SimpleDateFormat(VUCLIP_TIME_FORMAT).parse(Date1);
								c.setTime(date);
								//c.add(Calendar.HOUR_OF_DAY, -TIME_ZONE);
								Timestamp sqlDate=new Timestamp(c.getTime().getTime());
								userSubscription.setEndDate(sqlDate);
							}else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)){
								Date date = cell.getDateCellValue(); 
								c.setTime(date);
								//c.add(Calendar.HOUR_OF_DAY, -TIME_ZONE);
								Timestamp sqlDate=new Timestamp(c.getTime().getTime());
								userSubscription.setEndDate(sqlDate);
							}else{
								Date date = new Date();
								c.setTime(date);
								c.add(Calendar.DAY_OF_MONTH, billingCodeEnumTemp.getValidity());
								Timestamp sqlDate=new Timestamp(c.getTime().getTime());
								userSubscription.setEndDate(sqlDate);
							}
						}else if(columnindex==10){
							if(cell.getCellType()==Cell.CELL_TYPE_STRING && !(cell.getStringCellValue().trim()).contains(VENISO_NULL_DATE) &&  !(cell.getStringCellValue().trim()).contains(VENISO_NULL_DATE_1)){
								String Date1=cell.getStringCellValue().trim();
								Date date=new SimpleDateFormat(VUCLIP_TIME_FORMAT).parse(Date1);
								c.setTime(date);
								//c.add(Calendar.HOUR_OF_DAY, -TIME_ZONE);
								Timestamp sqlDate=new Timestamp(c.getTime().getTime());
								userSubscription.setNextBillingDate(sqlDate);
							}else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)){
								Date date = cell.getDateCellValue(); 
								c.setTime(date);
								//c.add(Calendar.HOUR_OF_DAY, -TIME_ZONE);
								Timestamp sqlDate=new Timestamp(c.getTime().getTime());
								userSubscription.setNextBillingDate(sqlDate);
							}
						}else if(columnindex==11){
							if(cell.getCellType()==Cell.CELL_TYPE_STRING && !(cell.getStringCellValue().trim()).contains(VENISO_NULL_DATE) && !(cell.getStringCellValue().trim()).contains(VENISO_NULL_DATE_1)){
								String Date1=cell.getStringCellValue().trim(); 
								Date date=new SimpleDateFormat(VUCLIP_TIME_FORMAT).parse(Date1);
								c.setTime(date);
								//c.add(Calendar.HOUR_OF_DAY, -TIME_ZONE);
								Timestamp sqlDate=new Timestamp(c.getTime().getTime());
								userSubscription.setSubscriptionDate(sqlDate);
							}else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)){
								Date date = cell.getDateCellValue(); 
								c.setTime(date);
								//c.add(Calendar.HOUR_OF_DAY, -TIME_ZONE);
								Timestamp sqlDate=new Timestamp(c.getTime().getTime());
								userSubscription.setSubscriptionDate(sqlDate);
							}else{
								Date date = new Date();
								Timestamp sqlDate=new Timestamp(date.getTime());
								userSubscription.setSubscriptionDate(sqlDate);
							}
						}else if(columnindex==12){
							tempCellValue = cell.getStringCellValue().trim();
							userSubscription.setUserSource(tempCellValue);
						}else if(columnindex==14){
							if(cell.getCellType()==Cell.CELL_TYPE_STRING && !(cell.getStringCellValue().trim()).contains(VENISO_NULL_DATE) && !(cell.getStringCellValue().trim()).contains(VENISO_NULL_DATE_1)){
								String Date1=cell.getStringCellValue().trim();
								Date date=new SimpleDateFormat(VUCLIP_TIME_FORMAT).parse(Date1);
								c.setTime(date);
								//c.add(Calendar.HOUR_OF_DAY, -TIME_ZONE);
								Timestamp sqlDate=new Timestamp(c.getTime().getTime());
								userSubscription.setCreateDate(sqlDate);
							}else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)){
								Date date = cell.getDateCellValue(); 
								c.setTime(date);
								//c.add(Calendar.HOUR_OF_DAY, -TIME_ZONE);
								Timestamp sqlDate=new Timestamp(c.getTime().getTime());
								userSubscription.setCreateDate(sqlDate);
							}else{
								Date date = new Date();
								Timestamp sqlDate=new Timestamp(date.getTime());
								userSubscription.setCreateDate(sqlDate);
							}
						}else if(columnindex==15){
							languageEnum = LanguageEnum.getLanguageEnum(cell.getStringCellValue().trim());
							userSubscription.setUserPreferredLanguage(languageEnum.getLanguageId());
						}
						columnindex++;
					}
					if(!skipRecord){
						noOfRecordsProcessed++;
						userSubscriptionList.add(userSubscription);
					}else{
						System.out.println("Skipped Record :: "+userSubscription.toString());
						noOfRecordsSkipped++;
					}
					if(noOfRecordsProcessed%500==0 && userSubscriptionList!=null && !userSubscriptionList.isEmpty()){
						processUserSubscriptionList(userSubscriptionList);
						System.out.println("Processed 500 records batch. Processed till now :"+ noOfRecordsProcessed);
						userSubscriptionList.clear();
					}
				}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("msidn"+ tempMsisdn+ " value:"+tempCellValue);
		}/*finally {*/
		
			file.close();
			if(userSubscriptionList.size()>0){
				processUserSubscriptionList(userSubscriptionList);
				System.out.println("Processing done.\nTotal records : "+noOfRecords
				+" \nprocessed : "+noOfRecordsProcessed+" \nnot processed : "+noOfRecordsSkipped
				+" \nnoOfRecordsSkippedDueToInvalidMsisdn : "+noOfRecordsSkippedDueToInvalidMsisdn
				+" \nnoOfRecordsSkippedDueToInvalidSubScriptionBillingCode : "+noOfRecordsSkippedDueToInvalidSubScriptionBillingCode
				+" \nnoOfRecordsSkippedDueToInvalidParentBillingCode : "+noOfRecordsSkippedDueToInvalidParentBillingCode
				+" \nnoOfRecordsSkippedDueToInvalidCurrentBillingCode : "+noOfRecordsSkippedDueToInvalidCurrentBillingCode
				+" \nnoOfRecordsSkippedDueToInvalidStatus : "+noOfRecordsSkippedDueToInvalidStatus
				+" \noOfRecordsSkippedDueToInvalidCustomerTransactionId : "+noOfRecordsSkippedDueToInvalidCustomerTransactionId);
			}
		/*}*/
		
	}

	private void processUserSubscriptionList(List<UserSubscription> userSubscriptionList) {
		DBProcesser  dbProcessing=new DBProcesser();
		dbProcessing.insertUserSubscriptionRecord(userSubscriptionList);
		dbProcessing.closeDBConnection();
	}

	public void processFile1(String path) throws IOException, ParseException{
		FileInputStream file = new FileInputStream(new File(path));
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);
		List<String> msisdnList=new ArrayList<String>();
		Iterator<Row> rowIterator = sheet.iterator();
		int noOfRecords=0;
		int noOfRecordsSkipped=0;
		int noOfRecordsProcessed=0;
		boolean skipRecord;
		while (rowIterator.hasNext()) 
		{
			String msisdn= null;
			try{
				Row row = rowIterator.next();	
				noOfRecords++;
				skipRecord=false;
				Iterator<Cell> cellIterator = row.cellIterator();
				Cell cell = cellIterator.next();
				
				if (cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
					cell.setCellType(Cell.CELL_TYPE_STRING);
				}
				msisdn=cell.getStringCellValue().trim();
			}catch(Exception E){
				noOfRecordsSkipped++;
				skipRecord=true;
				System.out.println("msisdn: "+msisdn);
			}
			if(!skipRecord && msisdn!=null){
				noOfRecordsProcessed++;
				msisdnList.add(msisdn);
			}
			if(noOfRecordsProcessed%500==0 && msisdnList!=null && !msisdnList.isEmpty()){
				processUserBlackList(msisdnList);
				System.out.println("Processed 500 records batch. Processed till now :"+ noOfRecordsProcessed);
				msisdnList.clear();
			}	
		}
		file.close();
		if(msisdnList.size()>0){
			processUserBlackList(msisdnList);
			System.out.println("Processing done.\nTotal records : "+noOfRecords+" \nprocessed : "
			+noOfRecordsProcessed+" \nnot processed : "+noOfRecordsSkipped);
		}
	}

	private void processUserBlackList(List<String> msisdnList) {
		DBProcesser  dbProcessing=new DBProcesser();
		dbProcessing.insertUserBlackListRecord(msisdnList);
		dbProcessing.closeDBConnection();
	} 

}
