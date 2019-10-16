/**
 *
 */
package html.parsing.stock;

/**
 * @author banks
 *
 */
public class MakeDaumKospiKosdaqListCall {

    MakeDaumKospiKosdaqListCall() {
        System.out.println(this.getClass().getSimpleName() + " Constructor");
        long startTime = System.currentTimeMillis();
        try {

            MakeDaumKospiKosdaqListThreadNew list1 = new MakeDaumKospiKosdaqListThreadNew(1, startTime);
            list1.start();
            Thread.sleep(2000);
            MakeDaumKospiKosdaqListThreadNew list2 = new MakeDaumKospiKosdaqListThreadNew(2, startTime);
            list2.start();
            Thread.sleep(2000);
            MakeDaumKospiKosdaqListThreadNew list3 = new MakeDaumKospiKosdaqListThreadNew(3, startTime);
            list3.start();
            Thread.sleep(2000);
            MakeDaumKospiKosdaqListThreadNew list4 = new MakeDaumKospiKosdaqListThreadNew(4, startTime);
            list4.start();
            Thread.sleep(2000);
            MakeDaumKospiKosdaqListThreadNew list5 = new MakeDaumKospiKosdaqListThreadNew(5, startTime);
            list5.start();
            Thread.sleep(2000);
            MakeDaumKospiKosdaqListThreadNew list6 = new MakeDaumKospiKosdaqListThreadNew(6, startTime);
            list6.start();
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        String elapsedTimeSecond = (endTime - startTime) / 1000 % 60 + "초";
        System.out.println("call time :" + elapsedTimeSecond);
        System.out.println("main method call finished.");
    }

    public static void main(String args[]) {
        new MakeDaumKospiKosdaqListCall();
    }
}
