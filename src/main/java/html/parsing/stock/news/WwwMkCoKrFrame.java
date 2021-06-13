package html.parsing.stock.news;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.ImageUtil;
import html.parsing.stock.util.StockUtil;

public class WwwMkCoKrFrame extends javax.swing.JFrame {

	public final static String USER_HOME = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(WwwMkCoKrFrame.class);
	

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
	private static String strMyComment = "";

	WwwMkCoKrFrame(int i) {
		logger = LoggerFactory.getLogger(this.getClass());
		logger.debug(this.getClass().getSimpleName());
		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		logger.debug("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "https://www.mk.co.kr/news/society/view/2019/12/1091653/";
		}
		createHTMLFile(url);
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
				new WwwMkCoKrFrame().setVisible(true);
			}
		});
	}

	public WwwMkCoKrFrame() {
		logger = LoggerFactory.getLogger(this.getClass());
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
		String url = urlTf.getText();
		if (url != null && !url.equals("")) {
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

	public StringBuilder createHTMLFile(String url) {
		return createHTMLFile(url, "");
	}

	public StringBuilder createHTMLFile(String url, String strMyComment) {
		News gurl = new News();
		gurl.getURL(url);
		String protocol = gurl.getProtocol();
		String host = gurl.getHost();
		String path = gurl.getPath();
		String protocolHost = gurl.getProtocolHost();

		StringBuilder sb1 = new StringBuilder();
		Document doc = null;
		String strTitleForFileName;
		try {
			logger.debug("url:" + url);
			doc = Jsoup.connect(url)
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
					.header("Accept-Language", "en").header("Accept-Encoding", "gzip,deflate,sdch").get();
//			logger.debug("doc:[" + doc + "]");

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			doc.select("iframe").remove();
			doc.select("script").remove();
			doc.select("all").remove();
			doc.select("div").attr("style", "width:741px");

			Elements title = doc.select("h1.top_title");
			logger.debug("title1:" + strTitle);
			if (title != null && title.size() > 0) {
				strTitle = title.get(0).text();
			}
			if (strTitle == null || strTitle.trim().equals("")) {
				title = doc.select("div.view_title h3");
				if (title != null && title.size() > 0) {
					strTitle = title.get(0).text();
				}
			}
			if (strTitle == null || strTitle.trim().equals("")) {
				title = doc.select("#view_tit .head_tit");
				if (title != null && title.size() > 0) {
					strTitle = title.get(0).text();
				}
			}

			logger.debug("title2:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			logger.debug("strTitleForFileName:" + strTitleForFileName);

			Elements subTitle = doc.select("h2.sub_title1");
			String strSubTitle = "";
			if (subTitle != null && subTitle.size() > 0) {
				strSubTitle = subTitle.outerHtml();
			}

			String strAuthor = doc.select(".author").html();
			logger.debug("strAuthor:[" + strAuthor + "]");
			strAuthor = strAuthor.replace("&nbsp;", " ");

			Elements dateElements = doc.select(".news_title_author .lasttime");
			logger.debug("dateElements:[" + dateElements + "]");
			Element dateElement = null;
			if (dateElements != null && !dateElements.isEmpty() && dateElements.size() > 0) {
				dateElement = dateElements.get(0);
			} else {
				dateElements = doc.select(".news_title_author .lasttime1");
				if (dateElements != null && dateElements.size() > 0) {
					dateElement = doc.select(".news_title_author .lasttime1").get(0);
				}
			}
			logger.debug("dateElements :" + dateElements);
			if (dateElements == null || dateElements.isEmpty() || dateElements.size() <= 0) {
				dateElements = doc.select("#view_tit .sm_num");
				if (dateElements != null && !dateElements.isEmpty() && dateElements.size() > 0) {
					dateElement = dateElements.get(0);
				}
			}
			String strFileNameDate = "";
			if (dateElement != null) {
				strDate = dateElement.html();
				logger.debug("strDate :" + strDate);
				strDate = strDate.replace("입력 :", "").trim();
				if (strDate.contains("수정")) {
					strDate = strDate.substring(0, strDate.indexOf("수정"));
				}
				strDate = strDate.replace("&nbsp;", "").trim();
				logger.debug("strDate :[" + strDate + "]");
				strDate = strDate.replace(".", "-");
				logger.debug("strDate :[" + strDate + "]");

				strFileNameDate = strDate;
				strFileNameDate = StockUtil.getDateForFileName(strDate);
				logger.debug("strFileNameDate:" + strFileNameDate);
			}
			logger.debug("strDate:[" + strDate + "]");

			Elements contentEls = doc.select("#article_body");
			if (contentEls.size() <= 0) {
				contentEls = doc.select(".view_txt");
			}
			logger.debug("contentEls:" + contentEls);
			if (contentEls.size() <= 0) {
				contentEls = doc.select(".read_txt");
			}

			Elements imgEls = contentEls.select("img");
			for (Element imgEl : imgEls) {
				String imgUrl = imgEl.attr("src");
				logger.debug("imgUrl:" + imgUrl);
				if (!imgUrl.startsWith("http")) {
					if (imgUrl.startsWith("//")) {
						imgUrl = protocol + ":" + imgUrl;
					} else {
						imgUrl = protocolHost + imgUrl;
					}
				}
				imgEl = ImageUtil.getImageWithStyle(imgEl, imgUrl);
				logger.debug("imgEl:" + imgEl);
			}

			String strContent = contentEls.html();
			strContent = strContent.replace("src=\"//", "src=\"" + protocol + "://");

			strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);
			

			String copyright = "";

			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			// sb1.append("<meta http-equiv=\"Content-Type\"
			// content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox(strMyComment));

			sb1.append("<div style='width:741px'>\r\n");

			sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
			sb1.append("<h2 id='title'>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
			sb1.append(strSubTitle + "<br>\r\n");
			sb1.append(strAuthor + "<br>\r\n");
			sb1.append(strContent + "<br>\r\n");
			sb1.append(copyright + "<br>\r\n");

			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");

			logger.debug("fileDir:" + USER_HOME + File.separator + "documents" + File.separator + host);
			File dir = new File(USER_HOME + File.separator + "documents" + File.separator + host);
			if (!dir.exists()) {
				dir.mkdirs();
			}

//			logger.debug("fileName2:" + USER_HOME + File.separator + "documents" + File.separator + strYMD + ".html");
//			String fileName = USER_HOME + File.separator + "documents" + File.separator + strYMD + ".html";
//			FileUtil.fileWrite(fileName, doc.html());

			logger.debug("fileName1:" + USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html");
			String fileName = USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

//			logger.debug("fileName2:" + USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html");
//			fileName = USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
//			FileUtil.fileWrite(fileName, sb1.toString());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			logger.debug("추출완료");
			if (executeResultLbl != null) {
				executeResultLbl.setText("추출완료");
			}
		}
		return sb1;
	}

}
