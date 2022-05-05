package com.searchify.crawler.service;

import com.searchify.crawler.response.CrawlResponse;

public interface ICrawlService {

	CrawlResponse crawlResource(String link);

}
