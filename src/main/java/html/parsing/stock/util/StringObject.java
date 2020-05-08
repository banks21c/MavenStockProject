package html.parsing.stock.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


public class StringObject {
	//----------------------------------------------------------------->>
	//	Instance Control Block
	protected static Object ok=null;
	protected static Object newInstance(Object ... objects)	{
		return new StringObject(objects);
	}
	protected static Object getInstance(Object ... objects)	{
		return (ok==null ? (ok=newInstance(objects)) : ok);
	}
	//----------------------------------------------------------------->>
	public static StringObject ik=null;
	/** NewInstance */
	public static StringObject ni(Object ... objects)	{	return (StringObject)newInstance(objects);	}
	/** SingleInstance */
	public static StringObject si(Object ... objects)	{	return (ik=(StringObject)getInstance(objects));	}
	//----------------------------------------------------------------->>
	protected void _initialize(Object ... objects)	{
	}
	protected void _destroy(Object ... objects) {
	}
	public void StringObject_new(Object ... objects)	{
		_initialize(objects);
	}
	public void StringObject_delete(Object ... objects)	{
		_destroy(objects);
	}
	//----------------------------------------------------------------->>
	/** Default Constructor */
	public StringObject()	{	StringObject_new(new Object[0]);	}
	public StringObject(Object ... objects)	{
		StringObject_new(objects);
	}
	/**
	 * StringObject
	 * @param args Arguments
	 */
	public static void main(String ... args) {
		Object[] objects=(Object[])args;

		StringObject.getInstance(objects);
		StringObject.si(objects).StringObject_test(objects);
		StringObject.si(objects)._destroy(objects);

		return;
	}
	/** Class Test Module */
	public void StringObject_test(Object ... objects)	{
		String aaa = "1";
		for (int i = 0; i < 1000; i++) {
			System.out.println(StringObject.si(objects).getPaddingString(""+i, 3, "0", "-"));
		}

		String bbb = "ajhskd${TTTT}jkjjjj${BBB}aaaaa";
		String ccc = StringObject.si(objects).convertString(bbb, "TTTT", null);
		System.out.println(ccc);
		ccc = StringObject.si(objects).convertString(ccc, "BBB", null);
		System.out.println(ccc);

		String sCodeValue = "code11=value11;code12=value12;code21=value21;code22=value22";
		System.out.println("value=" + StringObject.si(objects).getCodeValue(sCodeValue, "code12", null));
		System.out.println("sCodeValue=" + sCodeValue);
	}
	//----------------------------------------------------------------->>
	//----------------------------------------------------------------->>


	public String[] subSplit(String src, String regex, int limit)	{
		String srcs[]=src.split(regex);

		if(srcs.length>limit)	{
			regex=regex.replace("\\", "");
			String rets[]=new String[limit];
			StringBuffer sb=new StringBuffer();
			int j=srcs.length-rets.length;
			for(int i=0 ; i<=j ; i++)	{
				if(i>0)	sb.append(regex);
				sb.append(srcs[i]);
			}
			rets[0]=sb.toString();
			for(int i=1 ; i<rets.length ; i++)	rets[i]=srcs[j+i];
			return rets;
		}

		return srcs;
	}

	public List<String> exactSplit(String sSource, String sSeperator) {
		if (sSource == null)		throw new NullPointerException("sSource is empty or null!!!");
		if (sSeperator == null || sSeperator.trim().length() == 0)	throw new NullPointerException("sSeperator is empty or null!!!");
		List lData = new ArrayList<String>();
		int iCurrPos = 0;
        for (int iNextPos = 0; (iNextPos = sSource.indexOf(sSeperator,iCurrPos)) >= 0; iCurrPos = iNextPos + 1) {
        	lData.add(sSource.substring(iCurrPos,iNextPos));
        	iCurrPos = iNextPos + 1;
        }
        lData.add(sSource.substring(iCurrPos).replaceAll(sSeperator, ""));
        return lData;
	}

	//----------------------------------------------------------------->>

