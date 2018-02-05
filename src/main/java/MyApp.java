import java.util.Stack;

public class MyApp {
    public static void main(String[] args) {

        Bucket bucket = new Bucket();

        Thread writerThread = new Thread(() -> {
            while(true) {
                synchronized (bucket) {
                    if (bucket.isFull()) {
                        System.out.println("Bucket is full. Waiting ...");

                        try {
                            bucket.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        int a = (int)(Math.random() * 10);
                        System.out.println("Putting element into the bucket " + a);

                        bucket.addElement(a);
                    }
                    bucket.notifyAll();

                }
            }

        });

        Thread readerThread = new Thread(() -> {
            while (true) {
                synchronized (bucket) {
                    if (bucket.isEmpty()) {
                        System.out.println("Bucket is empty. Waiting ...");
                        try {
                            bucket.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        int elem = bucket.readElement();
                        System.out.println("Reading an element from the bucket " + elem);
                    }

                    bucket.notifyAll();

                }
            }

        });

        Thread whatsInTheBucket = new Thread(() -> {
            while (true) {
                synchronized (bucket) {
                    if (bucket.isEmpty()) {
                        System.out.println("Nothing is in the bucket. Waiting ...");
                        try {
                            bucket.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        int size = bucket.whatsInTheBucket();
                        System.out.println("There are  " + size + " elements in the bucket");
                    }

                    bucket.notifyAll();

                }
            }

        });

        readerThread.start();
        writerThread.start();
        whatsInTheBucket.start();


    }

}

class Bucket {
    private static Stack<Integer> stack = new Stack<>();

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public void addElement(int a) {
        stack.push(a);
    }

    public int readElement() {
        return stack.pop();
    }

    public int whatsInTheBucket() {
        return stack.size();
    }

    public boolean isFull() {
        return stack.size() >= 10;
    }
}
