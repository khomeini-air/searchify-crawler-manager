package com.searchify.crawler.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.searchify.crawler.request.CrawlRequest;
import com.searchify.crawler.response.CrawlResponse;
import com.searchify.crawler.service.ICrawlService;
import com.searchify.crawler.util.LoggerMessages;
import com.searchify.crawler.util.RESTEndpointMapper;
import com.searchify.crawler.util.UrlValidation;

@RestController
public class CrawlingController {
	
	Logger LOG = LoggerFactory.getLogger(CrawlingController.class);
	
	@Autowired
	ICrawlService crawlService;
	
	@Autowired
	UrlValidation urlValidation;
	

    @CrossOrigin
	@PostMapping(value = RESTEndpointMapper.CRAWL)
	public ResponseEntity<CrawlResponse> crawlResource(@RequestBody CrawlRequest crawlRequest){
		LOG.info("In CrawlingController : gettingResourceLinks with Request- "+crawlRequest.getLink());
		
		if(urlValidation.validateUrl(crawlRequest.getLink())){
			LOG.info("In CrawlingController : Invalid url- "+crawlRequest.getLink());
			return new ResponseEntity<CrawlResponse>(new CrawlResponse(LoggerMessages.INVALID_URL), HttpStatus.OK);
		}
		
		CrawlResponse response = crawlService.crawlResource(crawlRequest.getLink());

		LOG.info("In CrawlingController : "+LoggerMessages.CRAWL_SUCCESS);
		return new ResponseEntity<CrawlResponse>(response, HttpStatus.OK);
	}
}