	public String toEncoding(String src, String targetEncoding)	{
		return toEncoding(src, targetEncoding, null);	}
	/**
	 * @param src 변환할 문자열
	 * @param targetEncoding 타겟 인코딩
	 * @param srcEncoding 소스 인코딩
	 * @return
	 */
	public String toEncoding(String src, String targetEncoding, String srcEncoding)	{
		if(targetEncoding.equals(srcEncoding))	return src;
		try	{
			if(srcEncoding==null || srcEncoding=="")
					return new String(src.getBytes(), targetEncoding);
			else	return new String(src.getBytes(srcEncoding), targetEncoding);
		}
		catch(Exception e)	{
			return src;
		}
	}

	public String checkEncoding(byte[] b)	{
		String encoding="EUC-KR";
		if( (b[0]&0xFF)==0xEF && (b[1]&0xFF)==0xBB	&& (b[2]&0xFF) == 0xBF)	encoding="UTF-8";
		else if( (b[0]&0xFF)==0xFE && (b[1]&0xFF)==0xFF )	encoding="UTF-16BE";
		else if( (b[0]&0xFF)==0xFF && (b[1]&0xFF)==0xFE )	encoding="UTF-16LE";
		else if( (b[0]&0xFF)==0x00 && (b[1]&0xFF)==0x00	&& (b[0]&0xFF)==0xFE && (b[1]&0xFF)==0xFF )	encoding="UTF-32BE";
		else if( (b[0]&0xFF)==0xFF && (b[1]&0xFF)==0xFE && (b[0]&0xFF)==0x00 && (b[1]&0xFF)==0x00 )	encoding="UTF-32LE";
		return encoding;
	}

	public String findEncoding(byte[] b, String ... encodings)	{
		if(encodings.length==0)	encodings= new String[]{"UTF-8", "EUC-KR"};
		CharsetDecoder cdecoder;
		CharBuffer cbuffer;
		for(int i=0 ; i<encodings.length ; i++)	{
			cdecoder = Charset.forName(encodings[i]).newDecoder();
			try	{
				cbuffer = cdecoder.decode(ByteBuffer.wrap(b));
				cbuffer.toString();
				return encodings[i];
			}
			catch (CharacterCodingException e)	{  }
		}
		return null;
	}
	public String findEncoding(String str, String ...encodings)	{	return findEncoding(str.getBytes(), encodings);	}

	public String changeEncoding(byte[] b, String ...encodings)	{
		if(encodings.length==0)	encodings= new String[]{"UTF-8", "EUC-KR"};
		CharsetDecoder decoder;
		CharBuffer cb;
		for(int i=0 ; i<encodings.length ; i++)	{
			decoder = Charset.forName(encodings[i]).newDecoder();
			try	{
				cb = decoder.decode(ByteBuffer.wrap(b));
				return cb.toString();
			}
			catch (CharacterCodingException e)	{}
		}
		return null;
	}
	public String changeEncoding(String val, String ...encodings)	{	return changeEncoding(val.getBytes(), encodings);	}

	//----------------------------------------------------------------->>
	//	BASE64 encoding
	public String b64encode(String str){
		String result = null;
		sun.misc.BASE64Encoder b64e = new sun.misc.BASE64Encoder();
		try {
			byte[] b = str.getBytes();
			result = b64e.encode(b);
		}
		catch (Exception e) {	System.err.println(e);	}
		return result;
	}

	//	BASE64 decoding
	public byte[] b64decode(String str){
		byte[] b = null;
		sun.misc.BASE64Decoder b64d = new sun.misc.BASE64Decoder();
		try {
			b = b64d.decodeBuffer(str);
		}
		catch (Exception e) {	System.err.println(e);	}
		return b;
	}

	//	BASE64 decoding
	public String b64decode(String str, String sCharSet){
		String result = null;
		sun.misc.BASE64Decoder b64d = new sun.misc.BASE64Decoder();
		try {
			byte[] b = b64d.decodeBuffer(str);
			if (sCharSet == null)	result = new String(b);
			else					result = new String(b, sCharSet);
		}
		catch (Exception e) {	System.err.println(e);	}
		return result;
	}

	//----------------------------------------------------------------->>

