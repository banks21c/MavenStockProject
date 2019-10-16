package html.parsing.stock;

class RunnableThread implements Runnable {

    @Override

    public void run() {

        try {

            System.out.println("Runnable Thread:" + Thread.currentThread().getName());

            Thread.sleep(5000);

        } catch (InterruptedException e) {

            System.out.println(e.getMessage());

        }

    }

    public static void main(String[] args) {

        Thread t1 = new Thread(new RunnableThread());

        Thread t2 = new Thread(new RunnableThread());

        t1.start();

        t2.start();

        try {

            t2.join();

        } catch (InterruptedException e) {

            System.out.println(e.getMessage());

        }
    }
}
