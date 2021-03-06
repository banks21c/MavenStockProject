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
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.ImageUtil;
import html.parsing.stock.util.StockUtil;

public class WwwSentvCoKr extends javax.swing.JFrame {

	public final static String USER_HOME = System.getProperty("user.home");
	private static final long serialVersionUID = -233966048037670672L;
	private static Logger logger = null;
	

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

	WwwSentvCoKr(int i) {
		logger = LoggerFactory.getLogger(this.getClass());

		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "https://www.sentv.co.kr/news/view/571656";
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
				new WwwSentvCoKr().setVisible(true);
			}
		});
	}

	public WwwSentvCoKr() {
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
		Document doc;
		String strTitleForFileName;
		try {
			doc = Jsoup.connect(url).get();
			doc.select("iframe").remove();
			doc.select("script").remove();
			doc.select("ins").remove();
			doc.select(".btn_info").remove();
			doc.select(".new-view-wrap .section_2 .inner .tag-wrap").remove();
			doc.select(".new-view-wrap .section_2 .inner .reporter_wrap").remove();

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			Elements title = doc.select(".new-view-wrap .section_1 .title");
			strTitle = title.get(0).text();
			System.out.println("title:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

//            Elements author = doc.select(".new-view-wrap .section_1 .util-area span.writer").eq(0);
			Element author = doc.select(".new-view-wrap .section_1 .util-area span.writer").get(0);
			System.out.println("author:[" + author + "]");
			String strAuthor = author.text();
//            String author1 = doc.select("#v-left-scroll-in .view_top ul li").get(0).text();
//            System.out.println("author1:[" + author1 + "]");

//            String authorAndTime = doc.select("#v-left-scroll-in .view_top ul").outerHtml();
//            System.out.println("authorAndTime:[" + authorAndTime + "]");

//            Elements writeDateTime = doc.select(".new-view-wrap .section_1 .util-area span.date").eq(0);
			Element writeDateTime = doc.select(".new-view-wrap .section_1 .util-area span.date").get(0);
			System.out.println("writeDateTime:[" + writeDateTime + "]");
			String strDate = writeDateTime.text();
			strDate = strDate.replace("입력", "").trim();
			System.out.println("strDate:[" + strDate + "]");
			String strFileNameDate = strDate;
			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);

			Element content = doc.select(".new-view-wrap .section_2 .inner").get(0);
			/* 배너 삭제 */
			content.select(".newBanner.mid-banner-area").remove();

			Elements imgs = content.select("img");
			System.out.println("imgs.size:" + imgs.size());
			for (Element img : imgs) {
				String imgWidth = img.attr("width");
				String imgHeight = img.attr("height");
				String styleAttr = img.attr("style");
				String imgSrc = img.attr("src");
				System.out.println("styleAttr:" + styleAttr);
				System.out.println("imgSrc:" + imgSrc);

				String beforeString = "";
				String afterString = "";
				if (styleAttr != null && styleAttr.contains("width")) {
					beforeString = styleAttr.substring(0, styleAttr.indexOf("width"));
					String width1 = styleAttr.substring(styleAttr.indexOf("width"));
					String width2 = "";
					if (width1.indexOf(";") != -1) {
						width2 = width1.substring(0, width1.indexOf(";"));
						afterString = width1.substring(width1.indexOf(";") + 1);
					} else {
						width2 = width1;
						afterString = "";
					}
					String width = "";
					if (width2.indexOf(":") != -1) {
						width = width2.split(":")[1];
						width = width.replaceAll("px", "").trim();
					}

					int iWidth = 0;
					int changeWidth = 0;
					System.out.println("width:" + width);
					if (width != null && !width.equals("")) {
						iWidth = Integer.parseInt(width);
						if (iWidth > 741) {
							changeWidth = 741;
						} else {
							changeWidth = iWidth;
						}
					}
					String strChangeWidth = "width:" + changeWidth + "px";
					System.out.println("width=============>" + beforeString + changeWidth + ";" + afterString);
					img.attr("style", beforeString + changeWidth + ";" + afterString);
				} else {
					img.attr("style", ImageUtil.getImageStyle(imgSrc));
				}
			}

			String textBody = content.outerHtml();
			// System.out.println("textBody:"+textBody);
			Document textBodyDoc = Jsoup.parse(textBody);
			textBodyDoc.select("div").get(0).attr("style", "font-size:11pt");
			textBodyDoc.select(".lmbox1").attr("style", "font-size:10pt;color:gray;");
			String strContent = textBodyDoc.html();
			System.out.println("strContent:" + strContent);
			strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);
			

			String copyright = content.select(".copyright").outerHtml();

			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			// sb1.append("<meta http-equiv=\"Content-Type\"
			// content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox(strMyComment));

			sb1.append("<div style='width:741px'>\r\n");

			sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
			sb1.append("<h2 id='title'>[" + strDate + "] " + strTitle + "</h2>\n");
			sb1.append(strAuthor + "<br>\r\n");
			sb1.append(strDate + "<br>\r\n");
			sb1.append(strContent + "<br>\r\n");
			sb1.append(copyright + "<br>\r\n");

			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");

			File dir = new File(USER_HOME + File.separator + "documents" + File.separator + host);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String fileName = USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

			fileName = USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

		} catch (Exception e) {
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