	/**
	 * @param src 소스 문자열
	 * @param fs 찾을 문자열 앞에서 부터 찾는다.
	 * @return 처음 부터 찾은 문자열까지 반환
	 */
	public String substringBefore(String src, String fs)	{
		try	{	return src.substring(0, src.indexOf(fs));	}
		catch(Exception e)	{	return "";	}
	}
	/**
	 * @param src 소스 문자열
	 * @param fs 찾을 문자열 뒤에서 부터 찾는다.
	 * @return 처음 부터 찾은 문자열까지 반환
	 */
	public String substringBeforeLast(String src, String fs)	{
		try	{	return src.substring(0, src.lastIndexOf(fs));	}
		catch(Exception e)	{	return "";	}
	}
	/**
	 * @param src 소스 문자열
	 * @param fs 찾을 문자열 앞에서 부터 찾는다.
	 * @return 찾은 문자열 부터 끝까지의 문자열을 반환
	 */
	public String substringAfter(String src, String fs)	{
		try	{	return src.substring(src.indexOf(fs)+fs.length(), src.length());	}
		catch(Exception e)	{	return "";	}
	}
	/**
	 * @param src 소스 문자열
	 * @param fs 찾을 문자열 뒤에서 부터 찾는다.
	 * @return 찾은 문자열 부터 끝까지의 문자열을 반환
	 */
	public String substringAfterLast(String src, String fs)	{
		try	{	return src.substring(src.lastIndexOf(fs)+fs.length(), src.length());	}
		catch(Exception e)	{	return "";	}
	}

	//----------------------------------------------------------------->>

	public int string2int(String src, int dftValue)	{
		try	{	return Integer.parseInt(src);	}
		catch(Exception e)	{	return dftValue;	}
	}
	public int string2int(String src)	{	return string2int(src, 0);	}

	public long string2long(String src, long dftValue)	{
		try	{	return Long.parseLong(src);	}
		catch(Exception e)	{	return dftValue;	}
	}
	public long string2long(String src)	{	return string2long(src, 0L);	}

	//----------------------------------------------------------------->>

