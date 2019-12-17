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
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import html.parsing.stock.util.FileUtil;
import html.parsing.stock.StockUtil;

public class NewsTomatoCom extends javax.swing.JFrame {

    java.util.logging.Logger logger = null;
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

    public NewsTomatoCom(int i) {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        logger.log(Level.INFO, this.getClass().getSimpleName());
        String url = JOptionPane.showInputDialog("URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (url.equals("")) {
            url = "http://www.newstomato.com/ReadNews.aspx?no=819904";
        }
        createHTMLFile(url);
    }

    public NewsTomatoCom(String url) {
        logger = java.util.logging.Logger.getLogger(this.getClass().getName());
        logger.log(Level.INFO, new Date() + "");
        logger.log(Level.SEVERE, "url:" + url);
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
            @Override
			public void run() {
                new NewsTomatoCom().setVisible(true);
            }
        });
    }

    public NewsTomatoCom() {
        logger = java.util.logging.Logger.getLogger(this.getClass().getName());
        logger.log(Level.INFO, new Date() + "");
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

    public static StringBuilder createHTMLFile(String url) {

        News gurl = new News();
        gurl.getURL(url);
        String protocol = gurl.getProtocol();
        String host = gurl.getHost();
        String path = gurl.getPath();
        String protocolHost = gurl.getProtocolHost();

        StringBuilder sb1 = new StringBuilder();
        Document doc = null;
        String strTitleForFileName;
        FileWriter fw;
        try {
            doc = Jsoup.connect(url).get();
            System.out.println("doc:[" + doc + "]");

//            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
//            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
//            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
//            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

            doc.select("iframe").remove();
            doc.select("script").remove();
            doc.select("#tomatoAdIframe_0").remove();
            doc.select("#ad_wrap_2_10").remove();
            doc.select(".rns_controll").remove();
            doc.select(".carousel_wrap").remove();
            doc.select("#ctl00_ContentPlaceHolder1_WebNewsView_lblHTS").remove();
            doc.select("#ctl00_ContentPlaceHolder1_WebNewsView_hyHTS").remove();
            doc.select("#ctl00_ContentPlaceHolder1_WebNewsView_NewsReporterSns").remove();

            String fileName2 = userHome + File.separator + "documents" + File.separator + strYMD + ".html";
            System.out.println("fileName2:" + fileName2);
            Writer bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName2, true), StandardCharsets.UTF_8));
            bw.write(doc.html());
            bw.close();

            Elements title = doc.select("div.rn_stitle");
            System.out.println("title:" + strTitle);
            if (title != null && title.size() > 0) {
                strTitle = title.get(0).text();
            } else {
                strTitle = doc.select(".cc_title").text();
            }
            System.out.println("title:" + strTitle);
            strTitleForFileName = strTitle;
            strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
            System.out.println("strTitleForFileName:" + strTitleForFileName);

            Elements subTitle = doc.select("div.rn_sstitle");
            String strSubTitle = "";
            if (subTitle.size() > 0) {
                strSubTitle = subTitle.outerHtml();
            }

            String strAuthor = doc.select(".author").html();
            if (strAuthor.equals("")) {
                System.out.println(doc.select("#cc_textarea div"));
                System.out.println(doc.select("#cc_textarea div").last());
                Element authorElement = doc.select("#cc_textarea div").last();
                if (authorElement != null) {
                    strAuthor = authorElement.text();
                }
            }
            System.out.println("strAuthor:[" + strAuthor + "]");

            Elements rn_sdates = doc.select(".rn_sdate");
            System.out.println("rn_sdates:[" + rn_sdates + "]");
            Element rn_sdate = null;
            if (rn_sdates.size() > 0) {
                rn_sdate = rn_sdates.get(0);
                List<Node> childNodes = rn_sdate.childNodes();
                int childNodeSize = rn_sdate.childNodeSize();
                System.out.println("childNodeSize:" + childNodeSize);
                for (Node n : childNodes) {
                    String nodehtml = n.toString();
                    System.out.println("nodehtml:" + nodehtml);
                }
                strDate = rn_sdate.childNode(0).toString().trim();
                strDate = strDate.replaceAll("입력 : ", "").trim();
                System.out.println("strDate:[" + strDate + "]");
            } else {
                rn_sdate = doc.select(".cc_date").get(0);
                strDate = rn_sdate.text();
            }

            String strFileNameDate = strDate;
            System.out.println("strFileNameDate1:" + strFileNameDate);
            strFileNameDate = StockUtil.getDateForFileName(strDate);
            System.out.println("strFileNameDate2:" + strFileNameDate);

            Elements contentElements = doc.select(".rns_text");
            contentElements.select(".ct_controll").remove();
            Elements divElements = contentElements.select("div");
            for (Element div : divElements) {
                System.out.println("div.text:" + div.text());
                System.out.println("div.parent:" + div.parent());
                //div.parent().text(div.text());
                Element node = doc.createElement("span");
                node.text(div.text());
                div.replaceWith(node);
            }
            String strContent = "";
            if (!contentElements.text().equals("")) {
                strContent = contentElements.outerHtml();
            } else {
                contentElements = doc.select("#cc_textarea");
                contentElements.select(".ct_controll").remove();
                Element br = doc.createElement("br");
                Elements divs = contentElements.select("div");
                for (Element div : divs) {
                    div.removeAttr("style");
                    div.before(br);
                    div.after(br);
                }
                strContent = contentElements.outerHtml();
            }
            strContent = strContent.replaceAll("<div>[\r\n]*[ ]*&nbsp;[\r\n]*[ ]*</div>", "<br/>");
            System.out.println("strContent:[" + strContent + "]");
			strContent = StockUtil.makeStockLinkStringByExcel(strContent);

            String copyright = doc.select(".desc").html();

            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");

            sb1.append(StockUtil.getMyCommentBox());

            sb1.append("<div style='width:548px'>\r\n");

            sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
            sb1.append("<h1>[" + strDate + "]" + strTitle + "</h1><br>\r\n");
            sb1.append(strSubTitle + "<br>\r\n");
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

            System.out.println("fileName1:" + userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html");
            String fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
            FileUtil.fileWrite(fileName, sb1.toString());

            System.out.println("fileName2:" + userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html");
            fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
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
