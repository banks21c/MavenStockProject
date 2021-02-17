package html.parsing.stock.news;

import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.StockUtil;

public class VipMkCoKr extends javax.swing.JFrame {

	private static Logger logger = LoggerFactory.getLogger(VipMkCoKr.class);
	final static String userHome = System.getProperty("user.home");

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	static String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E HH.mm.ss.SSS", Locale.KOREAN).format(new Date());
	static String strDate = null;
	static String strTitle = null;
	static String strSubTitle = null;

	DecimalFormat df = new DecimalFormat("###.##");

	private javax.swing.JButton executeBtn;
	private javax.swing.JButton eraseBtn;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JLabel urlLbl;
	private javax.swing.JTextField urlTf;
	private javax.swing.JPanel executeResultPnl;
	private static javax.swing.JLabel executeResultLbl;
//	String strUrl = "http://vip.mk.co.kr/newSt/news/news_view.php?p_page=&sCode=122&t_uid=22&c_uid=136511&topGubun=";
	String strDefaultUrl = "http://vip.mk.co.kr/news/view/21/20/1820410.html";
	String strUrl;
	
	VipMkCoKr(int i) {
		strUrl = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + strUrl + "]");
		if (StringUtils.defaultString(strUrl).equals("")) {
			strUrl = strDefaultUrl;
		}
		createHTMLFile(strUrl);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// new NewsMoneyToday(1);
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
			| javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(NewsReader.class.getName()).log(java.util.logging.Level.SEVERE, null,
				ex);
		}
		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new VipMkCoKr().setVisible(true);
			}
		});
	}

	public VipMkCoKr() {

		initComponents();
	}

	private void initComponents() {

		jPanel2 = new javax.swing.JPanel();
		urlLbl = new javax.swing.JLabel();
		urlTf = new javax.swing.JTextField();
		jPanel1 = new javax.swing.JPanel(new java.awt.FlowLayout());
		executeBtn = new javax.swing.JButton();
		eraseBtn = new javax.swing.JButton();
		executeResultPnl = new javax.swing.JPanel();
		executeResultPnl.setSize(100, 50);
		executeResultLbl = new javax.swing.JLabel("추출준비");
		executeResultLbl.setSize(100, 50);
		executeResultPnl.add(executeResultLbl);

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("웹페이지 추출");
		setAlwaysOnTop(true);
		setMinimumSize(new java.awt.Dimension(500, 54));
		setSize(new java.awt.Dimension(500, 100));

		jPanel2.setLayout(new java.awt.BorderLayout());

		urlLbl.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		urlLbl.setText("주소 : ");
		urlLbl.setToolTipText("");
		jPanel2.add(urlLbl, java.awt.BorderLayout.WEST);

		urlTf.setHorizontalAlignment(javax.swing.JTextField.LEFT);
		urlTf.setToolTipText("");
		urlTf.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				urlTfActionPerformed(evt);
			}
		});
		urlTf.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyReleased(java.awt.event.KeyEvent evt) {
				urlTfKeyReleased(evt);
			}
		});
		jPanel2.add(urlTf, java.awt.BorderLayout.CENTER);

		getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

		getContentPane().add(executeResultPnl, java.awt.BorderLayout.EAST);

		eraseBtn.setText("지우기");
		eraseBtn.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				eraseBtnActionPerformed(evt);
			}
		});
		jPanel1.add(eraseBtn);

		executeBtn.setText("페이지 추출");
		executeBtn.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				executeBtnActionPerformed(evt);
			}
		});
		jPanel1.add(executeBtn);

		getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

		pack();
	}

	private void executeBtnActionPerformed(java.awt.event.ActionEvent evt) {
		String url = StringUtils.defaultString(urlTf.getText(), "");
		System.out.println("url==>" + url);
		if (!url.equals("")) {
			createHTMLFile(url);
		}
	}

	private void eraseBtnActionPerformed(java.awt.event.ActionEvent evt) {
		urlTf.setText("");
		executeResultLbl.setText("추출준비");
	}

	private void urlTfActionPerformed(java.awt.event.ActionEvent evt) {

	}

	private void urlTfKeyReleased(java.awt.event.KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			// SwingUtilities.getWindowAncestor(evt.getComponent()).dispose();
			String url = urlTf.getText();
			if (url != null && !url.equals("")) {
				createHTMLFile(url);
			}
		}
	}

	public static StringBuilder createHTMLFile(String url) {
		return createHTMLFile(url, "");
	}

	public static StringBuilder createHTMLFile(String strUrl, String strMyComment) {
		News gurl = new News();
		gurl.getURL(strUrl);
		String protocol = gurl.getProtocol();
		String host = gurl.getHost();
		String path = gurl.getPath();
		String protocolHost = gurl.getProtocolHost();

		StringBuilder sb1 = new StringBuilder();
		Document doc = null;
		String strTitleForFileName;
		FileWriter fw;
		try {
			System.out.println("strUrl:" + strUrl);
			doc = Jsoup.connect(strUrl)
				.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
				.header("Accept-Language", "en").header("Accept-Encoding", "gzip,deflate,sdch").get();
			System.out.println("doc:[" + doc + "]");

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			doc.select("iframe").remove();
			doc.select("script").remove();

			Elements title;
			Element tableEl;
			String strAuthor = "";
			Elements subTitle;
			String strDate = "";
			Node lastPNode;
			Element contentEl;
			if (strUrl.contains("planning_news_view")) {
				title = doc.select(".headtitle");
				tableEl = doc.select(".headtitle").get(0).parent().parent().parent().parent();
				Node authorNode = tableEl.select("br").last().previousSibling();
				strAuthor = authorNode.toString();
				strAuthor = strAuthor.replace("[", "").replace("]", "");
				lastPNode = tableEl.select("p").last();
				lastPNode = lastPNode.childNode(0);
				System.out.println("lastPNode:" + lastPNode);
				strDate = lastPNode.outerHtml();
				strDate = strDate.replace("입력", "").trim();
				System.out.println("strDate:" + strDate);
				contentEl = tableEl;
			} else {
				title = doc.select("div#Titless");
				contentEl = doc.select("div#Conts").get(0);
				System.out.println("div#Conts => "+contentEl);
			}
			
			contentEl.select("nobr").attr("style","white-space:nowrap");
			contentEl.select("nobr").tagName("div");
			
			System.out.println("title:" + strTitle);
			if (title != null && title.size() > 0) {
				strTitle = title.get(0).text();
			}
			System.out.println("title:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			subTitle = doc.select("h2.sub_title1");
			String strSubTitle = "";
			if (subTitle.size() > 0) {
				strSubTitle = subTitle.outerHtml();
			}

			if (strDate.equals("")) {
				strDate = title.get(0).parent().nextElementSibling().text();
			}
			if (strDate.equals("")) {
				strDate = title.get(0).parent().nextElementSibling().nextElementSibling().text();
			}
			String strDateArray[] = strDate.split("\\|");
			if (strDateArray.length == 3) {
				strDate = strDateArray[2];
				strDate = strDate.replace("_", " ").trim();
			}
			String strFileNameDate = strDate;
			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);

			System.out.println("contentEl1:[" + contentEl + "]:contentEl1");
			contentEl.select(".con_txt_new").remove();
			contentEl.select("spacer").remove();
			contentEl.select("style").remove();
			System.out.println("contentEl2:[" + contentEl + "]:contentEl2");

			if (strAuthor.equals("")) {
//				strAuthor = doc.select(".author").html();
				strAuthor = contentEl.select("br").last().previousSibling().toString();
				contentEl.select("br").last().previousSibling().remove();
			}
			System.out.println("strAuthor:[" + strAuthor + "]");

			String strContent = contentEl.outerHtml();
			System.out.println("strContent:" + strContent);
			strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);
			Document contentDoc = Jsoup.parse(strContent);
			contentDoc.select("#myCommentDiv").remove();
			strContent = contentDoc.select("body").html();

			String copyright = "";

			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			// sb1.append("<meta http-equiv=\"Content-Type\"
			// content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox(strMyComment));

			sb1.append("<div style='width:741px'>\r\n");

			sb1.append("<h3> 기사주소:[<a href='" + strUrl + "' target='_sub'>" + strUrl + "</a>] </h3>\n");
			sb1.append("<h2 id='title'>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
			sb1.append(strSubTitle + "<br>\r\n");
//			sb1.append(strDate + "<br>\r\n");
			sb1.append(strAuthor + "<br>\r\n");
			sb1.append(strContent + "<br>\r\n");
			sb1.append(copyright + "<br>\r\n");

			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");

			System.out.println("fileDir:" + userHome + File.separator + "documents" + File.separator + host);
			File dir = new File(userHome + File.separator + "documents" + File.separator + host);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			System.out.println("fileName1:" + userHome + File.separator + "documents" + File.separator + strFileNameDate
				+ "_" + strTitleForFileName + ".html");
			String fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_"
				+ strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

			System.out.println("fileName2:" + userHome + File.separator + "documents" + File.separator + strFileNameDate
				+ "_" + strTitleForFileName + ".html");
			fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_"
				+ strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("추출완료");
			if (executeResultLbl != null) {
				executeResultLbl.setText("추출완료");
			}
		}
		return sb1;
	}

}
