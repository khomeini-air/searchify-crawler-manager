package com.searchify.crawler.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.searchify.crawler.response.CrawlResponse;

/** 
 * Crawling Util class to handle the logic to crawl resource links and generate the ste of links and static content
 * 
 * @author rv
 *
 */
@Component
public class CrawlingUtils {
	
	//REGEX to filter the resources like Pdf, gif, xls, etc
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp4|zip|gz|pdf|xls|xlsx|doc|docx))$");
	
	Map<String, String> current = new HashMap<String, String>();
	Set<String> internalLinks = new TreeSet<>();
	Set<String> externalLinks = new TreeSet<>();
	Set<String> staticResources = new TreeSet<>();
	Set<String> otherResources = new TreeSet<>();
	
	/**
	 * This method does the operation to crawl the respective link and collect all the links from the page 
	 * to make set for the next set of URL to be crawled. Thus each and every url in the set needs to called
	 *  again and new set of links needs to created. While goes on we have use a way to avoid calling the same 
	 *  again if it had been already visited. In the course of extracting links, any Web crawler will encounter 
	 *  multiple links to the same document. To avoid downloading and processing a document multiple times, 
	 *  a URL-seen test must be performed on each extracted link before adding it to the URL frontier

	 * @param crawlingUrl
	 * @param url
	 * @param urls
	 */
	public void getLinks(String crawlingUrl, String url, Set<String> urls) {
		
		//checks if the same url is already visited
		if(!urls.add(url))
			return;

		try {
			Document doc = Jsoup.connect(url).get();
			 Elements metaTags = doc.getElementsByTag("meta");
			 
		        
		        String keywords = null;
		        String description =null;
		        String title = null;
		        
		        if(crawlingUrl.equals(url)) {
		        	for (Element metaTag : metaTags) {
			        	  String content = metaTag.attr("content");
			        	  String name = metaTag.attr("name");

			        	  if("keywords".equals(name)) {
			        	      keywords = content;
			        	  }
			        	  if("description".equals(name)) {
			        		  description = content;
				          }
//			        	  if("title".equals(name)) {
//			        		  title = content;
//					      }
			        	 
			        	}
			    
				    current.put("url", url);
				    current.put("keywords",  keywords);
				    current.put("description", description);
				    current.put("title", doc.title());
		        }
		        
		        
			Elements elements = doc.select("a");
			for(Element element : elements){

				String nextUrl = element.absUrl("href");

				//checks if the url is empty or starts '#'
				if(StringUtils.isEmpty(nextUrl) || (!StringUtils.isEmpty(nextUrl) && ( nextUrl.contains("#"))))
					continue;

				//checks if the url have the pattern matching resources
				if(FILTERS.matcher(nextUrl).matches()){
					otherResources.add(nextUrl);
					continue;
				}
				
				//checks if the url is external or internal
				if(nextUrl.startsWith(crawlingUrl)){
					internalLinks.add(nextUrl);
//					getLinks(crawlingUrl, nextUrl, urls);
				}
				else
					externalLinks.add(nextUrl);

			}
			
			//checks for all the static resources on the platform
			Elements staticElements = doc.select("img");
	        for(Element element : staticElements){
	        	String staticResource = element.absUrl("src");
	        	staticResources.add(staticResource);
	        	
	        }
	        
	       
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(current.isEmpty()) {
				current.put("error", "invalid url");
			}
			
		}
	}

	public CrawlResponse crawlResource(String link) {
		current = new HashMap<String, String>();
		internalLinks = new TreeSet<>();
		externalLinks = new TreeSet<>();
		staticResources = new TreeSet<>();
		otherResources = new TreeSet<>();
		getLinks(link, link, new HashSet<>());
		return new CrawlResponse(LoggerMessages.CRAWL_SUCCESS, current, internalLinks, externalLinks, staticResources, otherResources);
	}
}
