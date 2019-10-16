package html.parsing.stock;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class GetURLConnectionSample {

    public static void main(String args[]) {

        URL u;
        URLConnection uc;

        try {
            u = new URL("http://finance.naver.com/item/coinfo.nhn?code=102280");
            try {
                uc = u.openConnection();
                uc.setDoInput(true);
                System.out.println("AllowUserInteraction       :" + uc.getAllowUserInteraction());

                System.out.println("ConnectTimeout             :" + uc.getConnectTimeout());
                System.out.println("ContentEncoding            :" + uc.getContentEncoding());
                System.out.println("ContentLength              :" + uc.getContentLength());
                System.out.println("ContentLengthLong          :" + uc.getContentLengthLong());
                System.out.println("Content                    :" + uc.getContent());
                System.out.println("ContentType                :" + uc.getContentType());
                System.out.println("Class                      :" + uc.getClass());
                // System.out.println("Content :"+uc.getContent(classes));
                System.out.println("Date                       :" + uc.getDate());
                System.out.println("DefaultAllowUserInteraction:" + uc.getDefaultAllowUserInteraction());
                System.out.println("DefaultUseCaches           :" + uc.getDefaultUseCaches());
                System.out.println("DoInput                    :" + uc.getDoInput());
                System.out.println("DoOutput                   :" + uc.getDoOutput());
                // System.out.println("DefaultRequestProperty
                // :"+uc.getDefaultRequestProperty(key));
                System.out.println("Expiration                 :" + uc.getExpiration());
                System.out.println("FileNameMap                :" + uc.getFileNameMap());
                System.out.println("HeaderField(0)             :" + uc.getHeaderField(0));
                System.out.println("HeaderField(1)             :" + uc.getHeaderField(1));
                System.out.println("HeaderField(2)             :" + uc.getHeaderField(2));
                System.out.println("HeaderField(3)             :" + uc.getHeaderField(3));
                System.out.println("HeaderField(4)             :" + uc.getHeaderField(4));
                System.out.println("HeaderField(5)             :" + uc.getHeaderField(5));
                System.out.println("HeaderField(6)             :" + uc.getHeaderField(6));
                System.out.println("HeaderField(7)             :" + uc.getHeaderField(7));
                System.out.println("HeaderField(8)             :" + uc.getHeaderField(8));
                System.out.println("HeaderField(9)             :" + uc.getHeaderField(9));
                System.out.println("HeaderField(10)             :" + uc.getHeaderField(10));
                System.out.println(
                        "HeaderField(\"Transfer-Encoding\")             :" + uc.getHeaderField("Transfer-Encoding"));
                System.out.println("HeaderField(\"Cache-Control\")             :" + uc.getHeaderField("Cache-Control"));
                System.out.println("HeaderField(\"null\")             :" + uc.getHeaderField("null"));
                System.out.println("HeaderField(\"Server\")             :" + uc.getHeaderField("Server"));
                System.out.println("HeaderField(\"Connection\")             :" + uc.getHeaderField("Connection"));
                System.out.println("HeaderField(\"Vary\")             :" + uc.getHeaderField("Vary"));
                System.out.println("HeaderField(\"Expires\")             :" + uc.getHeaderField("Expires"));
                System.out.println("HeaderField(\"P3P\")             :" + uc.getHeaderField("P3P"));
                System.out.println("HeaderField(\"Date\")             :" + uc.getHeaderField("Date"));
                System.out.println("HeaderField(\"Content-Type\")             :" + uc.getHeaderField("Content-Type"));
                // System.out.println("HeaderFieldDate :"+uc.getHeaderFieldDate(name, Default));
                // System.out.println("HeaderFieldKey :"+uc.getHeaderFieldKey(n));
                // System.out.println("HeaderFieldInt :"+uc.getHeaderFieldInt(name, Default));
                // System.out.println("HeaderFieldLong :"+uc.getHeaderFieldLong(name, Default));
                System.out.println("HeaderFields               :" + uc.getHeaderFields());
                System.out.println("IfModifiedSince            :" + uc.getIfModifiedSince());
                System.out.println("LastModified               :" + uc.getLastModified());
                System.out.println("InputStream                :" + uc.getInputStream());
                // System.out.println("OutputStream :"+uc.getOutputStream());
                System.out.println("ReadTimeout                :" + uc.getReadTimeout());
                // System.out.println("RequestProperty :"+uc.getRequestProperty(key));
                // System.out.println("RequestProperties :"+uc.getRequestProperties());
                System.out.println("UseCaches                  :" + uc.getUseCaches());
                // System.out.println("System.out.println
                // :"+uc.guessContentTypeFromName(fname));
                // System.out.println("System.out.println :"+uc.guessContentTypeFromStream(is));
                System.out.println("URL                        :" + uc.getURL());

            } catch (IOException e) {
                System.err.println(e);
            }
        } catch (MalformedURLException e) {
            System.err.println(e);
        }

    }

}
