package com.vuclip.okvs.model;

import java.sql.Timestamp;

public class UserSubscription {
	private String msisdn;
	private int subscriptionBillingCode;
	private int billingCode;
	private int currentBillingCode;
	private int preferredLanguage;
	private int userPreferredLanguage;
	private int subscriptionStatusId;
	private Timestamp startDate;
	private Timestamp lastBillingRequestDate;
	private Timestamp lastBillingResponseDate;
	private Timestamp lastSuccessfulBillingDate;
	private Timestamp lastNotificationDate;
	private Timestamp nextBillingDate;
	private Timestamp subscriptionDate;
	private Timestamp createDate;
	private Timestamp endDate;
	private String userSource;
	private int credits;
	private String customerTransactionId;
	


	public String getUserSource() {
		return userSource;
	}

	public void setUserSource(String userSource) {
		this.userSource = userSource;
	}

	public Timestamp getNextBillingDate() {
		return nextBillingDate;
	}

	public void setNextBillingDate(Timestamp nextBillingDate) {
		this.nextBillingDate = nextBillingDate;
	}

	public Timestamp getLastNotificationDate() {
		return lastNotificationDate;
	}

	public void setLastNotificationDate(Timestamp lastNotificationDate) {
		this.lastNotificationDate = lastNotificationDate;
	}

	public Timestamp getLastBillingRequestDate() {
		return lastBillingRequestDate;
	}

	public void setLastBillingRequestDate(Timestamp lastBillingRequestDate) {
		this.lastBillingRequestDate = lastBillingRequestDate;
	}

	public Timestamp getLastBillingResponseDate() {
		return lastBillingResponseDate;
	}

	public void setLastBillingResponseDate(Timestamp lastBillingResponseDate) {
		this.lastBillingResponseDate = lastBillingResponseDate;
	}

	public Timestamp getLastSuccessfulBillingDate() {
		return lastSuccessfulBillingDate;
	}

	public void setLastSuccessfulBillingDate(Timestamp lastSuccessfulBillingDate) {
		this.lastSuccessfulBillingDate = lastSuccessfulBillingDate;
	}

	public int getPreferredLanguage() {
		return preferredLanguage;
	}

	public void setPreferredLanguage(int preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	public int getUserPreferredLanguage() {
		return userPreferredLanguage;
	}

	public void setUserPreferredLanguage(int userPreferredLanguage) {
		this.userPreferredLanguage = userPreferredLanguage;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public int getCurrentBillingCode() {
		return currentBillingCode;
	}

	public void setCurrentBillingCode(int currentBillingCode) {
		this.currentBillingCode = currentBillingCode;
	}

	public Timestamp getSubscriptionDate() {
		return subscriptionDate;
	}

	public void setSubscriptionDate(Timestamp subscriptionDate) {
		this.subscriptionDate = subscriptionDate;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public int getBillingCode() {
		return billingCode;
	}

	public void setBillingCode(int billingCode) {
		this.billingCode = billingCode;
	}

	public int getSubscriptionBillingCode() {
		return subscriptionBillingCode;
	}

	public void setSubscriptionBillingCode(int subscriptionBillingCode) {
		this.subscriptionBillingCode = subscriptionBillingCode;
	}

	public UserSubscription() {
	}
	
	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public int getSubscriptionStatusId() {
		return subscriptionStatusId;
	}

	public void setSubscriptionStatusId(int subscriptionStatusId) {
		this.subscriptionStatusId = subscriptionStatusId;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}



	public String getCustomerTransactionId() {
		return customerTransactionId;
	}

	public void setCustomerTransactionId(String customerTransactionId) {
		this.customerTransactionId = customerTransactionId;
	}

	@Override
	public String toString() {
		return "UserSubscription [msisdn=" + msisdn + ", subscriptionBillingCode=" + subscriptionBillingCode
				+ ", billingCode=" + billingCode + ", currentBillingCode=" + currentBillingCode + ", preferredLanguage="
				+ preferredLanguage + ", userPreferredLanguage=" + userPreferredLanguage + ", subscriptionStatusId="
				+ subscriptionStatusId + ", startDate=" + startDate + ", lastBillingRequestDate="
				+ lastBillingRequestDate + ", lastBillingResponseDate=" + lastBillingResponseDate
				+ ", lastSuccessfulBillingDate=" + lastSuccessfulBillingDate + ", lastNotificationDate="
				+ lastNotificationDate + ", nextBillingDate=" + nextBillingDate + ", subscriptionDate="
				+ subscriptionDate + ", createDate=" + createDate + ", endDate=" + endDate + ", userSource="
				+ userSource + ", credits=" + credits + ", customerTransactionId=" + customerTransactionId + "]";
	}

	


}
