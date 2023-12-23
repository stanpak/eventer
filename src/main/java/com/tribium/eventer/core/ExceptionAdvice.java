package com.tribium.eventer.core;

public class ExceptionAdvice
{
	public String getPossibleCauses() {
		return possibleCauses;
	}

	public void setPossibleCauses(String possibleCauses) {
		this.possibleCauses = possibleCauses;
	}

	public String getHowToFix() {
		return howToFix;
	}

	public void setHowToFix(String howToFix) {
		this.howToFix = howToFix;
	}

	public String getReferences() {
		return references;
	}

	public void setReferences(String references) {
		this.references = references;
	}

	private String possibleCauses;
	private String howToFix;

	private String references;
}
