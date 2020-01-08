package html.parsing.stock;

import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;

import org.apache.commons.io.FilenameUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.news.News;

/**
 *
 * @author banks
 */
public class URLFileDownloadUsingJsoupFrame extends javax.swing.JFrame {

    private static Logger logger = LoggerFactory.getLogger(URLFileDownloadUsingJsoupFrame.class);
    final static String userHome = System.getProperty("user.home");

    /**
     * Creates new form NewJFrame1
     */
    public URLFileDownloadUsingJsoupFrame() {
        initComponents();
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

        jPanel2 = new javax.swing.JPanel();
        urlLbl = new javax.swing.JLabel();
        textFieldPopupMenuPanel1 = new html.parsing.stock.TextFieldPopupMenuPanel();
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
        jPanel2.add(textFieldPopupMenuPanel1, java.awt.BorderLayout.CENTER);

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
    }// </editor-fold>//GEN-END:initComponents

    private void executeBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_executeBtnActionPerformed
        String url = textFieldPopupMenuPanel1.getTextField().getText();
        createHTMLFile(url);
    }// GEN-LAST:event_executeBtnActionPerformed

    private void urlTfActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_urlTfActionPerformed

    }// GEN-LAST:event_urlTfActionPerformed

    private void urlTfKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_urlTfKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            // SwingUtilities.getWindowAncestor(evt.getComponent()).dispose();
            String url = textFieldPopupMenuPanel1.getTextField().getText();
            createHTMLFile(url);
        }
    }// GEN-LAST:event_urlTfKeyReleased

    private void createHTMLFile(String sourceUrl) {

        News gurl = new News();
        gurl.getURL(sourceUrl);
        String protocolHost = gurl.getProtocolHost();
        String protocol = gurl.getProtocol();
        String host = gurl.getHost();
        String path = gurl.getPath();

        try {
            Connection con = Jsoup.connect(sourceUrl);
            Document doc = con.get();

            System.out.println("protocolHost ===> " + protocolHost);
            System.out.println("path ===> " + path);

            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocolHost, path);
            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

            System.out.println("===============doc=================>");
            System.out.println(doc);
            System.out.println("<==============doc==================");

            System.out.println("sourceUrl:" + sourceUrl);
            String fileName = FilenameUtils.getName(sourceUrl);
            System.out.println("fileName:" + fileName);
            if (fileName == null || fileName.equals("")) {
                fileName = "index.html";
            }
            String targetUrl = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + fileName;
            System.out.println("targetUrl:" + targetUrl);
            OutputStream imageWriter;
            try {
                imageWriter = new BufferedOutputStream(new FileOutputStream(targetUrl));
                imageWriter.write(doc.html().getBytes());
            } catch (FileNotFoundException ex) {
                java.util.logging.Logger.getLogger(URLFileDownloadUsingJsoupFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(URLFileDownloadUsingJsoupFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(URLFileDownloadUsingJsoupFrame.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.out.println("file download finished");
        }
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
            java.util.logging.Logger.getLogger(URLFileDownloadUsingJsoupFrame.class.getName()).log(java.util.logging.Level.SEVERE,
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
                new URLFileDownloadUsingJsoupFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton executeBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private html.parsing.stock.TextFieldPopupMenuPanel textFieldPopupMenuPanel1;
    private javax.swing.JLabel urlLbl;
    // End of variables declaration//GEN-END:variables
}
