package html.parsing.stock.javafx;

import java.io.File;
import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.NaverUtil;

/**
 * 유튜브에서 네이버로 공유할때 공유하기 화면
 */
public class NaverBlogLinkShareTest_Step2_JFrame1 extends javax.swing.JFrame {

	public static final String LINK_SHARE_URI_PREFIX = "http://blog.naver.com/LinkShare.nhn?url=";
	//로그인 안됐을 때 이동하는 URL
	//https://nid.naver.com/nidlogin.login?mode=form&template=plogin&url=http%3A%2F%2Fblog.naver.com%2FLinkShare.nhn%3Furl%3Dhttps%253A%252F%252Fwww.youtube.com%252Fwatch%253Fv%253DJ6zD3h_I3Lc%2526feature%253Dshare
	//로그인후 url로 이동
	//http://blog.naver.com/LinkShare.nhn?url=https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DJ6zD3h_I3Lc%26feature%3Dshare

	private static final Logger logger = LoggerFactory.getLogger(NaverBlogLinkShareTest_Step2_JFrame1.class);

	String strYmdhms = new SimpleDateFormat("yyyyMMdd_hhmmss", Locale.KOREAN).format(new Date());
	public final static String USER_HOME = System.getProperty("user.home");

	/**
	 * Creates new form NewJFrame
	 */
	public NaverBlogLinkShareTest_Step2_JFrame1() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the
	 * form. WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jPanel1 = new javax.swing.JPanel();
                jLabel3 = new javax.swing.JLabel();
                jPanel6 = new javax.swing.JPanel();
                jPanel2 = new javax.swing.JPanel();
                jScrollPane1 = new javax.swing.JScrollPane();
                nidAutTa = new javax.swing.JTextArea();
                jPanel7 = new javax.swing.JPanel();
                jButton4 = new javax.swing.JButton();
                jPanel12 = new javax.swing.JPanel();
                jLabel1 = new javax.swing.JLabel();
                jPanel5 = new javax.swing.JPanel();
                jPanel11 = new javax.swing.JPanel();
                jLabel2 = new javax.swing.JLabel();
                jScrollPane2 = new javax.swing.JScrollPane();
                nidSesTa = new javax.swing.JTextArea();
                jPanel4 = new javax.swing.JPanel();
                jButton3 = new javax.swing.JButton();
                jPanel8 = new javax.swing.JPanel();
                jPanel10 = new javax.swing.JPanel();
                jLabel4 = new javax.swing.JLabel();
                jScrollPane3 = new javax.swing.JScrollPane();
                urlTa = new javax.swing.JTextArea();
                jPanel9 = new javax.swing.JPanel();
                jButton5 = new javax.swing.JButton();
                jPanel3 = new javax.swing.JPanel();
                jButton1 = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setTitle("네이버 블로그 공유");

                jPanel1.setLayout(new java.awt.GridLayout(2, 0));

                jLabel3.setText("네이버 블로그에 공유 화면 저장");
                jPanel1.add(jLabel3);

                getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

                jPanel2.setPreferredSize(new java.awt.Dimension(600, 100));
                jPanel2.setLayout(new java.awt.BorderLayout());

                jScrollPane1.setPreferredSize(new java.awt.Dimension(500, 106));

                nidAutTa.setColumns(20);
                nidAutTa.setLineWrap(true);
                nidAutTa.setRows(4);
                jScrollPane1.setViewportView(nidAutTa);

                jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

