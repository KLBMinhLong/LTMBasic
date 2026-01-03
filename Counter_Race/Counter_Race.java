public class Counter_Race {
    public static int counter = 0;
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(new Incrementer());
        Thread thread2 = new Thread(new Incrementer());
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("Final counter value: " + counter);
    }
    private static class Incrementer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                increment();
            }
        }
        private synchronized static void increment() {
            counter++;
        }
    }
}