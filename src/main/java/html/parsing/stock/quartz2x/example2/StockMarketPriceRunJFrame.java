package html.parsing.stock.quartz2x.example2;

import javax.swing.JOptionPane;

import html.parsing.stock.focus.StockPlusMinusDivide100;
import html.parsing.stock.focus.StockUnique_ReadTxtFile_ThreadCall;
import html.parsing.stock.focus.StockWeeks52NewLowHighPriceTodayOneFile;

/**
 * 유튜브에서 네이버로 공유할때 공유하기 화면
 */
public class StockMarketPriceRunJFrame extends javax.swing.JFrame {

	String strBlogId = "";
	String strNidAut = "";
	String strNidSes = "";

	/**
	 * Creates new form StockMarketPriceRunJFrame
	 */
	public StockMarketPriceRunJFrame() {
		initComponents();
//		execute();
	}

	public StockMarketPriceRunJFrame(String strNidAut, String strNidSes) {
		this.strNidAut = strNidAut;
		this.strNidSes = strNidSes;
		System.out.println("strNidAut:" + strNidAut);
		System.out.println("strNidSes:" + strNidSes);
	}

	public void execute() {
//        new StockMarketPrice().extractAll();
		strNidAut = nidAutTa.getText();
		strNidSes = nidSesTa.getText();
//		NaverUtil.naverBlogLinkShare(strNidAut, strNidSes, strShareUrl, strShareTitle, strCategoryName, contentSb, rootPane);
		if (strNidAut.equals("")) {
			JOptionPane.showMessageDialog(rootPane, "NID_AUT를 입력하여 주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		} else if (strNidSes.equals("")) {
			JOptionPane.showMessageDialog(rootPane, "NID_SES를 입력하여 주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}

		start();
	}

	public void start() {
		new StockUnique_ReadTxtFile_ThreadCall(strBlogId, strNidAut, strNidSes).start();
		new StockWeeks52NewLowHighPriceTodayOneFile(strBlogId, strNidAut, strNidSes).start();
//		new StockPlusMinusDivide_ThreadCall(strNidAut,strNidSes).start();
		new StockPlusMinusDivide100(strBlogId, strNidAut, strNidSes).start();
	}

	/**
	 * This method is called from within the constructor to initialize the
	 * form. WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jPanel1 = new javax.swing.JPanel();
                jLabel3 = new javax.swing.JLabel();
                jPanel6 = new javax.swing.JPanel();
                jPanel2 = new javax.swing.JPanel();
                jLabel1 = new javax.swing.JLabel();
                jScrollPane1 = new javax.swing.JScrollPane();
                nidAutTa = new javax.swing.JTextArea();
                jPanel7 = new javax.swing.JPanel();
                jButton1 = new javax.swing.JButton();
                jPanel5 = new javax.swing.JPanel();
                jLabel2 = new javax.swing.JLabel();
                jScrollPane2 = new javax.swing.JScrollPane();
                nidSesTa = new javax.swing.JTextArea();
                jPanel4 = new javax.swing.JPanel();
                jButton2 = new javax.swing.JButton();
                jPanel3 = new javax.swing.JPanel();
                jButton3 = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setTitle("네이버 블로그 공유");
                setPreferredSize(new java.awt.Dimension(800, 400));

                jPanel1.setLayout(new java.awt.GridLayout(2, 0));

                jLabel3.setText("주식 시세 조회,저장, 블로그에 글쓰기");
                jPanel1.add(jLabel3);

                getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

                jPanel6.setPreferredSize(new java.awt.Dimension(1215, 160));

                jPanel2.setPreferredSize(new java.awt.Dimension(600, 33));
                jPanel2.setLayout(new java.awt.BorderLayout());

                jLabel1.setText("NID_AUT");
                jLabel1.setPreferredSize(new java.awt.Dimension(70, 15));
                jPanel2.add(jLabel1, java.awt.BorderLayout.WEST);

                jScrollPane1.setPreferredSize(new java.awt.Dimension(500, 106));

                nidAutTa.setColumns(25);
                nidAutTa.setLineWrap(true);
                nidAutTa.setRows(1);
                jScrollPane1.setViewportView(nidAutTa);

                jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

                jButton1.setText("삭제");
                jButton1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton1ActionPerformed(evt);
                        }
                });
                jPanel7.add(jButton1);

                jPanel2.add(jPanel7, java.awt.BorderLayout.EAST);

                jPanel6.add(jPanel2);

                jPanel5.setPreferredSize(new java.awt.Dimension(600, 200));
                jPanel5.setLayout(new java.awt.BorderLayout());

                jLabel2.setText("NID_SES");
                jLabel2.setPreferredSize(new java.awt.Dimension(70, 15));
                jPanel5.add(jLabel2, java.awt.BorderLayout.WEST);

                jScrollPane2.setPreferredSize(new java.awt.Dimension(500, 250));

                nidSesTa.setColumns(25);
                nidSesTa.setLineWrap(true);
                nidSesTa.setRows(10);
                jScrollPane2.setViewportView(nidSesTa);

                jPanel5.add(jScrollPane2, java.awt.BorderLayout.CENTER);

                jButton2.setText("삭제");
                jButton2.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton2ActionPerformed(evt);
                        }
                });
                jPanel4.add(jButton2);

                jPanel5.add(jPanel4, java.awt.BorderLayout.EAST);

                jPanel6.add(jPanel5);

                getContentPane().add(jPanel6, java.awt.BorderLayout.CENTER);

                jButton3.setText("네이버 블로그에 업로드");
                jButton3.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton3ActionPerformed(evt);
                        }
                });
                jPanel3.add(jButton3);

                getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

                getAccessibleContext().setAccessibleName("주식 시세 조회");

                pack();
        }// </editor-fold>//GEN-END:initComponents

        private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
		// TODO add your handling code here:
		execute();
        }//GEN-LAST:event_jButton3ActionPerformed

        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
		// TODO add your handling code here:
		nidAutTa.setText("");
        }//GEN-LAST:event_jButton1ActionPerformed

        private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
		// TODO add your handling code here:
		nidSesTa.setText("");
        }//GEN-LAST:event_jButton2ActionPerformed

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
			java.util.logging.Logger.getLogger(StockMarketPriceRunJFrame.class.getName())
				.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(StockMarketPriceRunJFrame.class.getName())
				.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(StockMarketPriceRunJFrame.class.getName())
				.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(StockMarketPriceRunJFrame.class.getName())
				.log(java.util.logging.Level.SEVERE, null, ex);
		}

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new StockMarketPriceRunJFrame().setVisible(true);
			}
		});
	}

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton jButton1;
        private javax.swing.JButton jButton2;
        private javax.swing.JButton jButton3;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JPanel jPanel4;
        private javax.swing.JPanel jPanel5;
        private javax.swing.JPanel jPanel6;
        private javax.swing.JPanel jPanel7;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JTextArea nidAutTa;
        private javax.swing.JTextArea nidSesTa;
        // End of variables declaration//GEN-END:variables
}
