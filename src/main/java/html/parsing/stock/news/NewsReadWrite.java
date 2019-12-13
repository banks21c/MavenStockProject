/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock.news;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author banks
 */
public class NewsReadWrite extends javax.swing.JFrame {

    java.util.logging.Logger logger = null;
    private WebEngine webEngine;
    private WebView view;

    /**
     * Creates new form NewJFrame1
     */
    public NewsReadWrite() {
        initComponents();
    }

    public void loadURL(final String url) {
        System.out.println("url:" + url);
        Platform.runLater(() -> {
            webEngine.load(url);
        });
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
        jPanel3 = new javax.swing.JPanel();
        jRadioButton18 = new javax.swing.JRadioButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        jRadioButton9 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton10 = new javax.swing.JRadioButton();
        jRadioButton12 = new javax.swing.JRadioButton();
        jRadioButton13 = new javax.swing.JRadioButton();
        jRadioButton14 = new javax.swing.JRadioButton();
        jRadioButton15 = new javax.swing.JRadioButton();
        jRadioButton16 = new javax.swing.JRadioButton();
        jRadioButton17 = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        urlLbl = new javax.swing.JLabel();
        urlTf = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        executeBtn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        JFXPanel jfxPanel = new JFXPanel();
        jfxPanel.setPreferredSize(new Dimension(300, 300));
        jPanel1.add(jfxPanel);

        Platform.runLater(() -> {
            view = new WebView();
            webEngine = view.getEngine();

            jfxPanel.setScene(new Scene(view));
        });
        jScrollPane1 = new javax.swing.JScrollPane();
        htmlViewArea = new javax.swing.JTextArea();
        jPanel8 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("웹페이지 추출");
        setMinimumSize(new java.awt.Dimension(250, 250));
        setSize(new java.awt.Dimension(500, 500));

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel3.setMinimumSize(new java.awt.Dimension(429, 150));
        jPanel3.setRequestFocusEnabled(false);
        jPanel3.setLayout(new java.awt.GridLayout(3, 5));

        buttonGroup1.add(jRadioButton18);
        jRadioButton18.setText("머니 투데이");
        jRadioButton18.setActionCommand("MT");
        jRadioButton18.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton18ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton18);

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("시사인");
        jRadioButton1.setActionCommand("SSI");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton1);

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("국민일보");
        jRadioButton2.setActionCommand("KMIB");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton2);

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setText("경향신문");
        jRadioButton4.setActionCommand("KHSM");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton4);

        buttonGroup1.add(jRadioButton5);
        jRadioButton5.setText("한겨레");
        jRadioButton5.setActionCommand("HKR");
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton5ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton5);

        buttonGroup1.add(jRadioButton6);
        jRadioButton6.setText("중앙일보");
        jRadioButton6.setActionCommand("JAIB");
        jRadioButton6.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton6ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton6);

        buttonGroup1.add(jRadioButton7);
        jRadioButton7.setText("조선일보");
        jRadioButton7.setActionCommand("CSIB");
        jRadioButton7.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton7ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton7);

        buttonGroup1.add(jRadioButton8);
        jRadioButton8.setText("한국일보");
        jRadioButton8.setActionCommand("HKIB");
        jRadioButton8.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton8ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton8);

        buttonGroup1.add(jRadioButton9);
        jRadioButton9.setText("허핑턴포스트");
        jRadioButton9.setActionCommand("HPTP");
        jRadioButton9.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton9ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton9);

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("다음");
        jRadioButton3.setActionCommand("DAUM");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton3);

        buttonGroup1.add(jRadioButton10);
        jRadioButton10.setText("네이버");
        jRadioButton10.setActionCommand("NAVER");
        jRadioButton10.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton10ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton10);

        buttonGroup1.add(jRadioButton12);
        jRadioButton12.setText("연합뉴스");
        jRadioButton12.setActionCommand("YHN");
        jRadioButton12.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton12ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton12);

        buttonGroup1.add(jRadioButton13);
        jRadioButton13.setText("노컷뉴스");
        jRadioButton13.setActionCommand("NC");
        jRadioButton13.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton13ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton13);

        buttonGroup1.add(jRadioButton14);
        jRadioButton14.setText("한국경제");
        jRadioButton14.setActionCommand("HKE");
        jRadioButton14.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton14ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton14);

        buttonGroup1.add(jRadioButton15);
        jRadioButton15.setText("스타한국");
        jRadioButton15.setActionCommand("SHK");
        jRadioButton15.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton15ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton15);

        buttonGroup1.add(jRadioButton16);
        jRadioButton16.setText("YTN");
        jRadioButton16.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton16ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton16);

        buttonGroup1.add(jRadioButton17);
        jRadioButton17.setText("이데일리");
        jRadioButton17.setActionCommand("EDAILY");
        jRadioButton17.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton17ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton17);

        jPanel7.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.BorderLayout());

        urlLbl.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        urlLbl.setText("주소 : ");
        urlLbl.setToolTipText("");
        jPanel4.add(urlLbl, java.awt.BorderLayout.WEST);

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
        jPanel4.add(urlTf, java.awt.BorderLayout.CENTER);

        executeBtn.setText("페이지 추출");
        executeBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeBtnActionPerformed(evt);
            }
        });
        jPanel9.add(executeBtn);

        jButton2.setText("초기화");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton2);

        jPanel4.add(jPanel9, java.awt.BorderLayout.EAST);

        jPanel7.add(jPanel4, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel7, java.awt.BorderLayout.PAGE_START);

        jPanel1.setLayout(new java.awt.GridLayout(1, 2));

        htmlViewArea.setColumns(20);
        htmlViewArea.setRows(5);
        jScrollPane1.setViewportView(htmlViewArea);

        jPanel1.add(jScrollPane1);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jButton1.setText("HTML View");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton1);

        getContentPane().add(jPanel8, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton5ActionPerformed
        // TODO add your handling code here:
        loadURL("http://www.hani.co.kr");
    }//GEN-LAST:event_jRadioButton5ActionPerformed

    private void jRadioButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton14ActionPerformed
        // TODO add your handling code here:
        loadURL("http://www.hankyung.com");
    }//GEN-LAST:event_jRadioButton14ActionPerformed

    private void jRadioButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton16ActionPerformed
        // TODO add your handling code here:
        loadURL("http://www.ytn.co.kr");
    }//GEN-LAST:event_jRadioButton16ActionPerformed

    private void jRadioButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton7ActionPerformed
        // TODO add your handling code here:
        loadURL("http://www.chosun.com");
    }//GEN-LAST:event_jRadioButton7ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // TODO add your handling code here:
        loadURL("http://www.khan.co.kr");
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jRadioButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton12ActionPerformed
        // TODO add your handling code here:
        loadURL("www.yonhapnews.co.kr/");
    }//GEN-LAST:event_jRadioButton12ActionPerformed

    private void jRadioButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton17ActionPerformed
        // TODO add your handling code here:
        loadURL("http://www.edaily.co.kr");
    }//GEN-LAST:event_jRadioButton17ActionPerformed

    private void jRadioButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton15ActionPerformed
        // TODO add your handling code here:
        loadURL("http://star.hankookilbo.com");
    }//GEN-LAST:event_jRadioButton15ActionPerformed

    private void jRadioButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton13ActionPerformed
        // TODO add your handling code here:
        loadURL("http://www.nocutnews.co.kr");
    }//GEN-LAST:event_jRadioButton13ActionPerformed

    private void jRadioButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton8ActionPerformed
        // TODO add your handling code here:
        loadURL("http://www.hankookilbo.com");
    }//GEN-LAST:event_jRadioButton8ActionPerformed

    private void jRadioButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton9ActionPerformed
        // TODO add your handling code here:
        loadURL("http://www.huffingtonpost.com");
    }//GEN-LAST:event_jRadioButton9ActionPerformed

    private void jRadioButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton6ActionPerformed
        // TODO add your handling code here:
        loadURL("http://www.joins.com");
    }//GEN-LAST:event_jRadioButton6ActionPerformed

    private void jRadioButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton18ActionPerformed
        // TODO add your handling code here:
        loadURL("http://www.mt.co.kr");
    }//GEN-LAST:event_jRadioButton18ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
        loadURL("http://www.sisain.co.kr");
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
        loadURL("http://www.kmib.co.kr");
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // TODO add your handling code here:
        loadURL("http://www.daum.net");
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton10ActionPerformed
        // TODO add your handling code here:
        loadURL("http://www.naver.com");
    }//GEN-LAST:event_jRadioButton10ActionPerformed

    private void executeBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_executeBtnActionPerformed
        createHTMLFile();
    }// GEN-LAST:event_executeBtnActionPerformed

    public void createHTMLFile() {
        String newsCompany = getSelectedButtonText(buttonGroup1);
        System.out.println("newsCompany:" + newsCompany);
        if (newsCompany != null) {
            url = urlTf.getText();
            if (!url.equals("")) {
                createHTMLFile(url, newsCompany);
            } else {
                JOptionPane.showMessageDialog(null, "저장할 주소를 입력하여 주세요.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "신문명을 선택하여 주세요.");
        }
    }

    private void createHTMLFile(String url, String newsCompany) {
        loadURL(urlTf.getText());
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

    private void jRadioButton11ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jRadioButton11ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jRadioButton11ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //System.out.println("document.documentElement.outerHTML:"+(String) webEngine.executeScript("document.documentElement.outerHTML"));
        //String html = (String) webEngine.executeScript("document.documentElement.outerHTML");

        org.w3c.dom.Document xmlDom = webEngine.getDocument();
        System.out.println(xmlDom);
        String html = xmlDom.getTextContent();
        System.out.println(xmlDom);

        Document doc = Jsoup.parse(html);
        doc.select("meta").remove();
        html = doc.html();
        System.out.println("html:" + html);
        htmlViewArea.setText(html);

        String newsCompany = getSelectedButtonText(buttonGroup1);
        System.out.println("newsCompany:" + newsCompany);
        if (newsCompany != null) {
            url = urlTf.getText();
            if (!url.equals("")) {
                StringBuilder sb = new StringBuilder();
                if (newsCompany.equals("MT")) {
                    sb = NewsMoneyToday.createHTMLFile(url);
                } else if (newsCompany.equals("SSI")) {
                    sb = NewsSisain.createHTMLFile(url);
                } else if (newsCompany.equals("KMIB")) {
                    sb = NewsKmib.createHTMLFile(url);
                } else if (newsCompany.equals("KHSM")) {
                } else if (newsCompany.equals("HKR")) {
                    sb = NewsHankyoreh.createHTMLFile(url);
                } else if (newsCompany.equals("JAIB")) {
                    sb = NewsJoinsCom.createHTMLFile(url);
                } else if (newsCompany.equals("CSIB")) {
                    sb = NewsChosun.createHTMLFile(url);
                } else if (newsCompany.equals("HKIB")) {
                    sb = NewsHankookilbo.createHTMLFile(url);
                } else if (newsCompany.equals("HPTP")) {
                    sb = NewsHuffingtonpost1.createHTMLFile(url);
                } else if (newsCompany.equals("DAUM")) {
                    sb = NewsDaumNet.createHTMLFile(url);
                } else if (newsCompany.equals("NAVER")) {
                    sb = NewsNaverCom.createHTMLFile(url);
                } else if (newsCompany.equals("YHN")) {
                    sb = NewsYonhap.createHTMLFile(url);
                } else if (newsCompany.equals("HKE")) {
                    sb = WwwHankyungCom.createHTMLFile(url);
                } else if (newsCompany.equals("SHK")) {
                    sb = NewsStarHankook.createHTMLFile(url);
                } else if (newsCompany.equals("YTN")) {
                    sb = NewsYTN.createHTMLFile(url);
                } else if (newsCompany.equals("EDAILY")) {
                    sb = NewsYTN.createHTMLFile(url);
                }
            } else {
                JOptionPane.showMessageDialog(null, "저장할 주소를 입력하여 주세요.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "신문명을 선택하여 주세요.");
        }

    }// GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        urlTf.setText("");
        htmlViewArea.setText("");
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewsReadWrite.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewsReadWrite.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewsReadWrite.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewsReadWrite.class.getName()).log(java.util.logging.Level.SEVERE, null,
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
			public void run() {
                new NewsReadWrite().setVisible(true);
            }
        });
    }

    private String url = "http://news.mt.co.kr/newsPrint.html?no=2017051716214167566";
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton executeBtn;
    private javax.swing.JTextArea htmlViewArea;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton10;
    private javax.swing.JRadioButton jRadioButton12;
    private javax.swing.JRadioButton jRadioButton13;
    private javax.swing.JRadioButton jRadioButton14;
    private javax.swing.JRadioButton jRadioButton15;
    private javax.swing.JRadioButton jRadioButton16;
    private javax.swing.JRadioButton jRadioButton17;
    private javax.swing.JRadioButton jRadioButton18;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel urlLbl;
    private javax.swing.JTextField urlTf;
    // End of variables declaration//GEN-END:variables
}
