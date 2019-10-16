package html.parsing.stock;

import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

public class Crawler implements Externalizable {

    private Vector vToSearch;
    private Map mSearched;
    private int MAXPAGES;
    private int nWaiting;
    private int nVisited;
    private int nExisted;

    public Crawler() {
        nExisted = 0;
        nVisited = 0;
        vToSearch = new Vector();
        mSearched = new HashMap();
        System.out.println("constructor called");
    }

    public void setMax(int max) {
        MAXPAGES = max;
    }

    /**
     * Establish a http connection between Crawler and Internet, send request to
     * the URL and the return HTML page String.
     *
     * @param url - the HTML page url going to request
     * @return String - the HTML contents in String format
     *
     */
    String HTTPConnection(URL url) {

        String strContent = null;

        // we only search for http
        if (url.getProtocol().compareTo("http") != 0) {
            return strContent;
        }

        try {
            // try opening the URL
            URLConnection urlConnection = url.openConnection();

            urlConnection.setAllowUserInteraction(false);

            InputStream urlStream = url.openStream();

            String type = urlConnection.getContentType();
            // we only search for html file right now
            if (type == null) {
                return strContent;
            }

            if (type.compareTo("text/html") != 0) {
                return strContent;
            }

            // get the input stream 1000 bytes by 1000 bytes
            byte b[] = new byte[1000];
            int numRead = urlStream.read(b);

            if (numRead != -1) {
                strContent = new String(b, 0, numRead);
                while (numRead != -1) {
                    numRead = urlStream.read(b);
                    if (numRead != -1) {
                        String newContent = new String(b, 0, numRead);
                        strContent += newContent;
                    }
                }
            }

            urlStream.close();
        } catch (IOException e) {
            System.out.println("ERROR: couldn't open URL " + url.toString());
            return strContent;
        }

        return strContent;

    }

    /**
     * Parse a HTML page, find all the links (fully qualified) in the page, push
     * links into the back of WAITING queue one by one if no duplication is
     * found, save HTML into files and push parsed link and saved file name into
     * COMPLETE hash table for index.
     *
     * @param strURL - the URL
     * @return true - if get, save, index HTML page successfully false - if any
     * of above goes wrong
     *
     */
    boolean HTMLParse(String strURL) {

        URL url;
        try {
            url = new URL(strURL);
        } catch (MalformedURLException e) {
            System.out.println("ERROR: invalid URL " + strURL);
            return false;
        }

        String htmlContent = HTTPConnection(url);
        if (htmlContent != null) {
            String lowerCaseContent = htmlContent.toLowerCase();

            int index = 0;
            while ((index = lowerCaseContent.indexOf("<a", index)) != -1) {
                if ((index = lowerCaseContent.indexOf("href", index)) == -1) {
                    break;
                }
                if ((index = lowerCaseContent.indexOf("=", index)) == -1) {
                    break;
                }

                index++;
                String remaining = htmlContent.substring(index);

                StringTokenizer st = new StringTokenizer(remaining, " \t\n\r\">#");
                String strLink = st.nextToken();

                URL urlLink;
                try {
                    urlLink = new URL(url, strLink);
                    strLink = urlLink.toString();
                    if (strLink.endsWith("/")) {
                        strLink = strLink.substring(0, strLink.length() - 1);
                    }
                    if (strLink.equalsIgnoreCase(strURL)) {
                        continue;
                    }

                } catch (MalformedURLException e) {
                    System.out.println("ERROR: bad URL " + strLink);
                    continue;
                }

                // check duplicated
                if (!IsVisited(strLink) && !IsWaiting(strLink)) {
                    vToSearch.addElement(strLink);
                }

            }
            if (!IsVisited(strURL)) {
                mSearched.put(strURL, htmlContent);
                nVisited++;
            }

            return true;
        }

        return false;
    }

    private boolean IsWaiting(String strURL) {
        return vToSearch.contains(strURL);
    }

    private boolean IsVisited(String strURL) {

        return mSearched.containsKey(strURL);
    }

    public Map getmSearched() {
        return mSearched;
    }

    public int getCachedSize() {
        return mSearched.size();
    }

    public void run(String init) {

        System.out.println(mSearched.size());
        URL url;
        try {
            if (init.endsWith("/")) {
                init = init.substring(0, init.length() - 1);
            }
            url = new URL(init);
            System.out.println("url:" + url);
        } catch (MalformedURLException e) {
            System.out.println("ERROR: invalid URL " + init);
        }

        vToSearch.addElement(init);

        while (vToSearch.size() > 0) {
            String strURL = (String) vToSearch.elementAt(0);
            vToSearch.remove(0);
            HTMLParse(strURL);

            if (nVisited >= MAXPAGES) {
                break;
            }
        }

    }

    public Map doSearch(String keyword) {

        if (mSearched == null || mSearched.size() == 0) {
            return null;
        }
        keyword = keyword.toLowerCase();
        Map find = new HashMap();
        Set keys = mSearched.keySet();
        Iterator key = keys.iterator();
        while (key.hasNext()) {
            String k = (String) key.next();
            String v = (String) mSearched.get(k);
            if (v != null) {
                String lv = v.toLowerCase();
                if (lv.indexOf(keyword) != -1) {
                    find.put(k, v);
                }
            }
        }

        return find;

    }

    public void PrintVisited() {
        Set keys = mSearched.keySet();
        Iterator k = keys.iterator();

        System.out.println(mSearched.size() + " have been visited.");
        while (k.hasNext()) {
            System.out.println(k.next());
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        mSearched = (Map) in.readObject();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(mSearched);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Crawler c = null;
        // String initURL = args[0];
        String initURL = "http://www.huffingtonpost.kr/2017/04/19/story_n_16090016.html";

        File file = new File("result.cra");
        if (file.exists()) {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            c = (Crawler) in.readObject();
        } else {
            c = new Crawler();
        }
        c.setMax(100);
        c.run(initURL);

        ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream("result.cra"));
        o.writeObject(c);
        o.close();

        c = null;

        File file1 = new File("result.cra");
        if (file.exists()) {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file1));
            System.out.println("in:" + in);
            c = (Crawler) in.readObject();
            System.out.println("c:" + c);
        } else {
            System.out.println("There is no crawler result!");
            System.exit(1);
        }

        Map find = c.doSearch("project");
        System.out.println(find.keySet());
        System.out.println(find.size());

    }

}
