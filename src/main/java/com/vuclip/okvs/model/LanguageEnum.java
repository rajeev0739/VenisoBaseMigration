package com.vuclip.okvs.model;

public enum LanguageEnum {
	AR("ar",87),
	EN("en",86);
	
	private final String languageFromCarrier;
	private final int languageId;
	
	LanguageEnum(String languageFromCarrier,int languageId){
		this.languageFromCarrier=languageFromCarrier;
		this.languageId=languageId;
	}

	public String getLanguageFromCarrier() {
		return languageFromCarrier;
	}

	public int getLanguageId() {
		return languageId;
	}
	
	public static LanguageEnum getLanguageEnum(String language){
		for (LanguageEnum languageEnum: values()){
			if (languageEnum.getLanguageFromCarrier().equalsIgnoreCase(language)){
				return languageEnum;
			}
		}
		return null;
	}
}
