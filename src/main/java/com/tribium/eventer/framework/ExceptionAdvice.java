package com.tribium.eventer.framework;

public class ExceptionAdvice
{
	public String possibleCauses;
	public String howToFix;

	public ExceptionAdvice()
	{
	}

	public ExceptionAdvice(String possibleCauses, String howToFix)
	{
		this.possibleCauses = possibleCauses;
		this.howToFix = howToFix;
	}
}
