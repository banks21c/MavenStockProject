package html.parsing.stock.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * User: Eugene Chipachenko
 * Date: 20.09.13
 * Time: 10:21
 */
public class BufferedFileWriter extends OutputStreamWriter {
	public BufferedFileWriter(String fileName) throws IOException {
		super(new FileOutputStream(fileName), Charset.forName("UTF-8"));
	}

	public BufferedFileWriter(String fileName, boolean append) throws IOException {
		super(new FileOutputStream(fileName, append), Charset.forName("UTF-8"));
	}

	public BufferedFileWriter(String fileName, String charsetName, boolean append) throws IOException {
		super(new FileOutputStream(fileName, append), Charset.forName(charsetName));
	}

	public BufferedFileWriter(File file) throws IOException {
		super(new FileOutputStream(file), Charset.forName("UTF-8"));
	}

	public BufferedFileWriter(File file, boolean append) throws IOException {
		super(new FileOutputStream(file, append), Charset.forName("UTF-8"));
	}

	public BufferedFileWriter(File file, String charsetName, boolean append) throws IOException {
		super(new FileOutputStream(file, append), Charset.forName(charsetName));
	}
}