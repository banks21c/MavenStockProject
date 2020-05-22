/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock.news;

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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.LoggerFactory;

import html.parsing.stock.ClassForNameExample;
import html.parsing.stock.DataSort.StockNameAscCompare2;
import html.parsing.stock.GlobalVariables;
import html.parsing.stock.StockUtil;
import html.parsing.stock.StockVO;
import html.parsing.stock.util.FileUtil;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author banks
 */
public class NewsReadAndMakeLink8 extends javax.swing.JFrame {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(NewsReadAndMakeLink8.class);
    final static String userHome = System.getProperty("user.home");
    URI uri = null;

    /**
     * Creates new form NewJFrame1
     */
    public NewsReadAndMakeLink8() {
        initComponents();
        initList();
    }

    void initList() {
        String kospiFileName = GlobalVariables.kospiFileName;
        String kosdaqFileName = GlobalVariables.kosdaqFileName;
        List<StockVO> kospiStockList = new ArrayList<StockVO>();
        List<StockVO> kosdaqStockList = new ArrayList<StockVO>();

        try {
            StockUtil.readStockCodeNameListFromExcel(kospiStockList, kospiFileName);
            Collections.sort(kospiStockList, new StockNameAscCompare2());
            StockUtil.readStockCodeNameListFromExcel(kosdaqStockList, kosdaqFileName);
            Collections.sort(kosdaqStockList, new StockNameAscCompare2());
        } catch (Exception e) {
            try {
                StockUtil.getStockCodeNameListFromKindKrxCoKr(kospiStockList, "stockMkt");
                Collections.sort(kospiStockList, new StockNameAscCompare2());
                StockUtil.getStockCodeNameListFromKindKrxCoKr(kosdaqStockList, "kosdaqMkt");
                Collections.sort(kosdaqStockList, new StockNameAscCompare2());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        String kospis[] = new String[kospiStockList.size()];
        String kosdaqs[] = new String[kosdaqStockList.size()];

        for (int i = 0; i < kospiStockList.size(); i++) {
            StockVO vo = kospiStockList.get(i);
            //System.out.println("코스피 증권코드:" + vo.getStockCode() + " 증권명:" + vo.getStockName());
            kospis[i] = vo.getStockCode() + ", " + vo.getStockName();
        }
        for (int i = 0; i < kosdaqStockList.size(); i++) {
            StockVO vo = kosdaqStockList.get(i);
            //System.out.println("코스닥 증권코드:" + vo.getStockCode() + " 증권명:" + vo.getStockName());
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel7 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        newsTextArea = new javax.swing.JTextArea();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        newsTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("웹페이지 추출");
        setMinimumSize(new java.awt.Dimension(800, 500));
        setSize(new java.awt.Dimension(1000, 600));

        jPanel7.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel7, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.GridLayout(1, 2));

        jSplitPane1.setDividerLocation(300);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 106));

        newsTextArea.setColumns(40);
        newsTextArea.setRows(5);
        jScrollPane1.setViewportView(newsTextArea);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jSplitPane2.setDividerLocation(150);

        jButton1.setText("Create Stock Link =>");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(201, 201, 201)
                .addComponent(jButton1)
                .addContainerGap(207, Short.MAX_VALUE))
        );

        jSplitPane2.setLeftComponent(jPanel2);

        jScrollPane4.setPreferredSize(new java.awt.Dimension(200, 106));

        newsTextArea1.setColumns(20);
        newsTextArea1.setRows(5);
        jScrollPane4.setViewportView(newsTextArea1);

        jSplitPane2.setRightComponent(jScrollPane4);

        jSplitPane1.setRightComponent(jSplitPane2);

        jTabbedPane1.addTab("소스보기", jSplitPane1);

        jPanel1.add(jTabbedPane1);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        String htmlSource = newsTextArea.getText();
        Document doc = Jsoup.parse(htmlSource);
        doc.select("script").remove();
        doc.select("div").removeAttr("class");
        doc.select("span").removeAttr("class");
        doc.select("ul").removeAttr("class");
        doc.select("ol").removeAttr("class");
        doc.select("li").removeAttr("class");
        
        Elements uls = doc.select("ol");
        for (Element ul : uls) {
            Elements lis = ul.select("li");
            for (Element li : lis) {
                Elements spanElements = li.select("span");
                spanElements.get(0).remove();
                spanElements.get(1).remove();
                spanElements.get(3).remove();
            }
        }
        
        Element body = doc.select("body").get(0);

        String htmlTarget = StockUtil.makeStockLinkStringByExcel(body.html());
        newsTextArea1.setText(htmlTarget);

        String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E HH.mm.ss.SSS", Locale.KOREAN).format(new Date());
        StringBuffer sb1 = new StringBuffer();
        sb1.append("<html lang='ko'>\r\n");
        sb1.append("<head>\r\n");
        sb1.append("</head>\r\n");
        sb1.append("<body>\r\n");

        sb1.append(StockUtil.getMyCommentBox());

        sb1.append("<div style='width:548px'>\r\n");

        sb1.append("<h2>[" + strYMD + "] Fortune 500 </h2><br>\r\n");
        sb1.append(htmlTarget + "<br>\r\n");

        sb1.append("</div>\r\n");
        sb1.append("</body>\r\n");
        sb1.append("</html>\r\n");

        String fileName = "";
        fileName = userHome + File.separator + "documents" + File.separator + strYMD + "_fortune.com.html";
        FileUtil.fileWrite(fileName, htmlTarget);

    }//GEN-LAST:event_jButton1MouseClicked

    private void createHTMLFile(String url) {
        if (url.equals("")) {
            return;
        }
        createHTMLFile(url, "");
    }

    private void createHTMLFile(String url, String newsCompany) {
        if (url.equals("")) {
            return;
        }
        System.out.println("url:" + url);
        newsCompany = "";
        int idx = 0;
        for (NewsPublisher np : NewsPublisher.values()) {
            String newsPublisherDomain = np.getName();
            idx = np.ordinal();
            if (url.contains(newsPublisherDomain)) {
                System.out.println("idx:" + idx + " newsPublisherDomain:" + newsPublisherDomain);
                System.out.println("주소가 일치합니다. idx:" + idx);
                newsCompany = np.getPublisherKey(idx);
                System.out.println("newsCompany2:" + newsCompany);
                break;
            }
        }
        System.out.println("newsCompany3:" + newsCompany);

        StringBuilder sb = new StringBuilder();
        if (newsCompany.equals("BUSAN")) {
            sb = NewsBusan.createHTMLFile(url);
        } else if (newsCompany.equals("WwwHanitvCom")) {
            sb = WwwHanitvCom.createHTMLFile(url);
        }
        if (newsCompany.equals("")) {
            return;
        }

        Class<?> c;
        try {
            c = Class.forName("html.parsing.stock.news." + newsCompany);
            System.out.println("Class Name:" + c.getName());
            System.out.println("url:" + url);
            //c.getDeclaredMethods()[0].invoke(object, Object... MethodArgs  );
            Method method = c.getDeclaredMethod("createHTMLFile", String.class);
            sb = (StringBuilder) method.invoke(String.class, new Object[]{url});
            java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.INFO, sb.toString());
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
        }

        Document htmlDoc = Jsoup.parse(sb.toString());
        newsTextArea.setText(htmlDoc.html());

        htmlDoc.select("meta").remove();
        sb.delete(0, sb.length());
        sb.setLength(0);
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
        }
    }// GEN-LAST:event_urlTfKeyReleased
/*
//	private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jRadioButton2ActionPerformed
//		// TODO add your handling code here:
//	}// GEN-LAST:event_jRadioButton2ActionPerformed
//
//	private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jRadioButton1ActionPerformed
//		// TODO add your handling code here:
//	}// GEN-LAST:event_jRadioButton1ActionPerformed
//
//	private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jRadioButton3ActionPerformed
//		// TODO add your handling code here:
//	}// GEN-LAST:event_jRadioButton3ActionPerformed
//
//	private void jRadioButton10ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jRadioButton10ActionPerformed
//		// TODO add your handling code here:
//	}// GEN-LAST:event_jRadioButton10ActionPerformed
//
//	private void jRadioButton11ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jRadioButton11ActionPerformed
//		// TODO add your handling code here:
//	}// GEN-LAST:event_jRadioButton11ActionPerformed
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String html = newsTextArea.getText();
    }// GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(NewsReadAndMakeLink8.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
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
                new NewsReadAndMakeLink8().setVisible(true);
            }
        });
    }

    private String url = "http://news.mt.co.kr/newsPrint.html?no=2017051716214167566";
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea newsTextArea;
    private javax.swing.JTextArea newsTextArea1;
    // End of variables declaration//GEN-END:variables
}