    public String javaEncode(String s) {
        StringBuilder buff = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
            /**
            case '\b':	// BS backspace : not supported in properties files
                buff.append("\\b");
                break;
            /**/
            case '\t':	// HT horizontal tab
                buff.append("\\t");
                break;
            case '\n':	// LF linefeed
                buff.append("\\n");
                break;
            case '\f':	// FF form feed
                buff.append("\\f");
                break;
            case '\r':	// CR carriage return
                buff.append("\\r");
                break;
            case '"':	// double quote
                buff.append("\\\"");
                break;
            case '\\':	// backslash
                buff.append("\\\\");
                break;
            default:
                int ch = c & 0xffff;
                if (ch >= ' ' && (ch < 0x80))	buff.append(c);
                else	{
                    buff.append("\\u");
                    // make sure it's four characters
                    buff.append(Integer.toHexString(0x10000 | ch).substring(1));
                }
            }
        }
        return buff.toString();
    }

	public String javaDecode(String s) throws Exception {
        StringBuilder buff = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"') {
                break;
            } else if (c == '\\') {
                if (i >= s.length()) {
                    throw getFormatException(s, s.length() - 1);
                }
                c = s.charAt(++i);
                switch (c) {
                case 't':	buff.append('\t');	break;
                case 'r':	buff.append('\r');	break;
                case 'n':	buff.append('\n');	break;
                case 'b':	buff.append('\b');	break;
                case 'f':	buff.append('\f');	break;
                case '#':	// for properties files
                    buff.append('#');
                    break;
                case '=':	// for properties files
                    buff.append('=');
                    break;
                case ':':	// for properties files
                    buff.append(':');
                    break;
                case '"':	buff.append('"');	break;
                case '\\':	buff.append('\\');	break;
                case 'u': {
                    try {
                        c = (char) (Integer.parseInt(s.substring(i + 1, i + 5), 16));
                    } catch (NumberFormatException e) {
                        throw getFormatException(s, i);
                    }
                    i += 4;
                    buff.append(c);
                    break;
                }
                default:
                    if (c >= '0' && c <= '9') {
                        try {
                            c = (char) (Integer.parseInt(s.substring(i, i + 3), 8));
                        } catch (NumberFormatException e) {
                            throw getFormatException(s, i);
                        }
                        i += 2;
                        buff.append(c);
                    } else {
                        throw getFormatException(s, i);
                    }
                }
            } else {
                buff.append(c);
            }
        }
        return buff.toString();
    }

	public String addAsterisk(String s, int index) {
        if (s != null && index < s.length()) {
            s = s.substring(0, index) + "[*]" + s.substring(index);
        }
        return s;
    }

	private Exception getFormatException(String s, int i) {
        //return Message.getSQLException(ErrorCode.STRING_FORMAT_ERROR_1, addAsterisk(s, i));
        return  new Exception(addAsterisk(s, i));
    }

    //----------------------------------------------------------------->>

	/**
	 * 패턴 체그
	 * @param pattern
	 * @param check
	 * @return
	 */
	public boolean checkPattern(String pattern, String check)	{
		/*
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(check);
		return m.matches();
		/**/
		return Pattern.matches(pattern, check);
	}

	/**
	 * 숫자 포맷 체크
	 * @param numPattern
	 * @param number
	 * @return
	 */
	public boolean checkNumber(String numPattern, String number)	{
		numPattern=numPattern.replaceAll("\\?", "(?:[0-9])");
		numPattern=numPattern.replaceAll("\\*", "(?:[0-9])*");
		return Pattern.matches(numPattern, number);
	}

	//----------------------------------------------------------------->>

	/**
	 * 문자열이 null이거나 "" 이면 null을 리턴, 그렇지 않으면 공백을 없앤 후 리턴한다.
     *
	 * @param sOrg 소스문자열
	 *
	 * @return 문자열 또는 소스문자열에서 공백을 없앤 문자열
	 */
	public String nvl(String sOrg) {
		return nvl(sOrg, null);
	}

	/**
	 * 문자열이 null이거나 "" 이면 지정문자열을 리턴, 그렇지 않으면 공백을 없앤 후 리턴한다.
     *
	 * @param sOrg 소스문자열
	 * @param initValue sOrg가 널이거나 빈 문자열일 경우 초기값으로 사용할 문자열
	 *
	 * @return 문자열 또는 소스문자열에서 공백을 없앤 문자열
	 */
	public String nvl(String sOrg, String initValue) {
		return sOrg==null?initValue:sOrg.trim().length()==0?initValue:sOrg.trim();
	}

	public String convertString(String scriptString, String sTagName, String repString) {
		return convertString(scriptString, sTagName, repString, true, "${", "}");
	}

	public String convertString(String scriptString, String sTagName, String repString, boolean bRemoveTag) {
		return convertString(scriptString, sTagName, repString, bRemoveTag, "${", "}");
	}

	public String convertString(String scriptString, String sTagName, String repString, boolean bRemoveTag, String open, String close) {
		scriptString = nvl(scriptString);
		if(scriptString==null)	return null;
		sTagName = nvl(sTagName);
		if(sTagName==null)	return null;
		if(close==null)	close=open;
		String skey = null;
		String svalue = bRemoveTag?nvl(repString, ""):nvl(repString);
		boolean bConverted = false;
		for(int i=0, j=0 ; (i=scriptString.indexOf(open, i))>=0  && i<scriptString.length() ; )	{
			j=scriptString.indexOf(close, i);
			if(j<0 || j>scriptString.length())	break;
			skey=scriptString.substring(i+open.length(), j);
			if(sTagName.equals(skey) && svalue!=null)	{
				scriptString = scriptString.substring(0, i) + svalue + scriptString.substring(j+close.length(), scriptString.length());
				bConverted = true;
			} else	{
				i = j + close.length();
			}
		}
		return bConverted?scriptString:null;
	}

	public String convertString(String scriptString, Map mData) {
		return  convertString(scriptString, mData, true, "${", "}");
	}

	public String convertString(String scriptString, Map mData, boolean bRemoveTag) {
		return  convertString(scriptString, mData, bRemoveTag, "${", "}");
	}

	/**
	 * @param src 컨버젼할 소스 문자열
	 * @param mData 키값을 밸류로 변환할 맵 인스턴스
	 * @param open 오픈 태그 스트링  default("${")
	 * @param close 클로우즈 태그 스트링 default("}")
	 * @return
	 */
	public String convertString(String src, Map mData, boolean bRemoveTag, String open, String close)	{
		src = nvl(src);
		if(src==null)	return null;
		if(mData==null || mData.isEmpty())	return null;
		if(close==null)	close=open;

		boolean bConverted = false;
		String skey=null, svalue=null;
		Object oValue = null;
		for(int i=0, j=0 ; (i=src.indexOf(open, i))>=0  && i<src.length() ; )	{
			j=src.indexOf(close, i);
			if(j<0 || j>src.length())	break;
			skey=src.substring(i+open.length(), j);
			oValue = mData.get(skey);
			svalue = bRemoveTag?oValue==null?"":""+oValue:oValue==null?null:""+oValue;
//			svalue = bRemoveTag?nvl((String)mData.get(skey), ""):nvl((String)mData.get(skey));
			if(svalue!=null) { // replace
				src = src.substring(0, i) + svalue + src.substring(j+close.length(), src.length());
				bConverted = true;
			} else	{ // jump
				i = j + close.length();
			}
		}
		return bConverted?src:null;
	}

	public String convertString(String scriptString, List<Map> lData) {
		return  convertString(scriptString, lData, true, "", "${", "}");
	}

	public String convertString(String scriptString, List<Map> lData, boolean bRemoveTag) {
		return  convertString(scriptString, lData, bRemoveTag, "", "${", "}");
	}

	public String convertString(String scriptString, List<Map> lData, String sLineSep) {
		return  convertString(scriptString, lData, true, sLineSep, "${", "}");
	}

	public String convertString(String scriptString, List<Map> lData, boolean bRemoveTag, String sLineSep) {
		return  convertString(scriptString, lData, bRemoveTag, sLineSep, "${", "}");
	}

	/**
	 * @param src 컨버젼할 소스 문자열
	 * @param lData 키값을 밸류로 변환할 맵 리스트 인스턴스
	 * @param bRemoveTag 키에 대한 값이 없을 경우 태그 문자열(open, close)을 제거할지 여부
	 * @param sLineSep 라인 구분자
	 * @param open 오픈 태그 스트링  default("${")
	 * @param close 클로우즈 태그 스트링 default("}")
	 * @return
	 */
	public String convertString(String src, List<Map> lData, boolean bRemoveTag, String sLineSep, String open, String close)	{
		if (lData == null || lData.size() < 1)	return null;
		String sResult = null;
		for (int i = 0; i < lData.size(); i++)
			if (sResult != null)	sResult = sResult + convertString(src, lData.get(i), bRemoveTag, open, close) + sLineSep;
			else					sResult = convertString(src, lData.get(i), bRemoveTag, open, close) + sLineSep;
		return sResult;
	}

	public String getListMapToTag(List<Map> lData, String sListTagName) {
		return getListMapToTag(lData, sListTagName, false, "");
	}

	public String getListMapToTag(List<Map> lData, String sListTagName, Object oDefValue) {
		return getListMapToTag(lData, sListTagName, false, oDefValue);
	}

	public String getListMapToTag(List<Map> lData, String sListTagName, boolean bRemoveNullLine, Object oDefValue) {
		sListTagName = nvl(sListTagName, null);
		if (sListTagName == null)	throw new IllegalArgumentException("List tag name is null!!!");
		if (lData == null)			throw new IllegalArgumentException("List data is null!!!");
		Map mData = null; String sRowData = null;
		Object oKey = null, oValue = null;
		boolean bHasData = false;
		StringBuffer sbTags = new StringBuffer();
		for (int i = 0; i < lData.size(); i++) {
			mData = lData.get(i);
			if (mData != null && !mData.isEmpty()) {
				sRowData = getMapToTag(mData, bRemoveNullLine, oDefValue);
				if (sRowData != null)	{
					sbTags.append("\t\t<").append(sListTagName).append(">\n").append(sRowData).append("\t\t</").append(sListTagName).append(">\n");
					bHasData = true;
				}
			}
		}
		return bHasData?sbTags.toString():null;
	}

	public String getMapToTag(Map mData) {
		return getMapToTag(mData, false, "");
	}

	public String getMapTagString(Map mData, Object oDefValue) {
		return getMapToTag(mData, false, oDefValue);
	}

	public String getMapToTag(Map mData, boolean bRemoveNullLine, Object oDefValue) {
		if (mData == null)	throw new IllegalArgumentException("Map data is null!!!");
		boolean bHasData = false;		String sKey = null;		Object oValue = null;
		StringBuffer sbTags = new StringBuffer();
		Iterator it = mData.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        sKey = (((String)pairs.getKey()).trim()).toLowerCase();
	        oValue = pairs.getValue();
	        if (bRemoveNullLine && oValue==null) continue;
	        if (sKey.endsWith("_hidden"))	continue;
	        oValue = oValue==null?oDefValue:oValue;
	        if (oValue instanceof String) oValue = ((String) oValue).trim();
	        sbTags.append("\t\t\t<").append(sKey).append("><![CDATA[").append(oValue).append("]]></").append(sKey).append(">\n");
	        bHasData = true;
	    }
		return bHasData?sbTags.toString():null;
	}

	/**
	 * sBuf문자열에 sP문자를 length길이만큼 정렬하여 채운 문자열을 얻는다.
	 * <br><b>Warning : 한글처리</b>
	 * @param sBuf 소스문자열
	 * @param length 채울문자 길이
	 * @param sP 채울문자
	 * @param sPos 정렬기준("+" : 우측정렬, "-" : 좌측정렬)
	 * @return sBuf문자열에 sP문자를 length길이만큼 정렬하여 채운 문자열
	 * @since 1.0
	 */
	public String getPaddingString(String sBuf, int length, String sP, String sPos)
	{
		String	sConv = "";
		if(sPos.equals("-")){
			for(int i=0; i<length; i++){
				if(i < sBuf.length())	sConv += sBuf.substring(i, i+1);
				else					sConv += sP;
			}
		} else if(sPos.equals("+")) {
			int i;
			for(i=0; i<length-sBuf.length(); i++)	sConv += sP;
			for(int j=0; i<length; i++, j++)		sConv += sBuf.substring(j, j+1);
		}
		return sConv;
	}

	/**
	 * mac 구조를 3C-5A-37-0E-D4-19 -> 3C:5A:37:0E:D4:19 변경한다.
	 *
	 * @param mac
	 * @return
	 * @throws Exception
	 */
	public String replaceMac(String mac) throws Exception {
	    return StringUtils.replace(StringUtils.trimToEmpty(mac), "-", ":");
	}

	/**
	 * 코드에 해당하는 값을 리턴한다.
	 *
	 * @param sCodeValueString 코드와 값은 등호(=)로 각 코드와 값의 쌍은 세미콜론으로 구분된 문자열(code1=value1;code2=value2)
	 * @param sCode 코드
	 * @param sDefault	디폴트 값
	 * @return
	 * @throws Exception
	 */
	public String getCodeValue(String sCodeValueString, String sCode, String sDefault) {
		String sResult = null;
		sCode = nvl(sCode);
		if (sCode == null)	return sResult;
		sCodeValueString = nvl(sCodeValueString);
		if (sCodeValueString == null)	return sResult;
		int iPosition = sCodeValueString.indexOf(sCode);
		while (iPosition >= 0) {
			if ("=".equals(sCodeValueString.substring(iPosition+sCode.length(), iPosition+sCode.length()+1))) {
				sResult = sCodeValueString.substring(iPosition+sCode.length()+1);
				iPosition = sResult.indexOf(";");
				if (iPosition > 0)	{
					sResult = sResult.substring(0, iPosition);
					iPosition = -1;
				}
			} else {
				sCodeValueString = sCodeValueString.substring(iPosition+sCode.length());
				iPosition = sCodeValueString.indexOf(sCode);
			}
		}
		return sResult==null?sDefault:sResult;
	}
}
