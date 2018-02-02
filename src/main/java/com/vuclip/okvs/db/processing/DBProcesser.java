package com.vuclip.okvs.db.processing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.vuclip.okvs.model.Constants;
import com.vuclip.okvs.model.UserSubscription;

public class DBProcesser {
	private static String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static String DB_CONNECTION = "jdbc:mysql://localhost:3306/baas";
	private static String DB_USER = "root";
	private static String DB_PASSWORD = "root";
	Connection dbConnection=null;
	
	public DBProcesser() {
		this.dbConnection=this.getDBConnection();
	}
	public void closeDBConnection(){
		if(this.dbConnection!=null){
			try {
				this.dbConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void insertUserSubscriptionRecord(List<UserSubscription> userSubscriptionList){
		PreparedStatement preparedStatement=null;
		int cnt=0;
		try{
			String insertQuery="INSERT INTO user_subscription (customer_id,provider_id,msisdn,user_id,item_id,item_type_id,billing_code,"
					+ "subscription_billing_code,current_billing_code,subscription_status_id,start_date,end_date,subscription_date,"
					+ "last_billing_request_date,last_billing_response_date,last_notification_date,next_billing_date,"
					+ "user_source,charging_source,customer_transaction_id,credits,create_date,modify_date,preferred_language,user_preferred_language,"
					+ "user_communication_language,last_successful_billing_date)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			preparedStatement=dbConnection.prepareStatement(insertQuery);
			
			Date date = new Date();
			TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
			Timestamp sqlDate=new Timestamp(date.getTime());
			for(UserSubscription userSubscription:userSubscriptionList){
				preparedStatement.setInt(1,Constants.customer_id);
				preparedStatement.setInt(2,Constants.provider_id);
				preparedStatement.setString(3,userSubscription.getMsisdn());
				preparedStatement.setString(4,userSubscription.getMsisdn());
				preparedStatement.setInt(5,Constants.item_id);
				preparedStatement.setInt(6,Constants.item_type_id);
				preparedStatement.setInt(7,userSubscription.getBillingCode());
				preparedStatement.setInt(8,userSubscription.getSubscriptionBillingCode());
				preparedStatement.setInt(9,userSubscription.getCurrentBillingCode());
				preparedStatement.setInt(10,userSubscription.getSubscriptionStatusId());
				preparedStatement.setTimestamp(11,userSubscription.getStartDate());
				preparedStatement.setTimestamp(12,userSubscription.getEndDate());
				preparedStatement.setTimestamp(13,userSubscription.getSubscriptionDate());
				preparedStatement.setTimestamp(14,userSubscription.getLastBillingRequestDate());
				preparedStatement.setTimestamp(15,userSubscription.getLastBillingResponseDate());
				preparedStatement.setTimestamp(16,userSubscription.getLastNotificationDate());
				preparedStatement.setTimestamp(17,userSubscription.getNextBillingDate());
				preparedStatement.setString(18,userSubscription.getUserSource());
				preparedStatement.setString(19,"MIGRATION");
				preparedStatement.setString(20,userSubscription.getCustomerTransactionId());
				preparedStatement.setInt(21,userSubscription.getCredits());
				preparedStatement.setTimestamp(22,userSubscription.getCreateDate());
				preparedStatement.setTimestamp(23,sqlDate);
				preparedStatement.setInt(24,userSubscription.getPreferredLanguage());
				preparedStatement.setInt(25,userSubscription.getUserPreferredLanguage());
				preparedStatement.setInt(26,userSubscription.getUserPreferredLanguage());
				preparedStatement.setTimestamp(27,userSubscription.getLastSuccessfulBillingDate());
				
				preparedStatement.addBatch();
				cnt++;
				if(cnt%500==0){
					System.out.println("Going to execute batch of size: "+cnt);
					preparedStatement.executeBatch();
					System.out.println("Executed and committed successfully..");
					preparedStatement.clearBatch();
					cnt=0;
				}
			}
		}catch(Exception exception){
			System.out.println(exception+" \n"+exception.getMessage());
			exception.printStackTrace();
		}finally{
			if(preparedStatement!=null){
				try {
					System.out.println("Going to execute batch of size: "+cnt);
					preparedStatement.executeBatch();
					System.out.println("Executed and committed successfully..");
					preparedStatement.clearBatch();
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public Connection getDBConnection() {
		Connection dbConnection = null;
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {

			dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,DB_PASSWORD);
			return dbConnection;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return dbConnection;
	}
	public void insertUserBlackListRecord(List<String> msisdnList) {
		PreparedStatement preparedStatement=null;
		try{
			String insertQuery="INSERT INTO `user_blacklist` (`msisdn`,`provider_id`,`customer_id`,`item_id`,`item_type_id`,`blacklist_type_id`,`start_date`,`end_date`,`validity`,`time_unit`,`create_date`,`modifie_date`) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
			preparedStatement=dbConnection.prepareStatement(insertQuery);
			int cnt=0;
			Date date = new Date();
			Calendar c =Calendar.getInstance();
			TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
			Timestamp sqlDate=new Timestamp(date.getTime());
			c.setTime(date);
			c.add(Calendar.YEAR,5);
			Timestamp end_date=new Timestamp(c.getTime().getTime());
			for(String msisdn:msisdnList){
				preparedStatement.setString(1,msisdn);
				preparedStatement.setInt(2,104);
				preparedStatement.setInt(3,104);
				preparedStatement.setInt(4,0);
				preparedStatement.setInt(5,0);
				preparedStatement.setInt(6,0);
				preparedStatement.setTimestamp(7,sqlDate);
				preparedStatement.setTimestamp(8,end_date);
				preparedStatement.setInt(9,60);
				preparedStatement.setString(10,"MONTH");
				preparedStatement.setTimestamp(11,sqlDate);
				preparedStatement.setTimestamp(12,sqlDate);
				
				preparedStatement.addBatch();
				cnt++;
				if(cnt%500==0){
					System.out.println("Going to execute batch of size: "+cnt);
					preparedStatement.executeBatch();
					System.out.println("Executed and committed successfully..");
					preparedStatement.clearBatch();
					cnt=0;
				}
			}
		}catch(Exception exception){
			System.out.println(exception+" \n"+exception.getMessage());
			exception.printStackTrace();
		}finally{
			if(preparedStatement!=null){
				try {
					preparedStatement.executeBatch();
					preparedStatement.clearBatch();
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
