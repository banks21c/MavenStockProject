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
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.StockUtil;
public class NewsMtCoKrFrame extends javax.swing.JFrame {

	public final static String USER_HOME = System.getProperty("user.home");
	private static final long serialVersionUID = 1L;
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
	private String executeBtnTxt = "머니투데이뉴스";

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
				new NewsMtCoKrFrame().setVisible(true);
			}
		});
	}

	public NewsMtCoKrFrame() {
		logger = LoggerFactory.getLogger(this.getClass());

		initComponents();
	}

	private void initComponents() {

		jPanel2 = new javax.swing.JPanel();
		urlLbl = new javax.swing.JLabel();
		urlTf = new javax.swing.JTextField("http://news.mt.co.kr/mtview.php?no=2018030111101822011&MT_T");
		jPanel1 = new javax.swing.JPanel(new java.awt.FlowLayout());
		executeBtn = new javax.swing.JButton();
		eraseBtn = new javax.swing.JButton();
		executeResultPnl = new javax.swing.JPanel();
		executeResultPnl.setSize(100, 50);
		executeResultLbl = new javax.swing.JLabel();
		executeResultLbl.setSize(100, 50);
		executeResultPnl.add(executeResultLbl);

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle(executeBtnTxt + " 페이지 추출");
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

		executeBtn.setText(executeBtnTxt + " 페이지 추출");
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
		urlTf.requestFocus();
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

	NewsMtCoKrFrame(int i) {
		logger = LoggerFactory.getLogger(this.getClass());

		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "http://stylem.mt.co.kr/stylemView.php?no=2018020809234688426";
			url = "http://news.mt.co.kr/mtview.php?no=2018030111101822011&MT_T";
		}
		createHTMLFile(url);
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
		String strTitleForFileName = "";
		String strFileNameDate = "";
		try {
			doc = Jsoup.connect(url).get();
			doc.select(".util_box").remove();
			doc.select(".articleRelnewsFrame").remove();
			doc.select("iframe").remove();

			Elements article = doc.select("#article");
			System.out.println("article :[" + article + "]");

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			System.out.println(".view .view_top h1.subject :" + doc.select(".view .view_top h1.subject"));
			System.out.println(".view .view_top h1.subject :" + doc.select(".view .view_top h1.subject").get(0));
			strTitle = doc.select(".view .view_top h1.subject").get(0).text();
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			Elements subtitles = article.select("h2.sub_subject");
			System.out.println("subtitles:" + subtitles);
			String subtitle = "";
			if (subtitles.size() > 0) {
				subtitle = article.select("#article h2").get(0).outerHtml();
			}
			String strAuthor = article.select("#article .infobox1 a").outerHtml();
			System.out.println("strAuthor:" + strAuthor);
			Elements dateEl = doc.select(".view .view_top .view_info .info2 li.date");
			if (dateEl.size() > 0) {
				strDate = dateEl.get(0).text();
			} else {
				strDate = doc.select(".view_content .vc_top .info .info2 li.date").get(0).text();

			}
			System.out.println("strDate:" + strDate);
			if (strDate.startsWith(":")) {
				strDate = strDate.substring(1).trim();
			}
			strFileNameDate = strDate;
			strFileNameDate = StockUtil.getDateForFileName(strDate);
			strFileNameDate = strFileNameDate.replace(".", "-");
			System.out.println("strFileNameDate:" + strFileNameDate);

			String textBody = article.select("#textBody").outerHtml();
			textBody = textBody.replaceAll("src=\"//", "src=\"http://");
			// System.out.println("textBody:"+textBody);
			Document textBodyDoc = Jsoup.parse(textBody);
			System.out.println(textBodyDoc.select("div").get(0));

			textBodyDoc.select("div").get(0).attr("style", "font-size:11pt");
			textBodyDoc.select(".lmbox1").attr("style", "font-size:10pt;color:gray;");
			String strContent = textBodyDoc.html();
			System.out.println("strContent:" + strContent);

			String copyright = article.select(".copyright").outerHtml();
			strContent = strContent + copyright;

			strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);
			

			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox(strMyComment));

			sb1.append("<div style='width:741px'>\r\n");

			sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
			sb1.append("<h2 id='title'>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
			sb1.append(subtitle + "<br>\r\n");
			sb1.append(strAuthor + "<br>\r\n");
			sb1.append(strDate + "<br>\r\n");
			sb1.append(strContent + "<br>\r\n");

			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");
			// System.out.println(sb1.toString());

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
