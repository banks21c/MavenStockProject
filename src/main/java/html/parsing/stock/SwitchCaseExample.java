package html.parsing.stock;

public class SwitchCaseExample {

    public static void main(String[] args) {

        // int형으로 조건 지정
        int i = 365;

        switch (i) {
            case 66:
                System.out.println("66 이라는 정수입니다.");
                break;
            case 365:
                System.out.println("365 라는 정수입니다.");
                break;
            case 1000:
                System.out.println("1000 이라는 정수입니다.");
                break;
            default:
                System.out.println("해당 숫자가 없습니다");
                break;
        }
        // 출력 결과: 365 라는 정수입니다.
        switch (i) {
            case 66:
                System.out.println("66 이라는 정수입니다.");
            case 365:
                System.out.println("365 라는 정수입니다.");
            case 1000:
                System.out.println("1000 이라는 정수입니다.");
            default:
                System.out.println("해당 숫자가 없습니다");
        }
        // 365 라는 정수입니다.
        // 1000 이라는 정수입니다.
        // 해당 숫자가 없습니다

        // char 형으로 조건 지정
        char c = 'A';

        switch (c) {
            case 'A':
                System.out.println("'A' 라는 문자입니다.");
                break;
            case '똠':
                System.out.println("'똠' 이라는 문자입니다.");
                break;
            case '7':
                System.out.println("'7' 이라는 문자입니다.");
                break;
            default:
                System.out.println("해당되는 문자가 없습니다");
                break;
        }
        // 출력 결과: 'A' 라는 문자입니다.
        switch (c) {
            case 'A':
                System.out.println("'A' 라는 문자입니다.");
            case '똠':
                System.out.println("'똠' 이라는 문자입니다.");
            case '7':
                System.out.println("'7' 이라는 문자입니다.");
            default:
                System.out.println("해당되는 문자가 없습니다");
        }
        // 'A' 라는 문자입니다.
        // '똠' 이라는 문자입니다.
        // '7' 이라는 문자입니다.
        // 해당되는 문자가 없습니다
    }
}
