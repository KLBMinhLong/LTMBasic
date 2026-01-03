public class TwoThreadsDemo {
    public static void main(String[] args) {
        Thread thread1 = new SimpleThread("Dalat");
        Thread thread2 = new SimpleThread("Sapa");

        thread1.start();
        thread2.start();
    }
}