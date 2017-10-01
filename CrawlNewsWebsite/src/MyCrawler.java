import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;



public class MyCrawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(gif|jpg"
			 + "|png|html|doc|pdf))$");
	private static int count = 0;
	private final static String fetch_file = "/Users/soniyasadalkar/Study/IR/Homeworks/2-HW/fetch_USA_today.csv";
	private final static String visit_file = "/Users/soniyasadalkar/Study/IR/Homeworks/2-HW/visit_USA_today.csv";
	private final static String urls_file = "/Users/soniyasadalkar/Study/IR/Homeworks/2-HW/urls_USA_today.csv";
	private final static String report_file = "/Users/soniyasadalkar/Study/IR/Homeworks/2-HW/CrawlReport_USA_today.txt";
	
	private static Set<String> uniqueURLs = new HashSet<String>();
	private static Set<String> uniqueNewsURLs = new HashSet<String>();
	private static Set<String> uniqueNonNewsURLs = new HashSet<String>();
	
	private static BufferedWriter fetch_bw;
	private static BufferedWriter visit_bw;
	private static BufferedWriter urls_bw;
	private static BufferedWriter report_bw;
	
	private static long fetchSucceeded = 0;
	private static long totalFetched = 0;
	private static long fetchFailed = 0;
	
	
	private static WebURL myWebUrl;
	private static int stat = 0;
	
	private static long total_urls_extracted = 0;
	
	private static long linksOK = 0;
	private static long linksMovedPermanently = 0;
	private static long linksMovedTemporarily = 0;
	private static long linksForbidden = 0;
	private static long linksNotFound = 0;
	private static long linksISE = 0;
	
	private static long lessthan1KB = 0;
	private static long sizeInTens = 0;
	private static long sizeInHundreds = 0;
	private static long sizeInThousands = 0;
	private static long sizeGreaterThan1MB = 0;
	
	private static long type_html = 0;
	private static long type_png = 0;
	private static long type_jpeg = 0;
	private static long type_svg = 0;
	private static long type_xicon = 0;
	private static long type_gif = 0;
	private static long type_pdf = 0;
			
			/**
			 * This method receives two parameters. The first parameter is the page
			 * in which we have discovered this new url and the second parameter is
			 * the new url. You should implement this function to specify whether
			 * the given url should be crawled or not (based on your crawling logic).
			 * In this example, we are instructing the crawler to ignore urls that
			 * have css, js, git, ... extensions and to only accept urls that start
			 * with "http://www.viterbi.usc.edu/". In this case, we didn't need the
			 * referringPage parameter to make the decision.
			 */
	
	
	
	@Override
	public void onStart() {
		try {
			urls_bw = new BufferedWriter(new FileWriter(urls_file));
			fetch_bw = new BufferedWriter(new FileWriter(fetch_file));
			visit_bw = new BufferedWriter(new FileWriter(visit_file));
			report_bw = new BufferedWriter(new FileWriter(report_file));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void onBeforeExit() {
		try {
			urls_bw.close();
			fetch_bw.close();
			visit_bw.close();
			
			report_bw.write("Name: Soniya Sadalkar\nUSC ID : 6771634935\nNews site crawled : usatoday.com\n\n");
			report_bw.write("Fetch Statistics\n");
			report_bw.write("================\n");
			report_bw.write("# fetches attempted: "+totalFetched+"\n");
			report_bw.write("# fetches succeeded: "+fetchSucceeded+"\n");
			report_bw.write("# fetched failed/aborted: "+fetchFailed+"\n\n");
			
			System.out.println("Total fetches : "+totalFetched);
			System.out.println("Success fetches : "+fetchSucceeded);
			System.out.println("Failed fetches : "+ fetchFailed);
			System.out.println("FAILED : " + myWebUrl+ " code: "+stat);
			
			report_bw.write("Outgoing URLs:\n");
			report_bw.write("Total URLs extracted: "+total_urls_extracted+"\n");
			report_bw.write("Unique URLs extracted: "+uniqueURLs.size()+"\n");
			report_bw.write("Unique URLs within news site: "+ uniqueNewsURLs.size()+"\n");
			report_bw.write("Unique URLS outside news site: "+uniqueNonNewsURLs.size()+"\n\n");
			
			System.out.println("Total URLS extracted = "+total_urls_extracted);
			System.out.println();
			
			report_bw.write("Status Codes:\n");
			report_bw.write("=============\n");
			report_bw.write("200 OK: "+linksOK+"\n");
			report_bw.write("301 Moved Permanently: "+linksMovedPermanently+"\n");
			report_bw.write("302 Moved Temporarily: "+linksMovedTemporarily+"\n");
			report_bw.write("403 Forbidden: "+linksForbidden+"\n");
			report_bw.write("404 Not Found: "+linksNotFound+"\n");
			report_bw.write("500 Internal Server Error"+linksISE+"\n\n");
			System.out.println("200 OK : "+linksOK);
			System.out.println("301 Moved Permanentely : "+linksMovedPermanently);
			System.out.println("302 Moved temporarily : "+ linksMovedTemporarily);
			System.out.println("403 Forbidden: "+linksForbidden);
			System.out.println("404 Not Found: "+linksNotFound);
			System.out.println("500 Internal Server Error : "+linksISE);
			System.out.println();
			
			report_bw.write("File Sizes:\n");
			report_bw.write("===========\n");
			report_bw.write("< 1KB: "+lessthan1KB+"\n");
			report_bw.write("1KB ~ <10KB: "+sizeInTens+"\n");
			report_bw.write("10KB ~ <100KB: "+sizeInHundreds+"\n");
			report_bw.write("100KB ~ <1MB: "+sizeInThousands+"\n");
			report_bw.write(">=1MB: "+sizeGreaterThan1MB+"\n\n");
			
			System.out.println("< 1KB : "+lessthan1KB);
			System.out.println("1KB ~ <10KB : "+sizeInTens);
			System.out.println("10KB ~ <100KB "+sizeInHundreds);
			System.out.println("100KB ~ <1MB : "+ sizeInThousands);
			System.out.println(">=1MB : "+sizeGreaterThan1MB);
			
			System.out.println();
			
			report_bw.write("Content Types:\n");
			report_bw.write("==============\n");
			report_bw.write("text/heml: "+type_html+"\n");
			report_bw.write("image/png: "+type_png+"\n");
			report_bw.write("image/jpeg: "+type_jpeg+"\n");
			report_bw.write("image/svg+xml: "+type_svg+"\n");
			report_bw.write("image/gif: "+type_gif+"\n");
			report_bw.write("image/x-icon: "+type_xicon+"\n");
			report_bw.write("application/pdf: "+type_pdf+"\n");
			
			System.out.println("text/html : "+type_html);
			System.out.println("image/png : "+ type_png);
			System.out.println("image/jpeg : "+ type_jpeg);
			System.out.println("image/svg+xml : "+ type_svg);
			System.out.println("image/gif : "+ type_gif);
			System.out.println("image/x-icon : "+ type_xicon);
			System.out.println("image/pdf : "+ type_pdf);
			
			report_bw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
		try {
			fetch_bw.write(webUrl+","+statusCode+"\n");
		} catch (IOException e) {
			System.out.println("VISIT pages write failed!!");
			e.printStackTrace();
		}
		totalFetched = totalFetched + 1;
		if(statusCode >= 200 && statusCode < 300) {
			if(statusCode == 200)
				linksOK++;
			fetchSucceeded = fetchSucceeded + 1;
			
		}
		else if(statusCode >= 300 && statusCode < 600) {
			if(statusCode == 301)
				linksMovedPermanently++;
			else if(statusCode == 302)
				linksMovedTemporarily++;
			else if(statusCode == 403)
				linksForbidden++;
			else if(statusCode == 404)
				linksNotFound++;
			else if(statusCode == 500)
				linksISE++;
			fetchFailed = fetchFailed + 1;
			myWebUrl = webUrl;
			stat = statusCode;
			
		}
	}
	
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase().replace(',', '_');
		
		if(referringPage != null) {
			uniqueURLs.add(href);
			try {
				if(href.startsWith("https://www.usatoday.com/")) {
					urls_bw.write(href+",OK"+"\n");
					uniqueNewsURLs.add(href);
				}
				else {
					urls_bw.write(href+",N_OK"+"\n");
					uniqueNonNewsURLs.add(href);
				}
			} catch (IOException e) {
				System.out.println("ALL URLS write failed!!");
				e.printStackTrace();
			}
		}
		
		return (referringPage != null &&
                referringPage.getContentType() != null &&
                referringPage.getContentType().contains("html") && (href.startsWith("https://www.usatoday.com/") || (href.startsWith("http://www.usatoday.com/")))) 
                || (FILTERS.matcher(href).matches() && (href.startsWith("https://www.usatoday.com/") || href.startsWith("http://www.usatoday.com/") ));
	}
	
	/**
	 * This function is called when a page is fetched and ready
	 * to be processed by your program.
	 */
	private void ComputeFileSizes(int size) {
		double size_in_kb = size / 1024.0;
		if( size_in_kb < 1 && size_in_kb >= 0 )
			lessthan1KB++;
		else if ( size_in_kb >= 1 && size_in_kb < 10 )
			sizeInTens++;
		else if ( size_in_kb >= 10 && size_in_kb < 100)
			sizeInHundreds++;
		else if ( size_in_kb >= 100 && size_in_kb < 1000)
			sizeInThousands++;
		else if(size_in_kb >= 1000)
			sizeGreaterThan1MB++;
		
	}
	
	private void ComputeContentTypeStats(String type) {
		type = type.toLowerCase();
		if(type.contains("text/html")) 
			type_html++;
		else if(type.contains("image/png"))
			type_png++;
		else if (type.contains("image/jpeg"))
			type_jpeg++;
		else if(type.contains("image/svg-+xml"))
			type_svg++;
		else if(type.contains("image/gif"))
			type_gif++;
		else if(type.contains("image/x-icon"))
			type_xicon++;
		else if(type.contains("application/pdf"))
			type_pdf++;
	}
	 @Override
	 public void visit(Page page) {
		 int status = page.getStatusCode();
		 String url = page.getWebURL().getURL();
		 String contentType = page.getContentType();
		 
		 ComputeContentTypeStats(contentType);
		 System.out.println(count+". URL: " + url);
		 System.out.println("Status: "+status);
		 
		 
		 
		 if (page.getParseData() instanceof HtmlParseData) {
			 HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			 String text = htmlParseData.getText();
			 String html = htmlParseData.getHtml();
			 Set<WebURL> links = htmlParseData.getOutgoingUrls();
			 total_urls_extracted = total_urls_extracted + links.size();
			 
			 /* Write the fetch attempted urls : that the crawler attempts to hit a url/ page and try to download it. 
			  * Its status code can be 2XX, 3XX, 4XX anything.
			  */
			 
			 
			 /* Visited page means the successful 2XX status page which the crawler actually visited. 
			  * This should be equal to the number of successful (2XX) fetched pages.
			  */
			 
			 if(status == 200) {
				 ComputeFileSizes(page.getContentData().length);
				 try {
					visit_bw.write(url+","+page.getContentData().length+","+links.size()+","+contentType+"\n");
				} catch (IOException e) {
					System.out.println("VISIT pages write failed!!");
					e.printStackTrace();
				}
			 }
			 System.out.println("Text length: " + text.length());
			 System.out.println("Html length: " + html.length());
			 System.out.println("Number of outgoing links: " + links.size());
			 count = count + 1;
		 }
	 }
}