                jButton4.setText("삭제");
                jButton4.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton4ActionPerformed(evt);
                        }
                });
                jPanel7.add(jButton4);

                jPanel2.add(jPanel7, java.awt.BorderLayout.EAST);

                jLabel1.setText("NID_AUT");
                jLabel1.setPreferredSize(new java.awt.Dimension(100, 15));
                jPanel12.add(jLabel1);

                jPanel2.add(jPanel12, java.awt.BorderLayout.WEST);

                jPanel6.add(jPanel2);

                jPanel5.setPreferredSize(new java.awt.Dimension(600, 200));
                jPanel5.setLayout(new java.awt.BorderLayout());

                jLabel2.setText("NID_SES");
                jLabel2.setPreferredSize(new java.awt.Dimension(100, 15));
                jPanel11.add(jLabel2);

                jPanel5.add(jPanel11, java.awt.BorderLayout.WEST);

                jScrollPane2.setPreferredSize(new java.awt.Dimension(500, 250));

                nidSesTa.setColumns(20);
                nidSesTa.setLineWrap(true);
                nidSesTa.setRows(10);
                nidSesTa.setPreferredSize(new java.awt.Dimension(144, 240));
                jScrollPane2.setViewportView(nidSesTa);

                jPanel5.add(jScrollPane2, java.awt.BorderLayout.CENTER);

                jButton3.setText("삭제");
                jButton3.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton3ActionPerformed(evt);
                        }
                });
                jPanel4.add(jButton3);

                jPanel5.add(jPanel4, java.awt.BorderLayout.EAST);

                jPanel6.add(jPanel5);

                jPanel8.setPreferredSize(new java.awt.Dimension(600, 200));
                jPanel8.setLayout(new java.awt.BorderLayout());

                jPanel10.setMinimumSize(new java.awt.Dimension(50, 15));
                jPanel10.setName(""); // NOI18N

                jLabel4.setText("공유 URL");
                jLabel4.setPreferredSize(new java.awt.Dimension(100, 15));
                jPanel10.add(jLabel4);

                jPanel8.add(jPanel10, java.awt.BorderLayout.WEST);

                urlTa.setColumns(20);
                urlTa.setRows(3);
                jScrollPane3.setViewportView(urlTa);

                jPanel8.add(jScrollPane3, java.awt.BorderLayout.CENTER);

                jButton5.setText("삭제");
                jButton5.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton5ActionPerformed(evt);
                        }
                });
                jPanel9.add(jButton5);

                jPanel8.add(jPanel9, java.awt.BorderLayout.EAST);

                jPanel6.add(jPanel8);

                getContentPane().add(jPanel6, java.awt.BorderLayout.CENTER);

                jButton1.setText("공유 화면 저장");
                jButton1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton1ActionPerformed(evt);
                        }
                });
                jPanel3.add(jButton1);

                getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

                pack();
        }// </editor-fold>//GEN-END:initComponents

        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
		// TODO add your handling code here:
		naverBlogLinkShare();
        }//GEN-LAST:event_jButton1ActionPerformed

        private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
		// TODO add your handling code here:
		nidAutTa.setText("");
        }//GEN-LAST:event_jButton4ActionPerformed

        private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
		// TODO add your handling code here:
		nidSesTa.setText("");
        }//GEN-LAST:event_jButton3ActionPerformed

        private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
		// TODO add your handling code here:
        }//GEN-LAST:event_jButton5ActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(NaverBlogLinkShareTest_Step2_JFrame1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(NaverBlogLinkShareTest_Step2_JFrame1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(NaverBlogLinkShareTest_Step2_JFrame1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(NaverBlogLinkShareTest_Step2_JFrame1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>
		//</editor-fold>
		//</editor-fold>
		//</editor-fold>
		//</editor-fold>
		//</editor-fold>
		//</editor-fold>
		//</editor-fold>
		//</editor-fold>
		//</editor-fold>
		//</editor-fold>
		//</editor-fold>
		//</editor-fold>
		//</editor-fold>
		//</editor-fold>
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new NaverBlogLinkShareTest_Step2_JFrame1().setVisible(true);
			}
		});
	}

	public void naverBlogLinkShare() {
		String strNidAut = nidAutTa.getText();
		String strNidSes = nidSesTa.getText();
		String strUrl = urlTa.getText();
		if (strNidAut.equals("")) {
			JOptionPane.showMessageDialog(rootPane, "NID_AUT를 입력하여 주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		} else if (strNidSes.equals("")) {
			JOptionPane.showMessageDialog(rootPane, "NID_SES를 입력하여 주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		} else if (strUrl.equals("")) {
			JOptionPane.showMessageDialog(rootPane, "URL을 입력하여 주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			HttpHeaders headers = new HttpHeaders();

			headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			headers.set("Accept-Encoding", "gzip, deflate");
			headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			headers.set("Cache-Control", "max-age=0");
			headers.set("Connection", "keep-alive");
//			headers.set("Content-Length", "4148");
//			headers.set("Content-Type", "application/x-www-form-urlencoded");
			//headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//			headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//			headers.setContentType(MediaType.TEXT_PLAIN);

			StringBuilder cookieSb = new StringBuilder();
			cookieSb.append("NID_AUT=");
			cookieSb.append(strNidAut).append(";");
			cookieSb.append("NID_SES=");
			cookieSb.append(strNidSes).append(";");

			headers.set("Cookie", cookieSb.toString());

			headers.set("Host", "blog.naver.com");
//			headers.set("Origin", "http://blog.naver.com");
//			headers.set("Referer", "http://blog.naver.com/LinkShare.nhn?url=https%3A//www.youtube.com/watch%3Fv%3DaL55d6sDiGE%26feature%3Dshare");
			headers.set("Upgrade-Insecure-Requests", "1");
			headers.set("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");

//			headers.set("X-Requested-With", "XMLHttpRequest");
			headers.forEach((key, value) -> {
				System.out.println(String.format("Header '%s' = %s", key, value));
			});

			RestTemplate restTemplate = new RestTemplate();

//			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
			System.out.println("__________1_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				System.out.println(httpMessageConverter);
			}
			System.out.println("__________1_____________");

//            HttpEntity<String> entity = new HttpEntity<String>(headers);
//			messageConverters.add(new org.springframework.http.converter.ByteArrayHttpMessageConverter());
			//org.springframework.web.client.RestClientException: Could not write request: no suitable HttpMessageConverter found for request type [org.springframework.util.LinkedMultiValueMap] and content type [application/x-www-form-urlencoded]
//			messageConverters.add(new org.springframework.http.converter.StringHttpMessageConverter());
			//org.springframework.web.client.RestClientException: Could not write request: no suitable HttpMessageConverter found for request type [org.springframework.util.LinkedMultiValueMap] and content type [application/x-www-form-urlencoded]			
//			messageConverters.add(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter());
			//org.springframework.web.client.RestClientException: Could not write request: no suitable HttpMessageConverter found for request type [org.springframework.util.LinkedMultiValueMap] and content type [application/x-www-form-urlencoded]			
//			messageConverters.add(new org.springframework.http.converter.ResourceHttpMessageConverter());
			//org.springframework.web.client.RestClientException: Could not write request: no suitable HttpMessageConverter found for request type [org.springframework.util.LinkedMultiValueMap] and content type [application/x-www-form-urlencoded]			
			//			messageConverters.add(new org.springframework.http.converter.xml.SourceHttpMessageConverter());
			//org.springframework.web.client.RestClientException: Could not write request: no suitable HttpMessageConverter found for request type [org.springframework.util.LinkedMultiValueMap] and content type [application/x-www-form-urlencoded]
//			messageConverters.add(new org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter());
			//org.springframework.web.client.RestClientException: Could not extract response: no suitable HttpMessageConverter found for response type [class java.lang.String] and content type [text/html;charset=UTF-8]
//			messageConverters.add(new org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter());
			//org.springframework.web.client.RestClientException: Could not write request: no suitable HttpMessageConverter found for request type [org.springframework.util.LinkedMultiValueMap] and content type [application/x-www-form-urlencoded]
			messageConverters.add(new org.springframework.http.converter.FormHttpMessageConverter());
			//org.springframework.web.client.RestClientException: Could not extract response: no suitable HttpMessageConverter found for response type [class java.lang.String] and content type [text/html;charset=UTF-8]
//			messageConverters.add(new org.springframework.http.converter.ResourceRegionHttpMessageConverter());
			//org.springframework.web.client.RestClientException: Could not write request: no suitable HttpMessageConverter found for request type [org.springframework.util.LinkedMultiValueMap] and content type [application/x-www-form-urlencoded]
			System.out.println("___________2____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				System.out.println(httpMessageConverter);
			}
			System.out.println("__________2_____________");

//			RestTemplate restTemplate2 = new RestTemplate(messageConverters);
//			restTemplate.setMessageConverters(messageConverters);
			messageConverters = restTemplate.getMessageConverters();
			System.out.println("__________3_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				System.out.println(httpMessageConverter);
			}
			System.out.println("__________3_____________");

			//Form Data
//			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
//			map.add("url", "https://www.youtube.com/watch?v=J6zD3h_I3Lc&feature=share");
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(LINK_SHARE_URI_PREFIX);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("http").host("blog.naver.com");
//			builder = builder.path("/LinkShare.nhn");
			builder = builder.path("/openapi/share");
			strUrl = URLEncoder.encode(strUrl, "UTF-8");
			builder = builder.queryParam("url", strUrl);
			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			System.out.println("uri:" + uri);
			System.out.println("uri path:" + uri.getPath());

			System.out.println("uriComponents :" + uriComponents);
//			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
//			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			HttpEntity<?> entity = new HttpEntity<>(headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, byte[].class);
			System.out.println("response :" + response);

//			RestTemplate template = new RestTemplate();
//			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
//			HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
//			ResponseEntity<String> entity = restTemplate.getForEntity("https://example.com", String.class);
//			HttpEntity<?> entity = new HttpEntity<>(headers);
//			HttpEntity<String> entity = new HttpEntity<String>(headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, byte[].class);
//			ResponseEntity<Object> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, Object.class);
//			ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
//		        ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
			System.out.println("response.getStatusCode():" + response.getStatusCode());
			HttpHeaders responseHeaders = response.getHeaders();
			responseHeaders.forEach((key, value) -> {
				System.out.println(String.format("Response Header [%s] = %s", key, value));
			});

//			System.out.println("guessEncoding :" + guessEncoding(response.getBody()));
			byte[] responseBody = response.getBody();
			System.out.println("body :" + responseBody);
			String unzipString = "";
			if (responseBody != null) {
				unzipString = NaverUtil.unzipStringFromBytes(response.getBody(), "UTF8");
			}

			System.out.println("body:" + unzipString);
			if (response.getStatusCode() == HttpStatus.OK) {
//				Files.write(Paths.get(marketType + "_excelDownload.xls"), response.getBody().toString().getBytes("EUC-KR"));
				String fileName = USER_HOME + File.separator + "documents" + File.separator + strYmdhms + "_" + "LinkShare.nhn.html";
				FileUtil.fileWrite(fileName, unzipString);
			};
			System.out.println("finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton jButton1;
        private javax.swing.JButton jButton3;
        private javax.swing.JButton jButton4;
        private javax.swing.JButton jButton5;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel10;
        private javax.swing.JPanel jPanel11;
        private javax.swing.JPanel jPanel12;
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
        private javax.swing.JTextArea nidAutTa;
        private javax.swing.JTextArea nidSesTa;
        private javax.swing.JTextArea urlTa;
        // End of variables declaration//GEN-END:variables
}
