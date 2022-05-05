package com.searchify.crawler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.searchify.crawler.response.CrawlResponse;
import com.searchify.crawler.util.CrawlingUtils;

@Service
public class CrawlServiceImpl implements ICrawlService {

	@Autowired
	CrawlingUtils crawlingUtils;
	
	@Override
	public CrawlResponse crawlResource(String link) {
		
		return crawlingUtils.crawlResource(link);
	}

}
