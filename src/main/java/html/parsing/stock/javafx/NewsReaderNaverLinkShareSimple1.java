/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock.javafx;

import static html.parsing.stock.util.NaverUtil.getNaverBlogLinkSharePage;

import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.model.StockVO;
import html.parsing.stock.news.News;
import html.parsing.stock.news.NewsPublisher;
import html.parsing.stock.util.DataSort.StockNameAscCompare2;
import html.parsing.stock.util.GlobalVariables;
import html.parsing.stock.util.NaverUtil;
import html.parsing.stock.util.StockUtil;

/**
 *
 * @author banks
 */
public class NewsReaderNaverLinkShareSimple1 extends javax.swing.JFrame {

	String strBlogId = "";
	String strNidAut = "";
	String strNidSes = "";

	/**
	 *
	 */
	private static final long serialVersionUID = 5002886789046787575L;
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(NewsReaderNaverLinkShareSimple1.class);
	URI uri = null;

	/**
	 * Creates new form NewJFrame1
	 */
	public NewsReaderNaverLinkShareSimple1() {
		initComponents();
		initList();
	}

	void initList() {
		String kospiFileName = GlobalVariables.KOSPI_LIST_TXT;
		String kosdaqFileName = GlobalVariables.KOSDAQ_LIST_TXT;
		List<StockVO> kospiStockList = new ArrayList<StockVO>();
		List<StockVO> kosdaqStockList = new ArrayList<StockVO>();

		try {
			StockUtil.readStockCodeNameListFromTxtFile(kospiStockList, kospiFileName);
			Collections.sort(kospiStockList, new StockNameAscCompare2());
			StockUtil.readStockCodeNameListFromTxtFile(kosdaqStockList, kosdaqFileName);
			Collections.sort(kosdaqStockList, new StockNameAscCompare2());
		} catch (Exception e) {
			try {
				StockUtil.getStockCodeNameListFromKindKrxCoKr("stockMkt");
				Collections.sort(kospiStockList, new StockNameAscCompare2());
				StockUtil.getStockCodeNameListFromKindKrxCoKr("kosdaqMkt");
				Collections.sort(kosdaqStockList, new StockNameAscCompare2());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		String kospis[] = new String[kospiStockList.size()];
		String kosdaqs[] = new String[kosdaqStockList.size()];

		for (int i = 0; i < kospiStockList.size(); i++) {
			StockVO vo = kospiStockList.get(i);
			// System.out.println("코스피 증권코드:" + vo.getStockCode() + " 증권명:" +
			// vo.getStockName());
			kospis[i] = vo.getStockCode() + ", " + vo.getStockName();
		}
		for (int i = 0; i < kosdaqStockList.size(); i++) {
			StockVO vo = kosdaqStockList.get(i);
			// System.out.println("코스닥 증권코드:" + vo.getStockCode() + " 증권명:" +
			// vo.getStockName());
			kosdaqs[i] = vo.getStockCode() + ", " + vo.getStockName();
		}

	}

	private static void open(URI uri) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException e) {
				/* TODO: error handling */ }
		} else {
			/* TODO: error handling */ }
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		buttonGroup1 = new javax.swing.ButtonGroup();
		jPanel7 = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel();
		urlLbl = new javax.swing.JLabel();
		textFieldPopupMenuPanel1 = new html.parsing.stock.TextFieldPopupMenuPanel();
		jPanel9 = new javax.swing.JPanel();
		executeBtn = new javax.swing.JButton();
		extractImgBtn = new javax.swing.JButton();
		initContainers = new javax.swing.JButton();
		jPanel5 = new javax.swing.JPanel();
		jPanel6 = new javax.swing.JPanel();
		filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(25, 0), new java.awt.Dimension(25, 0),
				new java.awt.Dimension(25, 32767));
		jScrollPane4 = new javax.swing.JScrollPane();
		extractedUrlTextPane = new javax.swing.JTextPane();
		jPanel8 = new javax.swing.JPanel();
		jPanel37 = new javax.swing.JPanel();
		jPanel10 = new javax.swing.JPanel();
		jLabel19 = new javax.swing.JLabel();
		jScrollPane7 = new javax.swing.JScrollPane();
		nidAutTa = new javax.swing.JTextArea();
		jPanel38 = new javax.swing.JPanel();
		delNidAutBtn = new javax.swing.JButton();
		jPanel39 = new javax.swing.JPanel();
		jLabel21 = new javax.swing.JLabel();
		jScrollPane8 = new javax.swing.JScrollPane();
		nidSesTa = new javax.swing.JTextArea();
		jPanel40 = new javax.swing.JPanel();
		delNidSesBtn = new javax.swing.JButton();
		jPanel11 = new javax.swing.JPanel();
		jPanel13 = new javax.swing.JPanel();
		jLabel20 = new javax.swing.JLabel();
		jPanel12 = new javax.swing.JPanel();
		categoryListComboBox = new javax.swing.JComboBox<>();
		jPanel14 = new javax.swing.JPanel();
		extractCategoryBtn = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("웹페이지 추출");
		setMinimumSize(new java.awt.Dimension(500, 250));
		setSize(new java.awt.Dimension(1000, 1000));

		jPanel7.setLayout(new java.awt.BorderLayout());

		jPanel4.setLayout(new java.awt.BorderLayout());

		urlLbl.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		urlLbl.setText("주소 : ");
		urlLbl.setToolTipText("");
		jPanel4.add(urlLbl, java.awt.BorderLayout.WEST);

		textFieldPopupMenuPanel1.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				textFieldPopupMenuPanel1FocusLost(evt);
			}
		});
		textFieldPopupMenuPanel1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
			public void propertyChange(java.beans.PropertyChangeEvent evt) {
				textFieldPopupMenuPanel1PropertyChange(evt);
			}
		});
		jPanel4.add(textFieldPopupMenuPanel1, java.awt.BorderLayout.CENTER);

		executeBtn.setText("페이지 추출");
		executeBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				executeBtnActionPerformed(evt);
			}
		});
		jPanel9.add(executeBtn);

		extractImgBtn.setText("이미지추출");
		extractImgBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				extractImgBtnActionPerformed(evt);
			}
		});
		jPanel9.add(extractImgBtn);

		initContainers.setText("초기화");
		initContainers.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				initContainersActionPerformed(evt);
			}
		});
		jPanel9.add(initContainers);

		jPanel4.add(jPanel9, java.awt.BorderLayout.EAST);

		jPanel5.setLayout(new java.awt.BorderLayout());

		jPanel6.add(filler1);

		jPanel5.add(jPanel6, java.awt.BorderLayout.WEST);

		extractedUrlTextPane.setEditable(false);
		jScrollPane4.setViewportView(extractedUrlTextPane);

		jPanel5.add(jScrollPane4, java.awt.BorderLayout.CENTER);

		jPanel4.add(jPanel5, java.awt.BorderLayout.PAGE_END);

		jPanel7.add(jPanel4, java.awt.BorderLayout.SOUTH);

		getContentPane().add(jPanel7, java.awt.BorderLayout.NORTH);

		jPanel37.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		jPanel37.setMinimumSize(new java.awt.Dimension(1000, 300));
		jPanel37.setPreferredSize(new java.awt.Dimension(1000, 250));
		jPanel37.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

		jPanel10.setPreferredSize(new java.awt.Dimension(990, 24));
		jPanel10.setLayout(new java.awt.BorderLayout());

		jLabel19.setText("NID_AUT");
		jLabel19.setPreferredSize(new java.awt.Dimension(100, 15));
		jPanel10.add(jLabel19, java.awt.BorderLayout.WEST);

		jScrollPane7.setPreferredSize(new java.awt.Dimension(500, 24));

		nidAutTa.setColumns(20);
		nidAutTa.setLineWrap(true);
		nidAutTa.setRows(1);
		nidAutTa.setPreferredSize(new java.awt.Dimension(500, 24));
		jScrollPane7.setViewportView(nidAutTa);

		jPanel10.add(jScrollPane7, java.awt.BorderLayout.CENTER);

		delNidAutBtn.setText("삭제");
		delNidAutBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				delNidAutBtnActionPerformed(evt);
			}
		});
		jPanel38.add(delNidAutBtn);

		jPanel10.add(jPanel38, java.awt.BorderLayout.EAST);

		jPanel37.add(jPanel10);

		jPanel39.setPreferredSize(new java.awt.Dimension(990, 130));
		jPanel39.setLayout(new java.awt.BorderLayout());

		jLabel21.setText("NID_SES");
		jLabel21.setPreferredSize(new java.awt.Dimension(100, 15));
		jPanel39.add(jLabel21, java.awt.BorderLayout.WEST);

		jScrollPane8.setPreferredSize(new java.awt.Dimension(500, 0));

		nidSesTa.setColumns(20);
		nidSesTa.setLineWrap(true);
		nidSesTa.setRows(7);
		jScrollPane8.setViewportView(nidSesTa);

		jPanel39.add(jScrollPane8, java.awt.BorderLayout.CENTER);

		delNidSesBtn.setText("삭제");
		delNidSesBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				delNidSesBtnActionPerformed(evt);
			}
		});
		jPanel40.add(delNidSesBtn);

		jPanel39.add(jPanel40, java.awt.BorderLayout.EAST);

		jPanel37.add(jPanel39);

		jPanel11.setPreferredSize(new java.awt.Dimension(990, 100));
		jPanel11.setLayout(new java.awt.BorderLayout());

		jLabel20.setText("Naver Category");
		jPanel13.add(jLabel20);

		jPanel11.add(jPanel13, java.awt.BorderLayout.WEST);

		jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

		categoryListComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "266:쿠팡 상품 추천", "267:로켓배송",
				"268:로켓프레시", "269:로켓직구", "270:정기배송", "271:골드박스", "272:기획전", "274:카테고리별 베스트 상품", "275:PL 상품",
				"276:PL 브랜드별 상품", "277:추천 상품", "33:소개, 알림, 공지", "173:유행, 트렌드, 동향", "255:역사", "88:사회,문화", "198:국정교과서",
				"216:혼이비정상", "31:정치, 정부, 정책", "180:선거", "7:국외, 해외, 국제, 세계", "249:북한", "236:미국", "228:중국", "237:일본",
				"2:경제, 산업", "256:삼성", "260:현대", "141:부동산", "238:가상(암호)화폐", "250:투자썰전", "47:IT(Info Tech)",
				"258:BT(Bio Tech)", "259:NT(Nano Tech)", "199:카페베네", "131:증권", "265:미국", "146:증권↑↓↗↘", "153:특징주",
				"164:신고, 신저가", "235:시간외단일가", "278:증권뉴스", "176:제약,약품, 바이오", "264:IT(Info Tech)", "273:조선", "190:삼성",
				"171:국민연금", "261:ETN, ETF", "188:핸디소프트", "253:Entertainment", "166:외국인 보유", "170:리포트,리서치", "172:상하한일수",
				"148:데이타", "155:Top 100", "159:기외 연속매수", "160:기외 연속매도", "156:기외 거래량", "161:기외 거래대금", "157:기외 양매수금",
				"162:기외 양매수량", "158:기외 양매도금", "163:기외 양매도량", "152:기획기사", "209:방송, 언론", "210:JTBC", "201:뉴스공장",
				"202:파파이스", "206:스포트라이트", "150:건강", "207:치매", "29:비타민", "140:운동", "151:식당", "208:마약", "263:질병",
				"132:Manuka Honey", "9:음식, 식료품", "262:환경", "142:사건, 사고", "182:세월호", "234:4대강", "204:5촌살인사건",
				"241:MeToo", "243:갑질", "244:댓글사건", "121:오늘의 잠언", "177:오늘의 계시", "128:오늘의 성경", "120:오늘의 말씀", "149:오늘의 사진",
				"123:오늘의 영어", "178:주일, 수요말씀", "245:인물", "197:문재인대통령", "189:노무현대통령", "225:인물1", "179:이승만", "183:박정희",
				"240:이명박", "185:박근혜", "193:이재명", "191:김기춘", "186:최태민", "200:김재규", "184:최순실", "229:장준하", "192:역사",
				"147:브렉시트", "145:자동차", "174:여행", "관광", "196:레져", "144:신앙", "181:종교", "230:과학", "111:LearningJava, 4Th",
				"94:자바 IO, NIO NetPrg", "50:Node.js 프로그래밍", "70:막힘없이배우는Java프로그래밍", "89:HTML5를 활용한 모바일웹앱",
				"90:1부.HTML5주요기능", "91:2부. jQueryMobile", "92:3부.Sencha Touch", "5:웹 프로그래밍", "127:모바일 프로그래밍",
				"130:모던웹을위한HTML5프로그래밍", "35:연예, 엔터, 재미", "129:해외직구", "32:쇼핑", "135:문화, 예술", "3:음악", "139:미술", "49:영화",
				"6:연예", "8:책", "211:교양", "212:다큐", "213:교육", "46:보안", "24:패션", "37:뷰티", "19:디자인", "114:메르스", "25:생활",
				"10:스포츠", "30:동영상", "69:월남전", "43:영감의 시", "126:천국과지옥", "125:정명석선생님", "137:프로그램", "45:CSS", "87:Eclipse",
				"247:easyui", "93:Google", "44:HTML", "27:JavaScript", "26:Java", "42:jQuery", "248:NetBeans",
				"112:Node.js", "86:Spring", "246:Mybatis", "115:Swing", "39:Thymeleaf", "254:tomcat", "113:Software",
				"36:드라이버", "257:Freemarker", "133:데이터베이스", "41:Oracle", "48:MSSQL", "40:MySQL", "134:운영체제",
				"22:Windows", "21:Unix, Linux", "175:레오사진", "233:광고" }));
		jPanel12.add(categoryListComboBox);

		jPanel11.add(jPanel12, java.awt.BorderLayout.CENTER);

		extractCategoryBtn.setText("카테고리추출");
		extractCategoryBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				extractCategoryBtnActionPerformed(evt);
			}
		});
		jPanel14.add(extractCategoryBtn);

		jPanel11.add(jPanel14, java.awt.BorderLayout.EAST);

		jPanel37.add(jPanel11);

		jPanel8.add(jPanel37);

		getContentPane().add(jPanel8, java.awt.BorderLayout.CENTER);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void delNidAutBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_delNidAutBtnActionPerformed
		// TODO add your handling code here:
		nidAutTa.setText("");
	}// GEN-LAST:event_delNidAutBtnActionPerformed

	private void delNidSesBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_delNidSesBtnActionPerformed
		// TODO add your handling code here:
		nidSesTa.setText("");
	}// GEN-LAST:event_delNidSesBtnActionPerformed

	private void extractCategoryBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_extractCategoryBtnActionPerformed
		// TODO add your handling code here:
		String strSelectedCategory = String.valueOf(categoryListComboBox.getSelectedItem());
		String strSelectedCategoryArray[] = strSelectedCategory.split(":");
		System.out.println("strSelectedCategoryArray[0]-------------->" + strSelectedCategoryArray[0]);
		System.out.println("strSelectedCategoryArray[1]-------------->" + strSelectedCategoryArray[1]);
		createCategoryListCombo();
	}// GEN-LAST:event_extractCategoryBtnActionPerformed

	private void initContainersActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_initContainersActionPerformed
		// TODO add your handling code here:
		textFieldPopupMenuPanel1.getTextField().setText("");
		extractedUrlTextPane.setText("");
	}// GEN-LAST:event_initContainersActionPerformed

	private void extractImgBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_extractImgBtnActionPerformed
		try {
			// TODO add your handling code here:
			url = textFieldPopupMenuPanel1.getTextField().getText();
			Document doc = Jsoup.connect(url).get();
			News gurl = new News();
			gurl.getURL(url);
			String protocol = gurl.getProtocol();
			String host = gurl.getHost();
			String path = gurl.getPath();

			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);

		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		} finally {
			System.out.println("이미지 추출 완료");
		}

	}// GEN-LAST:event_extractImgBtnActionPerformed

	private void executeBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_executeBtnActionPerformed
		try {
			extractedUrlTextPane.setText("");
			Thread.sleep(1000);
			createHTMLFile();
		} catch (InterruptedException ex) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
	}// GEN-LAST:event_executeBtnActionPerformed

	private void textFieldPopupMenuPanel1PropertyChange(java.beans.PropertyChangeEvent evt) {// GEN-FIRST:event_textFieldPopupMenuPanel1PropertyChange
		textFieldPopupMenuPanel1.getTextField().addFocusListener(new java.awt.event.FocusAdapter() {
			@Override
			public void focusLost(java.awt.event.FocusEvent evt) {
				jTextField1FocusLost(evt);
			}
		});
		textFieldPopupMenuPanel1.getTextField().addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jTextField1ActionPerformed(evt);
			}
		});
	}// GEN-LAST:event_textFieldPopupMenuPanel1PropertyChange

	private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
		logger.debug("jTextField1ActionPerformed");
		String url = textFieldPopupMenuPanel1.getTextField().getText();
		logger.debug("url :" + url);
		if (StringUtils.defaultString(url).equals("")) {
			return;
		}
		;
		try {
			extractedUrlTextPane.setText("");
			Thread.sleep(1000);
			createHTMLFile();
		} catch (InterruptedException ex) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {
		logger.debug("jTextField1FocusLost");
		String url = textFieldPopupMenuPanel1.getTextField().getText();
		logger.debug("url :" + url);
		if (StringUtils.defaultString(url).equals("")) {
			return;
		}
		;
		try {
			extractedUrlTextPane.setText("");
			Thread.sleep(1000);
			createHTMLFile();
		} catch (InterruptedException ex) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void textFieldPopupMenuPanel1FocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_textFieldPopupMenuPanel1FocusLost
		logger.debug("textFieldPopupMenuPanel1FocusLost");
	}// GEN-LAST:event_textFieldPopupMenuPanel1FocusLost

	public void createHTMLFile() {
		String newsCompany = getSelectedButtonText(buttonGroup1);
		System.out.println("newsCompany1:" + newsCompany);
		url = textFieldPopupMenuPanel1.getTextField().getText();
		if (!url.equals("")) {
			if (newsCompany != null) {
				createHTMLFile(url, newsCompany);
			} else {
				// JOptionPane.showMessageDialog(null, "신문명을 선택하여 주세요.");
				createHTMLFile(url);
			}
		}
	}

	private void createHTMLFile(String strUrl) {
		createHTMLFile(strUrl, "");
	}

	private void createHTMLFile(String strUrl, String strMyComment) {
		if (strUrl.equals("")) {
			return;
		}
		System.out.println("url:" + strUrl);
		// 페이지 추출 완료 라벨 초기화
		extractedUrlTextPane.setText("");
		// tab2에서 페이지 이동
		String newsCompany = "";
		int idx = 0;
		for (NewsPublisher np : NewsPublisher.values()) {
			String newsPublisherDomain = np.getName();
			idx = np.ordinal();
			if (strUrl.contains(newsPublisherDomain)) {
				System.out.println("idx:" + idx + " newsPublisherDomain:" + newsPublisherDomain);
				System.out.println("주소가 일치합니다. idx:" + idx);
				newsCompany = np.toString();
				System.out.println("newsCompany2:" + newsCompany);
				break;
			}
		}
		StringBuilder sb = new StringBuilder();

		if (newsCompany.equals("")) {
			textFieldPopupMenuPanel1.getTextField().setText("");
			extractedUrlTextPane.setText(strUrl + " 추출실패");
			return;
		}

		Class<?> c;
		try {
			c = Class.forName("html.parsing.stock.news." + newsCompany);
			System.out.println("Class Name:" + c.getName());
			System.out.println("url:" + strUrl);
			// c.getDeclaredMethods()[0].invoke(object, Object... MethodArgs );
//			Method method = c.getDeclaredMethod("createHTMLFile", String.class);
//			sb = (StringBuilder) method.invoke(String.class, new Object[]{url});
			Method method = c.getDeclaredMethod("createHTMLFile", String.class, String.class);
			sb = (StringBuilder) method.invoke(String.class, new Object[] { strUrl, strMyComment });
			java.util.logging.Logger.getLogger(NewsReaderNaverLinkShareSimple1.class.getName()).log(Level.INFO,
					sb.toString());
		} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException ex) {
			java.util.logging.Logger.getLogger(NewsReaderNaverLinkShareSimple1.class.getName()).log(Level.SEVERE, null,
					ex);
		}

		Document htmlDoc = Jsoup.parse(sb.toString());
		logger.debug("htmlDoc:" + htmlDoc.html());

		htmlDoc.select("meta").remove();
		sb.delete(0, sb.length());
		sb.setLength(0);
		textFieldPopupMenuPanel1.getTextField().setText("");
		extractedUrlTextPane.setText(strUrl + " 페이지 추출 완료");

		String strCategoryNo = "33";
		String strCategoryName = "증권";
		String strSelectedCategory = String.valueOf(categoryListComboBox.getSelectedItem());
		String strSelectedCategoryArray[] = strSelectedCategory.split(":");
		if (strSelectedCategoryArray.length > 0) {
			strCategoryNo = strSelectedCategoryArray[0];
			strCategoryName = strSelectedCategoryArray[1];
			System.out.println("strSelectedCategoryArray[0]-------------->" + strSelectedCategoryArray[0]);
			System.out.println("strSelectedCategoryArray[1]-------------->" + strSelectedCategoryArray[1]);
		}

		String strShareTitle = htmlDoc.select("h2#title").text();
		String strShareUrl = htmlDoc.select("a").first().attr("href");
		StringBuilder contentSb = new StringBuilder();
		contentSb.append(htmlDoc.html());
		contentSb.toString();

		logger.debug("strShareTitle:" + strShareTitle);
		logger.debug("strShareUrl:" + strShareUrl);

		naverBlogLinkShare(contentSb, strCategoryName, strShareTitle, strShareUrl);
	}

	public String getSelectedButtonText(ButtonGroup buttonGroup) {
		for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();

			if (button.isSelected()) {
				return button.getActionCommand();
			}
		}
		return null;
	}

	private void urlTfActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_urlTfActionPerformed

	}// GEN-LAST:event_urlTfActionPerformed

	private void urlTfKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_urlTfKeyReleased
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			// SwingUtilities.getWindowAncestor(evt.getComponent()).dispose();
			createHTMLFile();
		}
	}// GEN-LAST:event_urlTfKeyReleased

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
		// (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(NewsReaderNaverLinkShareSimple1.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		}

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new NewsReaderNaverLinkShareSimple1().setVisible(true);
			}
		});
	}

	/**
	 * strBlogId = null, NaverUtil.naverBlogLinkShare 오류 임시 처방
	 * 
	 * @param contentSb
	 * @param strCategoryName
	 * @param strShareTitle
	 * @param strShareUrl
	 */
	public void naverBlogLinkShare(StringBuilder contentSb, String strCategoryName, String strShareTitle,
			String strShareUrl) {
		strNidAut = nidAutTa.getText();
		strNidSes = nidSesTa.getText();
		NaverUtil.naverBlogLinkShare(strBlogId, strNidAut, strNidSes, strShareUrl, strShareTitle, strCategoryName,
				contentSb, rootPane);
	}

	public void createCategoryListCombo() {
		strNidAut = nidAutTa.getText();
		strNidSes = nidSesTa.getText();
		if (strNidAut.equals("") || strNidSes.equals("")) {
			return;
		}
		Document linkSharePageDoc = getNaverBlogLinkSharePage(strNidAut, strNidSes);
		Elements categoryListOptionEls = linkSharePageDoc.select("#_categoryList option");
		System.out.println("categoryListOptionEls.size :" + categoryListOptionEls.size());
		String[] categoryListOptionArray = new String[categoryListOptionEls.size()];
		System.out.println("categoryListOptionArray.length :" + categoryListOptionArray.length);
		int idx = 0;
		for (Element categoryListOption : categoryListOptionEls) {
			String categoryListOptionTxt = categoryListOption.text();
			String categoryListOptionValue = categoryListOption.attr("value");
			String optionValue = categoryListOptionValue + ":" + categoryListOptionTxt;
			System.out.println("optionValue :" + optionValue);
			categoryListOptionArray[idx] = optionValue;
			idx++;
		}
	}

	private String url = "http://news.mt.co.kr/newsPrint.html?no=2017051716214167566";
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.ButtonGroup buttonGroup1;
	private javax.swing.JComboBox<String> categoryListComboBox;
	private javax.swing.JButton delNidAutBtn;
	private javax.swing.JButton delNidSesBtn;
	private javax.swing.JButton executeBtn;
	private javax.swing.JButton extractCategoryBtn;
	private javax.swing.JButton extractImgBtn;
	private javax.swing.JTextPane extractedUrlTextPane;
	private javax.swing.Box.Filler filler1;
	private javax.swing.JButton initContainers;
	private javax.swing.JLabel jLabel19;
	private javax.swing.JLabel jLabel20;
	private javax.swing.JLabel jLabel21;
	private javax.swing.JPanel jPanel10;
	private javax.swing.JPanel jPanel11;
	private javax.swing.JPanel jPanel12;
	private javax.swing.JPanel jPanel13;
	private javax.swing.JPanel jPanel14;
	private javax.swing.JPanel jPanel37;
	private javax.swing.JPanel jPanel38;
	private javax.swing.JPanel jPanel39;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel40;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JPanel jPanel7;
	private javax.swing.JPanel jPanel8;
	private javax.swing.JPanel jPanel9;
	private javax.swing.JScrollPane jScrollPane4;
	private javax.swing.JScrollPane jScrollPane7;
	private javax.swing.JScrollPane jScrollPane8;
	private javax.swing.JTextArea nidAutTa;
	private javax.swing.JTextArea nidSesTa;
	private html.parsing.stock.TextFieldPopupMenuPanel textFieldPopupMenuPanel1;
	private javax.swing.JLabel urlLbl;
	// End of variables declaration//GEN-END:variables
}
