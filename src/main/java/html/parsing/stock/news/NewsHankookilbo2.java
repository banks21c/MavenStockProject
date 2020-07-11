package html.parsing.stock.news;

import java.awt.event.KeyEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
import html.parsing.stock.util.StockUtil;

/**
 *
 * @author banks
 */
public class NewsHankookilbo2 extends javax.swing.JFrame {

    private static Logger logger = LoggerFactory.getLogger(NewsHankookilbo2.class);
    final static String userHome = System.getProperty("user.home");
    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";
    static String strDate = null;
    static String strTitle = null;

    DecimalFormat df = new DecimalFormat("###.##");

    /**
     * Creates new form NewJFrame1
     */
    public NewsHankookilbo2() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        urlLbl = new javax.swing.JLabel();
        urlTf = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        executeBtn = new javax.swing.JButton();

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
    }// </editor-fold>

    private void executeBtnActionPerformed(java.awt.event.ActionEvent evt) {
        url = urlTf.getText();
        if (url != null && !url.equals("")) {
            createHTMLFile(url);
        }
    }

    private void urlTfActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void urlTfKeyReleased(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            // SwingUtilities.getWindowAncestor(evt.getComponent()).dispose();
            url = urlTf.getText();
            if (url != null && !url.equals("")) {
                createHTMLFile(url);
            }
        }
    }

    private StringBuilder createHTMLFile(String url) {


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
            doc.select("iframe").remove();
            doc.select("script").remove();

            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

            strTitle = doc.select(".titGroup h4").text();
            System.out.println("title:" + strTitle);
            strTitleForFileName = strTitle;
            strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
            System.out.println("strTitleForFileName:" + strTitleForFileName);

            Element timeElement = doc.select(".writeOption p").get(0);
            System.out.println("time html:" + timeElement);

            strDate = timeElement.childNode(0).toString()
                    .substring(timeElement.childNode(0).toString().indexOf(":") + 1).trim();
            System.out.println("strDate:" + strDate);
            strFileNameDate = strDate;

            strFileNameDate = StockUtil.getDateForFileName(strDate);
            System.out.println("strFileNameDate:" + strFileNameDate);

            String dateTime = timeElement.html();
            System.out.println("dateTime:" + dateTime);

            String author = doc.select(".authorInfo .name").text();
            System.out.println("author:" + author);

            Elements article = doc.select(".newsStoryTxt");
            Elements elements = doc.select(".newsStoryPhoto p");
            // rename all 'font'-tags to 'span'-tags, will also keep attributs etc.
            elements.tagName("span");

            // System.out.println("article:["+article+"]");
            String strArticleHTML = doc.select(".newsStoryTxt").outerHtml();
            // System.out.println("strArticleHTML:["+strArticleHTML+"]");
            strArticleHTML = strArticleHTML.replaceAll("<p> </p>", "<br>");
            strArticleHTML = strArticleHTML.replaceAll("<p></p>", "<br><br><p>");
            // System.out.println("strArticleHTML:["+strArticleHTML+"]");
            Document articleDoc = Jsoup.parse(strArticleHTML);
            // System.out.println("articleDoc:["+articleDoc+"]");
            article = articleDoc.select(".newsStoryTxt");
            System.out.println("article:[" + article + "]");
            doc.select("iframe").remove();
            doc.select(".article-ad-align-left").remove();

            article.attr("style", "width:548px");
            article.select("p").attr("style", "font-size:11pt");
            article.select(".newsStoryPhoto span").attr("style", "font-size:10pt;color:metal;background-color:beige;");
            String articleHtml = article.outerHtml();
            System.out.println("articleHtml:" + articleHtml);

            String copyright = doc.select(".copy span").outerHtml();
            System.out.println("copyright:" + copyright);

            String strContent = articleHtml.replaceAll("640px", "548px");
            strContent = strContent.replaceAll("<p></p>", "<br><br><p>");
            strContent = strContent.replaceAll("</article>", "</article><br>");
            strContent = StockUtil.makeStockLinkStringByTxtFile(strContent);

            sb1.append("<!DOCTYPE html>\r\n");
            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            ////sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");

            sb1.append(StockUtil.getMyCommentBox());

            sb1.append("<div style='width:548px'>\r\n");

            sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
            sb1.append("<h2 id='title'>[" + strDate + "] " + strTitle + "</h2>\n");
            sb1.append("<span style='font-size:12px'>" + dateTime + "</span><br><br>\n");
            sb1.append("<span style='font-size:12px'>" + author + "</span><br><br>\n");
            sb1.append(strContent + "<br><br>\n");
            sb1.append("저작권자 © 한국일보 무단전재 및 재배포 금지<br><br>\n");
            sb1.append("Copyright ⓒ The Hankook-Ilbo All rights reserved<br><br>\n");
            sb1.append("</div>\r\n");
            sb1.append("</body>\r\n");
            sb1.append("</html>\r\n");
            System.out.println("sb.toString:" + sb1.toString());

            File dir = new File(userHome + File.separator + "documents" + File.separator + host);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
            FileUtil.fileWrite(fileName, sb1.toString());

            fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
            FileUtil.fileWrite(fileName, sb1.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("추출완료");
            urlTf.setText("");
        }
        return sb1;
    }

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewsHankookilbo2.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        }
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>

        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
			public void run() {
                new NewsHankookilbo2().setVisible(true);
            }
        });
    }

    private String url = "http://news.mt.co.kr/newsPrint.html?no=2017051716214167566";
    // Variables declaration - do not modify
    private javax.swing.JButton executeBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel urlLbl;
    private javax.swing.JTextField urlTf;
    // End of variables declaration
}
