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

public class WwwIlyoseoulCoKr extends javax.swing.JFrame {

	public final static String USER_HOME = System.getProperty("user.home");
	private static final long serialVersionUID = -3831732256777123240L;
	private static Logger logger = LoggerFactory.getLogger(WwwIlyoseoulCoKr.class);
	

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

	WwwIlyoseoulCoKr(int i) {

		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "http://www.asiae.co.kr/news/view.htm?idxno=2018041911043434606";
		}
		if (url != null && !url.equals("")) {
			createHTMLFile(url);
		}
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
				new WwwIlyoseoulCoKr().setVisible(true);
			}
		});
	}

	public WwwIlyoseoulCoKr() {

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
			doc = Jsoup.connect(url).get();

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			doc.select("iframe").remove();
			doc.select("script").remove();
			doc.select(".news_twobox").remove();
			doc.select(".art_btm").remove();
			doc.select(".article_ad").remove();
			doc.select(".add_banner").remove();

			String fileName2 = USER_HOME + File.separator + "documents" + File.separator + strYMD + ".html";
			System.out.println("fileName2:" + fileName2);
//            Writer bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName2, true), StandardCharsets.UTF_8));
//            bw.write(doc.html());
//            bw.close();

			Elements title = doc.select(".article-view-header .article-header-wrap .article-head-title");
			System.out.println("title:" + strTitle);
			if (title != null && title.size() > 0) {
				strTitle = title.get(0).text();
			}
			System.out.println("title:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			String strAuthor = doc.select(".article-view-header .info-text li:first-child").text();
			System.out.println("strAuthor:[" + strAuthor + "]");

			String strDate = doc.select(".article-view-header .info-text li").get(1).text();
			System.out.println("strDate:[" + strDate + "]");
			strDate = strDate.replace("입력", "");
			strDate = strDate.replace("&nbsp;", " ");
			strDate = strDate.replace("&nbsp", " ");
			strDate = strDate.trim();

			String strFileNameDate = strDate;
			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);

			Elements contentEls = doc.select("#article-view-content-div");
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
			System.out.println("strContent:" + strContent);
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
			sb1.append("<h2 id='title'>[" + strDate + "]" + strTitle + "</h2><br>\r\n");
			sb1.append(strAuthor + "<br>\r\n");
			sb1.append(strContent + "<br>\r\n");
			sb1.append(copyright + "<br>\r\n");

			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");

			System.out.println("fileDir:" + USER_HOME + File.separator + "documents" + File.separator + host);
			File dir = new File(USER_HOME + File.separator + "documents" + File.separator + host);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			System.out.println("fileName1:" + USER_HOME + File.separator + "documents" + File.separator + strFileNameDate
					+ "_" + strTitleForFileName + ".html");
			String fileName = USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

			System.out.println("fileName2:" + USER_HOME + File.separator + "documents" + File.separator + strFileNameDate
					+ "_" + strTitleForFileName + ".html");
			fileName = USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_"
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
