package com.searchify.crawler.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * CrawlRequest
 * 
 * @author RV
 *
 */
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class CrawlRequest {
	
	private String link;
	
	
}
