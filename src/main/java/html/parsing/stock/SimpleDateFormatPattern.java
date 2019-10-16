/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author parsing-25
 */
/*
기호 의미 보기 
 G  연대(BC, AD)   AD  
 y  년도  2009 
 M  월 (1~12월 또는 1월~12월)  10또는 10월, OCT 
 w  년의 몇 번째 주(1~53)  50 
 W  월의 몇 번째 주(1~5)  4 
 D  년의 몇 번째 일(1~366)  100 
 d  월의 몇 번째 일(1~31)  15 
 F  월의 몇번째 요일(1~5)  1 
 E  요일  월 
 a  오전/오후(AM, PM)  PM 
 H  시간(0~23)  20 
 k  시간(1~24)  12 
 K  시간(0~11)  10 
 h  시간(1~12)  11 
 m  분(0~59)  35 
 s  초(0~59)  55 
 S  천분의 1초(0~999)  253 
 z  Time zone(General time zone)  GMT+9:00 
 Z  Time zone(RFC 822 time zone)  +0900 
'  escape문자(특수문자를 표현하는데 사용)  없음 
 */
public class SimpleDateFormatPattern {

    public static void main(String[] args) {
        Date today = new Date();

        SimpleDateFormat sdf0, sdf1, sdf2, sdf3, sdf4;
        SimpleDateFormat sdf5, sdf6, sdf7, sdf8, sdf9;

        sdf0 = new SimpleDateFormat("yyyyMMdd");
        sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        sdf2 = new SimpleDateFormat("''yy년 MM월 dd일 E요일");
        sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sdf4 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

        sdf5 = new SimpleDateFormat("오늘은 올 해의 D번째 날입니다.");
        sdf6 = new SimpleDateFormat("오늘은 이 달의 d번째 날입니다.");
        sdf7 = new SimpleDateFormat("오늘은 올 해의 w번째 주입니다.");
        sdf8 = new SimpleDateFormat("오늘은 이 달의 W번째 주입니다.");
        sdf9 = new SimpleDateFormat("오늘은 이 달의 F번째 E요일입니다.");

        System.out.println(sdf0.format(today));
        System.out.println(sdf1.format(today)); // format(Date d)
        System.out.println(sdf2.format(today));
        System.out.println(sdf3.format(today));
        System.out.println(sdf4.format(today));
        System.out.println();
        System.out.println(sdf5.format(today));
        System.out.println(sdf6.format(today));
        System.out.println(sdf7.format(today));
        System.out.println(sdf8.format(today));
        System.out.println(sdf9.format(today));
    }
}
