/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock.news;

import html.parsing.stock.ClassForNameExample;
import html.parsing.stock.DataSort.StockNameAscCompare2;
import html.parsing.stock.GlobalVariables;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import static html.parsing.stock.StockUtil.readStockCodeNameListFromExcel;
import html.parsing.stock.StockVO;
import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.LoggerFactory;

/**
 *
 * @author banks
 */
public class NewsReaderTest extends javax.swing.JFrame {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(NewsReaderTest.class);
    URI uri = null;

    /**
     * Creates new form NewJFrame1
     */
    public NewsReaderTest() {
        initComponents();
        initList();
    }

    void initList() {
        String kospiFileName = GlobalVariables.kospiFileName;
        String kosdaqFileName = GlobalVariables.kosdaqFileName;
        List<StockVO> kospiStockList = new ArrayList<StockVO>();
        List<StockVO> kosdaqStockList = new ArrayList<StockVO>();

        readStockCodeNameListFromExcel(kospiStockList, kospiFileName);
        Collections.sort(kospiStockList, new StockNameAscCompare2());
        readStockCodeNameListFromExcel(kosdaqStockList, kosdaqFileName);
        Collections.sort(kosdaqStockList, new StockNameAscCompare2());

        String kospis[] = new String[kospiStockList.size()];
        String kosdaqs[] = new String[kosdaqStockList.size()];

        for (int i = 0; i < kospiStockList.size(); i++) {
            StockVO vo = kospiStockList.get(i);
            System.out.println("코스피 증권코드:" + vo.getStockCode() + " 증권명:" + vo.getStockName());
            kospis[i] = vo.getStockCode() + ", " + vo.getStockName();
        }
        for (int i = 0; i < kosdaqStockList.size(); i++) {
            StockVO vo = kosdaqStockList.get(i);
            System.out.println("코스닥 증권코드:" + vo.getStockCode() + " 증권명:" + vo.getStockName());
            kosdaqs[i] = vo.getStockCode() + ", " + vo.getStockName();
        }

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            public int getSize() {
                return kospis.length;
            }

            public String getElementAt(int i) {
                return kospis[i];
            }
        });
        jList2.setModel(new javax.swing.AbstractListModel<String>() {
            public int getSize() {
                return kosdaqs.length;
            }

            public String getElementAt(int i) {
                return kosdaqs[i];
            }
        });
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
        jPanel4 = new javax.swing.JPanel();
        urlLbl = new javax.swing.JLabel();
        textFieldPopupMenuPanel1 = new html.parsing.stock.TextFieldPopupMenuPanel();
        jPanel9 = new javax.swing.JPanel();
        executeBtn = new javax.swing.JButton();
        extractImgBtn = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(25, 0), new java.awt.Dimension(25, 0), new java.awt.Dimension(25, 32767));
        jScrollPane4 = new javax.swing.JScrollPane();
        extractedUrlTextPane = new javax.swing.JTextPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        newsTextArea = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane2 = new javax.swing.JEditorPane();
        jPanel8 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane6 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("웹페이지 추출");
        setMinimumSize(new java.awt.Dimension(500, 250));
        setSize(new java.awt.Dimension(1000, 600));

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

        jButton3.setText("초기화");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton3);

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

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.GridLayout(15, 2));

        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel1);

        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel2);

        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel14);

        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel3);

        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel4);

        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel5);

        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel11);

        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel12);

        jLabel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel13MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel13);

        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel8);

        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel9);

        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel10);

        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel6);

        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel7);

        jLabel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel16MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel16);

        jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel15MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel15);

        getContentPane().add(jPanel2, java.awt.BorderLayout.WEST);

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jSplitPane1.setDividerLocation(250);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 106));

        newsTextArea.setColumns(20);
        newsTextArea.setRows(5);
        jScrollPane1.setViewportView(newsTextArea);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jScrollPane3.setPreferredSize(new java.awt.Dimension(200, 100));

        jEditorPane1.setEditable(false);
        jScrollPane3.setViewportView(jEditorPane1);
        // add an html editor kit
        HTMLEditorKit kit = new HTMLEditorKit();
        jEditorPane1.setEditorKit(kit);

        // add some styles to the html
        StyleSheet styleSheet = kit.getStyleSheet();

        // create a document, set it on the jeditorpane, then add the html
        javax.swing.text.Document doc = kit.createDefaultDocument();
        jEditorPane1.setDocument(doc);
        //jEditorPane1.setText("blah blah blah");
        // create some simple html as a string
        String htmlString = "<html>\n"
        + "<body>\n"
        + "<h1>Welcome!</h1>\n"
        + "<h2>This is an H2 header</h2>\n"
        + "<p>This is some sample text</p>\n"
        + "<p><a href=\"http://devdaily.com/blog/\">devdaily blog</a></p>\n"
        + "</body>\n";
        //jEditorPane1.setText(htmlString);

        jSplitPane1.setRightComponent(jScrollPane3);

        jTabbedPane1.addTab("소스보기", jSplitPane1);

        try {
            jEditorPane2.setPage(new java.net.URL("http://www.google.com"));
        } catch (java.io.IOException e1) {
            e1.printStackTrace();
        }
        jScrollPane2.setViewportView(jEditorPane2);

        jTabbedPane1.addTab("브라우징", jScrollPane2);

        jPanel1.add(jTabbedPane1);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jButton1.setText("HTML View");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton1);

        getContentPane().add(jPanel8, java.awt.BorderLayout.SOUTH);

        jPanel3.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanel3.setPreferredSize(new java.awt.Dimension(300, 138));
        jPanel3.setLayout(new java.awt.GridLayout(1, 2));

        jScrollPane5.setMinimumSize(new java.awt.Dimension(100, 23));
        jScrollPane5.setPreferredSize(new java.awt.Dimension(150, 138));

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "item 1" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane5.setViewportView(jList1);

        jPanel3.add(jScrollPane5);

        jScrollPane6.setMinimumSize(new java.awt.Dimension(100, 23));
        jScrollPane6.setPreferredSize(new java.awt.Dimension(150, 138));

        jList2.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "item 1" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList2MouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jList2);

        jPanel3.add(jScrollPane6);

        getContentPane().add(jPanel3, java.awt.BorderLayout.EAST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void extractImgBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_extractImgBtnActionPerformed
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
            Logger.getLogger(NewsReaderTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.out.println("이미지 추출 완료");
        }

    }//GEN-LAST:event_extractImgBtnActionPerformed

    private void navigate(String url) {
        try {
            jEditorPane2.setPage(url);
        } catch (IOException ex) {
            Logger.getLogger(NewsReaderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        navigate("http://www.hani.co.kr");
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        navigate("http://www.khan.co.kr");
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
        navigate("http://www.kmib.co.kr");
    }//GEN-LAST:event_jLabel14MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        navigate("http://www.hankookilbo.com");
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        navigate("http://www.hankyung.com/");
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        navigate("http://www.sedaily.com/");
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        navigate("http://www.chosun.com");
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        navigate("http://www.joins.com");
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        navigate("http://imnews.imbc.com");
    }//GEN-LAST:event_jLabel8MouseClicked

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        navigate("http://news.kbs.co.kr");
    }//GEN-LAST:event_jLabel9MouseClicked

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        navigate("http://www.sbs.co.kr/");
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        navigate("http://www.edaily.co.kr");
    }//GEN-LAST:event_jLabel11MouseClicked

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        navigate("http://www.etoday.co.kr");
    }//GEN-LAST:event_jLabel12MouseClicked

    private void jLabel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseClicked
        navigate("http://www.mt.co.kr/");
    }//GEN-LAST:event_jLabel13MouseClicked

    private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
        navigate("http://www.donga.com");
    }//GEN-LAST:event_jLabel15MouseClicked

    private void jLabel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseClicked
        navigate("http://www.newstomato.com");
    }//GEN-LAST:event_jLabel16MouseClicked

    private void executeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executeBtnActionPerformed
        try {
            extractedUrlTextPane.setText("");
            Thread.sleep(1000);
            createHTMLFile();
        } catch (InterruptedException ex) {
            Logger.getLogger(NewsReaderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_executeBtnActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        textFieldPopupMenuPanel1.getTextField().setText("");
        newsTextArea.setText("");
        jEditorPane1.setText("");
        extractedUrlTextPane.setText("");
    }//GEN-LAST:event_jButton3ActionPerformed

        private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged

        }//GEN-LAST:event_jList1ValueChanged

        private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
            String selected = jList1.getSelectedValue();
            String stockCode = selected.split(",")[0];
            try {
                uri = new URI("https://finance.naver.com/item/main.nhn?code=" + stockCode);
                open(uri);
            } catch (URISyntaxException ex) {
                Logger.getLogger(NewsReaderTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }//GEN-LAST:event_jList1MouseClicked

        private void jList2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList2MouseClicked
            String selected = jList2.getSelectedValue();
            String stockCode = selected.split(",")[0];
            try {
                uri = new URI("https://finance.naver.com/item/main.nhn?code=" + stockCode);
                open(uri);
            } catch (URISyntaxException ex) {
                Logger.getLogger(NewsReaderTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }//GEN-LAST:event_jList2MouseClicked

    private void textFieldPopupMenuPanel1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_textFieldPopupMenuPanel1PropertyChange
        textFieldPopupMenuPanel1.getTextField().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1FocusLost(evt);
            }
        });
        textFieldPopupMenuPanel1.getTextField().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
    }//GEN-LAST:event_textFieldPopupMenuPanel1PropertyChange
    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
        logger.debug("jTextField1ActionPerformed");
        String url = textFieldPopupMenuPanel1.getTextField().getText();
        logger.debug("url :" + url);
        if (url.equals("")) {
            return;
        };
        try {
            extractedUrlTextPane.setText("");
            Thread.sleep(1000);
            createHTMLFile();
        } catch (InterruptedException ex) {
            Logger.getLogger(NewsReaderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {
        logger.debug("jTextField1FocusLost");
        String url = textFieldPopupMenuPanel1.getTextField().getText();
        logger.debug("url :" + url);
        if (url.equals("")) {
            return;
        };
        try {
            extractedUrlTextPane.setText("");
            Thread.sleep(1000);
            createHTMLFile();
        } catch (InterruptedException ex) {
            Logger.getLogger(NewsReaderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void textFieldPopupMenuPanel1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textFieldPopupMenuPanel1FocusLost
        logger.debug("textFieldPopupMenuPanel1FocusLost");
    }//GEN-LAST:event_textFieldPopupMenuPanel1FocusLost

    public void createHTMLFile() {
        String newsCompany = getSelectedButtonText(buttonGroup1);
        System.out.println("newsCompany1:" + newsCompany);
        url = textFieldPopupMenuPanel1.getTextField().getText();
        if (!url.equals("")) {
            if (newsCompany != null) {
                createHTMLFile(url, newsCompany);
            } else {
                //JOptionPane.showMessageDialog(null, "신문명을 선택하여 주세요.");
                createHTMLFile(url);
            }
        }
    }

    private void createHTMLFile(String url) {
        if(url.equals("")) return;
        createHTMLFile(url, "");
    }

    private void createHTMLFile(String url, String newsCompany) {
        if(url.equals("")) return;
        System.out.println("url:" + url);
        //페이지 추출 완료 라벨 초기화
        extractedUrlTextPane.setText("");
        //tab2에서 페이지 이동
        navigate(url);
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
        } else if (newsCompany.equals("NewsWwwHanitvCom")) {
            sb = NewsWwwHanitvCom.createHTMLFile(url);
        }
        if (newsCompany.equals("")) {
            textFieldPopupMenuPanel1.getTextField().setText("");
            extractedUrlTextPane.setText(url + " 추출실패");
            newsTextArea.setText("");
            jEditorPane1.setText("");
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
            Logger.getLogger(ClassForNameExample.class.getName()).log(Level.INFO, sb.toString());
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
        }

        Document htmlDoc = Jsoup.parse(sb.toString());
        newsTextArea.setText(htmlDoc.html());

        htmlDoc.select("meta").remove();
        sb.delete(0, sb.length());
        sb.setLength(0);
        jEditorPane1.setText(htmlDoc.html());
        textFieldPopupMenuPanel1.getTextField().setText("");
        extractedUrlTextPane.setText(url + " 페이지 추출 완료");
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
        jEditorPane1.setText(html);
    }// GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        textFieldPopupMenuPanel1.getTextField().setText("");
        newsTextArea.setText("");
        jEditorPane1.setText("");
        extractedUrlTextPane.setText("");
    }// GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(NewsReaderTest.class.getName()).log(java.util.logging.Level.SEVERE, null,
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
                new NewsReaderTest().setVisible(true);
            }
        });
    }

    private String url = "http://news.mt.co.kr/newsPrint.html?no=2017051716214167566";
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton executeBtn;
    private javax.swing.JButton extractImgBtn;
    private javax.swing.JTextPane extractedUrlTextPane;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JEditorPane jEditorPane2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea newsTextArea;
    private html.parsing.stock.TextFieldPopupMenuPanel textFieldPopupMenuPanel1;
    private javax.swing.JLabel urlLbl;
    // End of variables declaration//GEN-END:variables
}
