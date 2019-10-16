/**
 *
 */
package html.parsing.stock;

/**
 * @author banks
 *
 */
public class ArithmeticExceptionTest {

    /**
     *
     */
    public ArithmeticExceptionTest() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        System.out.println(0 / 1);
        System.out.println(1 / 0);//Exception in thread "main" java.lang.ArithmeticException: / by zero
    }

}
