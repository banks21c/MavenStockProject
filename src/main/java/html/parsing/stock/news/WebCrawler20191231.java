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

import html.parsing.stock.StockUtil;
import html.parsing.stock.util.FileUtil;

public class WebCrawler20191231 extends javax.swing.JFrame {

	private static Logger logger = LoggerFactory.getLogger(WebCrawler20191231.class);
	final static String userHome = System.getProperty("user.home");

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	static String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E HH.mm.ss.SSS", Locale.KOREAN).format(new Date());
	static String strDate = null;
	static String strTitle = null;
	static String strFileNameDate = null;
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
	String inputUrl = "";
	String defaultUrl = "https://www.dailymail.co.uk/femail/article-2945827/Heartwarming-story-father-daughter-posed-photo-spot-35-YEARS.html";

	WebCrawler20191231(int i) {
		logger = LoggerFactory.getLogger(this.getClass());
		logger.debug(this.getClass().getSimpleName());
		inputUrl = JOptionPane.showInputDialog("URL을 입력하여 주세요.");
		logger.debug("url:[" + inputUrl + "]");
		if (inputUrl.equals("")) {
			inputUrl = defaultUrl;
		}
		createHTMLFile(inputUrl);
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
				new WebCrawler20191231().setVisible(true);
			}
		});
	}

	public WebCrawler20191231() {
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
		} else {
			logger.debug("기본 url 추출");
			createHTMLFile(defaultUrl);
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
		News gurl = new News();
		gurl.getURL(url);
		String protocol = gurl.getProtocol();
		String host = gurl.getHost();
		String path = gurl.getPath();
		String protocolHost = gurl.getProtocolHost();

		StringBuilder sb1 = new StringBuilder();
		StringBuilder temp = new StringBuilder();

		sb1.append("<html lang='ko'>\r\n");
		sb1.append("<head>\r\n");
		//sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
		sb1.append("</head>\r\n");
		sb1.append("<body>\r\n");
		Document doc = null;
		try {
			logger.debug("url:" + url);
			doc = Jsoup.connect(url)
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
					.header("Accept-Language", "en").header("Accept-Encoding", "gzip,deflate,sdch").get();

			Elements imgs = doc.select("img");
			String imgWidth = "";
			String imgHeight = "";
			int iImgWidth = 0;
			int iImgHeight = 0;
			for (Element img : imgs) {
				imgWidth = img.attr("width");
				imgHeight = img.attr("height");
				System.out.println("imgWidth:" + imgWidth);
				System.out.println("imgHeight:" + imgHeight);
				if(imgWidth.equals("")) {
					continue;
				}
				if(imgHeight.equals("")) {
					continue;
				}
				iImgWidth = Integer.parseInt(imgWidth);
				iImgHeight = Integer.parseInt(imgHeight);
				if (iImgWidth > 548) {
					iImgWidth = 548;
					iImgHeight = (548 * iImgHeight) / iImgWidth;
				}
				imgWidth = iImgWidth + "";
				imgHeight = iImgHeight + "";

				img.attr("width", imgWidth);
				img.attr("height", imgHeight);
			}

			String title = doc.select("title").text();
			if (title != null && !title.equals("")) {
				sb1.append("<h3>" + doc.select("title") + "</h3>\r\n");
			}
			Elements articleBody = doc.select("div[itemprop = articleBody]");
			articleBody.select("img").removeAttr("src");
			sb1.append(articleBody.html().replace("data-src", "src"));

			String strContent = "";
			strContent = sb1.toString();
			strContent = StockUtil.makeStockLinkStringByExcel(strContent);

			sb1.append(strContent);
			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");

			String fileName = userHome + File.separator + "documents" + File.separator + strTitle + "_" + strYMD
					+ ".html";
			logger.debug("fileName2:" + fileName);
			FileUtil.fileWrite(fileName, sb1.toString());

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
