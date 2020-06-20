
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GomtvMp4Download {

	private final static Logger logger = LoggerFactory.getLogger(GomtvMp4Download.class);
	String strYmdhms = new SimpleDateFormat("yyyyMMdd_hhmmss", Locale.KOREAN).format(new Date());
	DecimalFormat df = new DecimalFormat("###.##");

	static final String USER_HOME = System.getProperty("user.home");

	public GomtvMp4Download() {
		String strUrl = "https://pip-gomtv-xcdn-c.pip.cjenm.com/smc/gomtv/multi/eng/C01_118356/2f636a656e6d2f434c49502f45412f423132303135353532342f423132303135353532345f455049303132315f30345f7433342e6d7034/0-0-0/content.mp4?solexpire=1592506156&solpathlen=148&soltoken=3907fa572b2fcbb1749752e886356b74&soltokenrule=c29sZXhwaXJlfHNvbHBhdGhsZW58c29sdXVpZA==&soluriver=2&soluuid=b2652e4b-a8fa-4f2c-935b-35655c97cb59&itemtypeid=34";
		try {
			postRequest(strUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getFileNameFromUrl(String strUrl) {
		String strFileNameFromUrl = "";
		if (strUrl == null || strUrl.equals("")) {
			return strUrl;
		}
		strFileNameFromUrl = strUrl;
		if (strFileNameFromUrl.contains("?")) {
			strFileNameFromUrl = strFileNameFromUrl.substring(0, strFileNameFromUrl.indexOf("?"));
		}
		if (strFileNameFromUrl.contains("/")) {
			strFileNameFromUrl = strFileNameFromUrl.substring(strFileNameFromUrl.lastIndexOf("/")+1);
		}
		System.out.println(strUrl);
		System.out.println(strFileNameFromUrl);
		return strFileNameFromUrl;
	}

	public void postRequest(String strUrl) throws IOException {
		URL url = new URL(strUrl);

		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		logger.debug("getResponseCode:" + httpURLConnection.getResponseCode());
		logger.debug("getContentEncoding:" + httpURLConnection.getContentEncoding());
		logger.debug("getContentType:" + httpURLConnection.getContentType());
		logger.debug("getContentLength:" + httpURLConnection.getContentLength());
		logger.debug("getContentLengthLong:" + httpURLConnection.getContentLengthLong());
		logger.debug("getContent:" + httpURLConnection.getContent());

		if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			// 파일 출력 스트림 생성
			try (InputStream is = httpURLConnection.getInputStream()) {
				// 파일 출력 스트림 생성
				String strFileName = USER_HOME + File.separator + "documents" + File.separator + strYmdhms + "_" + getFileNameFromUrl(strUrl) + ".html";
				
				FileOutputStream outputStream = new FileOutputStream(strFileName);
				
				// 파일 내용을 담을 버퍼(?) 선언
				byte[] readBuffer = new byte[1024];
				while (is.read(readBuffer, 0, readBuffer.length) != -1) {
					//버퍼 크기만큼 읽을 때마다 출력 스트림에 써준다.
					outputStream.write(readBuffer);
				}
				outputStream.flush();
				outputStream.close();
			}

		} else {
			// ... do something with unsuccessful response
		}
	}

	public static void main(String[] args) {
		new GomtvMp4Download();

	}

}
