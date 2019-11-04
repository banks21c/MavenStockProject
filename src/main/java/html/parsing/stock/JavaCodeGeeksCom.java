package html.parsing.stock;

import html.parsing.stock.news.NewsReader;
import html.parsing.stock.news.News;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JavaCodeGeeksCom extends javax.swing.JFrame {

    java.util.logging.Logger logger = null;
    final static String userHome = System.getProperty("user.home");

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";
    static String strDate = null;
    static String strTitle = null;
    static String strDefaultUrl = "https://examples.javacodegeeks.com/enterprise-java/spring/batch/quartz-spring-batch-example/";

    DecimalFormat df = new DecimalFormat("###.##");

    private javax.swing.JButton executeBtn;
    private javax.swing.JButton eraseBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel urlLbl;
    private javax.swing.JTextField urlTf;
    private javax.swing.JPanel executeResultPnl;
    private static javax.swing.JLabel executeResultLbl;

    JavaCodeGeeksCom(int i) {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        logger.log(Level.INFO, this.getClass().getSimpleName());
        String url = JOptionPane.showInputDialog("URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (url.equals("")) {
            url = strDefaultUrl;
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewsReader.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JavaCodeGeeksCom().setVisible(true);
            }
        });
    }

    public JavaCodeGeeksCom() {
        logger = java.util.logging.Logger.getLogger(this.getClass().getName());
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
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                urlTfActionPerformed(evt);
            }
        });
        urlTf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                urlTfKeyReleased(evt);
            }
        });
        jPanel2.add(urlTf, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(executeResultPnl, java.awt.BorderLayout.EAST);

        eraseBtn.setText("지우기");
        eraseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eraseBtnActionPerformed(evt);
            }
        });
        jPanel1.add(eraseBtn);

        executeBtn.setText("페이지 추출");
        executeBtn.addActionListener(new java.awt.event.ActionListener() {
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
        System.out.println("url:" + url);
        if (url != null && !url.equals("")) {
            createHTMLFile(url);
        } else {
            createHTMLFile(strDefaultUrl);
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
        Document doc;
        String strTitleForFileName;
        try {
            doc = Jsoup.connect(url).userAgent("Mozilla").get();

            System.out.println("doc:" + doc);
            doc.select("iframe").remove();
            doc.select("script").remove();
            doc.select(".e3lan-post").remove();
            doc.select("#mc4wp-form-1").remove();

            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

            Elements title = doc.select("article h1");
            strTitle = title.get(0).text();
            System.out.println("title:" + strTitle);
            strTitleForFileName = strTitle;
            strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
            System.out.println("strTitleForFileName:" + strTitleForFileName);

            Elements authorElements = doc.select("#author-box");
            String authorHtml = "";
            if (authorElements.size() > 0) {
                authorHtml = authorElements.get(0).outerHtml();
            }
            System.out.println("authorHtml:[" + authorHtml + "]");

            String strDate = "";
            System.out.println("strDate:[" + strDate + "]");
            String strFileNameDate = strDate;
            strFileNameDate = StockUtil.getDateForFileName(strDate);
            System.out.println("strFileNameDate:" + strFileNameDate);

            Element content = doc.select("article#the-post").get(0);
            String textBody = content.outerHtml();
            // System.out.println("textBody:"+textBody);
            Document textBodyDoc = Jsoup.parse(textBody);
            textBodyDoc.select("div").get(0).attr("style", "font-size:11pt");
            textBodyDoc.select(".lmbox1").attr("style", "font-size:10pt;color:gray;");
            String strContent = textBodyDoc.html();
            System.out.println("textBodyHtml:" + strContent);
            try {
				strContent = StockUtil.makeStockLinkString(strContent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            String copyright = content.select(".copyright").outerHtml();

            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");

            sb1.append(StockUtil.getMyCommentBox());

            sb1.append("<div style='width:548px'>\r\n");

            sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
            sb1.append("[" + strDate + "]" + strTitle + "<br>\r\n");
            sb1.append(authorHtml + "<br>\r\n");
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

            System.out.println("fileName1:" + userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html");
            String fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
            FileUtil.fileWrite(fileName, sb1.toString());

            System.out.println("fileName2:" + userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html");
            fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
            FileUtil.fileWrite(fileName, sb1.toString());

        } catch (IOException e) {
            System.out.println("exception msg:" + e.getMessage());
        } finally {
            System.out.println("추출완료");
            if (executeResultLbl != null) {
                executeResultLbl.setText("추출완료");
            }
        }
        return sb1;
    }

}