/**
 *
 */
package html.parsing.stock;

/**
 * @author banks
 *
 */
public class MakeKospiKosdaqListCall {

    public static void main(String args[]) {
        MakeKospiKosdaqListThread list = new MakeKospiKosdaqListThread();
        try {
            while (list.isAlive()) {
                System.out.println("Main thread will be alive till the child thread is live");
                Thread.sleep(1500);
            }
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }
        System.out.println("Main thread's run is over");
    }
}
