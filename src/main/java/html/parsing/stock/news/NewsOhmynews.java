package html.parsing.stock.news;

import html.parsing.stock.FileUtil;
import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.StockUtil;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NewsOhmynews extends javax.swing.JFrame {

    java.util.logging.Logger logger = null;
    final static String userHome = System.getProperty("user.home");

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    static String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E HH.mm.ss.SSS", Locale.KOREAN).format(new Date());
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

    NewsOhmynews(int i) {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        logger.log(Level.INFO, this.getClass().getSimpleName());
        String url = JOptionPane.showInputDialog("URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (url.equals("")) {
            url = "http://www.ohmynews.com/NWS_Web/View/mov_pg.aspx#ME000088968";
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
                new NewsOhmynews().setVisible(true);
            }
        });
    }

    public NewsOhmynews() {
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

            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

            System.out.println("fileName2:" + userHome + File.separator + "documents" + File.separator + strYMD + ".html");
            fw = new FileWriter(userHome + File.separator + "documents" + File.separator + strYMD + ".html");
            fw.write(doc.html());
            fw.close();

            Elements title = doc.select(".newstitle a");
            System.out.println("title:" + strTitle);
            if (title != null && title.size() > 0) {
                strTitle = title.get(0).text();
            } else {
                title = doc.select(".tit_subject");
                if (title.isEmpty()) {
                    title = doc.select(".St_tit_subject");
                }
                strTitle = title.get(0).text();
            }
            System.out.println("title:" + strTitle);
            strTitleForFileName = strTitle;
            strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
            System.out.println("strTitleForFileName:" + strTitleForFileName);

            Elements lis = doc.select("#v-left-scroll-in .view_top ul li");
            Elements author = lis.eq(0);
            System.out.println("author:[" + author + "]");
            String author1 = "";
            if (lis.size() > 0) {
                author1 = lis.get(0).text();
                System.out.println("author1:[" + author1 + "]");
            }
            System.out.println("author1:[" + author1 + "]");

            String authorAndTime = doc.select("#v-left-scroll-in .view_top ul").outerHtml();
            System.out.println("authorAndTime:[" + authorAndTime + "]");

            Elements infoData = doc.select(".info_data div");
            System.out.println("infoData:[" + infoData + "]");
            String strDate = "";
            String strAuthor = "";
            if (infoData.size() > 0) {
                Element dateElement = infoData.get(0);
                System.out.println("~~~~~[" + dateElement.childNode(0) + "]");
                dateElement.select("span").remove();
                strDate = dateElement.childNode(0).toString().trim();
                System.out.println("strDate:[" + strDate + "]");
                strAuthor = infoData.get(1).text();
                System.out.println("strAuthor:[" + strAuthor + "]");
            } else {
                strDate = doc.select(".txt_time").text();
            }
            if (strDate == null || strDate.equals("")) {
                String dates = doc.select(".St_info_data div").get(0).text();
                System.out.println("dates====>" + dates);
                StringTokenizer st = new StringTokenizer(dates, "l");
                if (st.hasMoreTokens()) {
                    strDate = st.nextToken();
                }
                System.out.println("strDate====>" + strDate);
            }

            String strFileNameDate = strDate;
            strFileNameDate = StockUtil.getDateForFileName(strDate);
            System.out.println("strFileNameDate:" + strFileNameDate);

            StringBuilder sb = new StringBuilder();
            Elements youtubes = doc.select(".vod");
            if (youtubes != null && youtubes.size() > 0) {
                for (Element youtube : youtubes) {
                    String strYoutubeHtml = youtube.html();
                    sb.append(strYoutubeHtml);
                }
            }
            Elements contents = doc.select(".txt_view");
            Elements content = null;
            String textBody = "";
            if (contents.size() > 0) {
                content = doc.select(".txt_view");
                System.out.println("content:[" + content + "]");
                textBody = content.get(0).outerHtml();
            } else {
                content = doc.select(".article_view");
                if (content.isEmpty()) {
                    content = doc.select(".at_contents");
                    content.select("#content_wrap").remove();
                    content.select("script").remove();
                    content.select("#footer_area").remove();
                    content.select(".sns_scroll").remove();
                    content.select(".add_scroll").remove();
                    content.select("div.add_scroll_rt").remove();
                    content.select("div.add_footer").remove();
                    content.select(".btn_top").remove();
                    content.select(".atc_btns").remove();
                }
                textBody = content.get(0).outerHtml();
            }
            String strContent = sb.toString() + textBody;
            System.out.println("strContent:" + strContent);
			strContent = StockUtil.makeStockLinkStringByExcel(strContent);

            String copyright = doc.select(".copyright").outerHtml();

            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");

            sb1.append(StockUtil.getMyCommentBox());

            sb1.append("<div style='width:548px'>\r\n");

            sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
            sb1.append("[" + strDate + "]" + strTitle + "<br>\r\n");
            sb1.append(authorAndTime + "<br>\r\n");
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
