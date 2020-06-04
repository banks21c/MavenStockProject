package html.parsing.stock.news;

import java.awt.event.KeyEvent;
import java.io.File;
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
import html.parsing.stock.StockUtil;
import html.parsing.stock.util.FileUtil;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

public class NewsSedailyCom extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 590660743475607524L;
	private static Logger logger = null;
	final static String userHome = System.getProperty("user.home");

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static String strDate = null;
	static String strTitle = null;

	DecimalFormat df = new DecimalFormat("###.##");

	private javax.swing.JButton executeBtn;
	private javax.swing.JButton eraseBtn;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JLabel urlLbl;
	private javax.swing.JTextField urlTf;
	private javax.swing.JPanel executeResultPnl;
	private static javax.swing.JLabel executeResultLbl;

	public NewsSedailyCom() {
		logger = LoggerFactory.getLogger(this.getClass());

		initComponents();
	}

	NewsSedailyCom(int i) {
		logger = LoggerFactory.getLogger(this.getClass());

		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "http://www.sedaily.com/NewsView/1RVOCVY2MC";
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
				new NewsSedailyCom().setVisible(true);
			}
		});
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
		Font ft = new Font(Font.SERIF, Font.PLAIN, 10);
		executeResultLbl.setForeground(Color.black);
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
		logger = LoggerFactory.getLogger(NewsSedailyCom.class.getName());

		News gurl = new News();
		gurl.getURL(url);
		String protocol = gurl.getProtocol();
		String host = gurl.getHost();
		String path = gurl.getPath();
		String protocolHost = gurl.getProtocolHost();

		StringBuilder sb1 = new StringBuilder();
		Document doc;
		String strTitleForFileName;
		try {
			doc = Jsoup.connect(url).get();
			doc.select("iframe").remove();
			doc.select("script").remove();
			doc.select("ins").remove();
			doc.select(".btn_info").remove();

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			Elements title = doc.select("#v-left-scroll-in h2");
			if (title.size() <= 0) {
				title = doc.select(".article_head .art_tit");
			}
			strTitle = title.get(0).text();
			System.out.println("title:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			Elements authorEls = doc.select("#v-left-scroll-in .view_top ul");
			String strAuthor = "";
			String authorAndTime = "";
			String strDate = "";
			if (authorEls.size() > 0) {
				strAuthor = authorEls.select("li").get(0).text();
				authorAndTime = authorEls.outerHtml();
				strDate = authorEls.select("li").get(1).text();
				strDate = strDate.replace("입력", "");
			} else {
				authorEls = doc.select(".article_head .article_info");
				strAuthor = authorEls.select("span").get(2).text();
				authorAndTime = authorEls.outerHtml();
				strDate = authorEls.select("span").get(0).text();
				strDate = strDate.replace("입력", "");
			}
			System.out.println("authorEls:[" + authorEls + "]");
			System.out.println("strAuthor:[" + strAuthor + "]");
			System.out.println("authorAndTime:[" + authorAndTime + "]");
			System.out.println("strDate:[" + strDate + "]");

			String strFileNameDate = strDate;
			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);

			Elements contentEls = doc.select(".view_con");
			System.out.println("contentEls:" + contentEls);
			System.out.println("contentEls.size:" + contentEls.size());
			Element contentEl;
			String strArticleSummary = "";
			String strArticle = "";
			logger.debug("ssssssssssssssssssssssssss");
			if (contentEls.size() > 0) {
				contentEl = doc.select(".view_con").get(0);
				strArticle = contentEl.outerHtml();
			} else {
				logger.debug("elseeeeeeeeeeeeee");
				Elements articleSummaryEls = doc.select(".article_summary");
				logger.debug("articleSummaryEls:" + articleSummaryEls);
				if (articleSummaryEls.size() > 0) {
					Element articleSummaryEl = articleSummaryEls.get(0);
					logger.debug("articleSummaryEl:" + articleSummaryEl);
					articleSummaryEl.removeAttr("style");
					articleSummaryEl.attr("style",
							"font-family: 'Noto Sans KR', sans-serif;margin-bottom: 6px;font-weight: bold;line-height: 1.2em;letter-spacing: 0 !important;");
					strArticleSummary = articleSummaryEl.outerHtml();
				}
				contentEl = doc.select(".article_view").get(0);
				strArticle = contentEl.outerHtml();
			}
			// System.out.println("textBody:"+textBody);
			Document textBodyDoc = Jsoup.parse(strArticleSummary + strArticle);
			textBodyDoc.select(".lmbox1").attr("style", "font-size:10pt;color:gray;");
			String strContent = textBodyDoc.html();
			System.out.println("strContent:" + strContent);
			strContent = StockUtil.makeStockLinkStringByExcel(strContent);

			Elements copyrightEls = doc.select(".copyright");
			String copyright = "";
			if (copyrightEls.size() > 0) {
				copyright = copyrightEls.get(0).outerHtml();
			} else {
				copyrightEls = doc.select(".article_copy");
				if (copyrightEls.size() > 0) {
					copyright = copyrightEls.get(0).outerHtml();
				}
			}

			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			// sb1.append("<meta http-equiv=\"Content-Type\"
			// content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox());

			sb1.append("<div style='width:548px'>\r\n");

			sb1.append("<h3> 기사주소:[<a href='").append(url).append("' target='_sub'>").append(url)
					.append("</a>] </h3>\n");
			sb1.append("<h2>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
			sb1.append(authorAndTime).append("<br>\r\n");
			sb1.append(strContent).append("<br>\r\n");
			sb1.append(copyright).append("<br>\r\n");

			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");

			File dir = new File(userHome + File.separator + "documents" + File.separator + host);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

			fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("추출완료");
			if (executeResultLbl != null) {
				executeResultLbl.setText("추출완료");
				Font ft = new Font(Font.SERIF, Font.PLAIN, 10);
				executeResultLbl.setForeground(Color.red);
			}
		}
		return sb1;
	}

}
