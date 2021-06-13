
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class StringZipper {

//GZIPOutputStream을 이용하여 문자열 압축하기
	public byte[] zipStringToBytes(String input) throws IOException {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(gzipOutputStream);
		bufferedOutputStream.write(input.getBytes());

		bufferedOutputStream.close();
		byteArrayOutputStream.close();

		return byteArrayOutputStream.toByteArray();
	}

//GZIPInputStream을 이용하여 byte배열 압축해제하기
	public String unzipStringFromBytes(byte[] bytes) throws IOException {

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(gzipInputStream);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		byte[] buffer = new byte[100];

		int length;
		while ((length = bufferedInputStream.read(buffer)) > 0) {
			byteArrayOutputStream.write(buffer, 0, length);
		}

		bufferedInputStream.close();
		gzipInputStream.close();
		byteArrayInputStream.close();
		byteArrayOutputStream.close();

		return byteArrayOutputStream.toString();
	}

//테스트
	public static void main(String[] args) throws IOException {

		StringZipper sz = new StringZipper();

		byte[] tmp = sz.zipStringToBytes("TEST");
		String str = sz.unzipStringFromBytes(tmp);

		System.out.println(str);
	}
}
