package com.vuclip.okvs.model;

public enum BillingCodeEnum {
	PRE_CHARGE("0.0",1907100001,0,30),
	POST_CHARGE("0.0",1907100000,0,30),
	FREE_TRIAL("0",1907120000,99999,1), // need to confirm since two free trial 
	BILLING_CODE_1("0.75",1907175000,99999,1),// need to change billing-code for this 1907175000
	BILLING_CODE_2("2.00",1907120001,99999,1), //1907120001
	BILLING_CODE_3("45.00",1907145001,99999,30); //1907145001
	
	//BILLING_CODE_1("0.5",1907150000,99999,1),
	// BILLING_CODE_2("45.00",1907145001,99999,30),
	//BILLING_CODE_5("1.50",1907115000,99999,1),
	//BILLING_CODE_71("2.00",1907120001,99999,1),
	//BILLING_CODE_30("1.00",1907110000,99999,1);

	private final String billingCodeFromCarrier;
	private final int billingCode;
	private final int credits;
	private final int validity;
	
	BillingCodeEnum (String billingCodeFromCarrier,int billingCode,int credits,int validity){
		this.billingCodeFromCarrier=billingCodeFromCarrier;
		this.billingCode=billingCode;
		this.validity=validity;
		this.credits=credits;
	}
	
	public String getBillingCodeFromCarrier() {
		return billingCodeFromCarrier;
	}

	public int getValidity() {
		return validity;
	}

	public int getBillingCode(){
		return this.billingCode;
	}
	
	public int getCredits() {
		return credits;
	}
	
	public static boolean isValidPlanId(String planId){
		for (BillingCodeEnum billingCodeEnum: values()){
			if (billingCodeEnum.getBillingCodeFromCarrier().equalsIgnoreCase(planId))
				return true;
		}
		return false;
	}
	
	public static BillingCodeEnum getBillingCodeEnum(String planId){
		for (BillingCodeEnum billingCodeEnum: values()){
			
			Float vuclipPrice=Float.parseFloat(billingCodeEnum.getBillingCodeFromCarrier());
			Float carrierPrice=Float.parseFloat(planId);
		/*	if (billingCodeEnum.getBillingCodeFromCarrier().equalsIgnoreCase(planId)){
				return billingCodeEnum;
			}*/
			if (vuclipPrice.equals(carrierPrice)){
				return billingCodeEnum;
			}
			
		}
		return null;
	}

	public static int getValidity(int billingCode) {
		for (BillingCodeEnum billingCodeEnum: values()){
			if (billingCodeEnum.getBillingCode()==billingCode){
				return billingCodeEnum.getValidity();
			}
		}
		return 0;
	}

	public static int getCredits(int billingCode) {
		for (BillingCodeEnum billingCodeEnum: values()){
			if (billingCodeEnum.getBillingCode()==billingCode){
				return billingCodeEnum.getCredits();
			}
		}
		return 0;
	}
}
