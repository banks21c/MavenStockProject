package html.parsing.stock.news;

import org.jsoup.nodes.Document;

public interface NewsInterface {

	public StringBuilder createHTMLFile(String strUrl, String strMyComment);

	public StringBuilder createHTMLFileFromWebView(String s1, String strUrl, String strMyComment);

	public StringBuilder createHTMLFileCommon(Document doc, String strUrl, String strMyComment);

}
