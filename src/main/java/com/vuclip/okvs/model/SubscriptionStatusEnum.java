package com.vuclip.okvs.model;

public enum SubscriptionStatusEnum {
	ACTIVE("ACTIVE",4),
	GRACE("GRACE",6),
	SUSPEND("SUSPEND",7),
	PARKING("PARKING",5);

	
	private final String statusFromCarrier;
	private final int subscriptionStatusId;
	
	SubscriptionStatusEnum (String statusFromCarrier,int subscriptionStatusId){
		this.statusFromCarrier=statusFromCarrier;
		this.subscriptionStatusId=subscriptionStatusId;
	}

	public String getStatusFromCarrier() {
		return statusFromCarrier;
	}

	public int getSubscriptionStatusId() {
		return subscriptionStatusId;
	}
	public static SubscriptionStatusEnum getSubscriptionStatusEnum(String planId){
		for (SubscriptionStatusEnum subscriptionStatusEnum: values()){
			if (subscriptionStatusEnum.getStatusFromCarrier().equalsIgnoreCase(planId)){
				return subscriptionStatusEnum;
			}
		}
		return null;
	}
}
