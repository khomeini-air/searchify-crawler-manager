package com.searchify.crawler.util;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class UrlValidation {

	//pattern matching the valid url
	private final static Pattern FILTERS_1 = Pattern.compile("^(http://|https://)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$");
	
	private final static Pattern FILTERS_2 = Pattern.compile("^(http://|https://)?(www.)?([a-zA-Z0-9]+).[a-z]{3}.?([a-z]+)?$");

	
	/**
	 * Validating the url is valid or nor
	 * @param link
	 * @return
	 */
	public boolean validateUrl(String link) {
		return false;
//		return ( FILTERS_1.matcher(link).matches() || FILTERS_2.matcher(link).matches());
	}

}
