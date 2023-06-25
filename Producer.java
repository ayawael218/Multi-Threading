import java.util.Queue;

public class Producer extends Thread {
    int bufferSize, N;
    static int count;
    Queue<Integer> buffer;
    Producer(int N, int bufferSize, Queue<Integer> buffer) {
        this.buffer = buffer;
        this.bufferSize = bufferSize;
        this.N = N;
        count = 2;
    }
    public static int getCount() {
        return count;
    }

    boolean isPrime(int x) {
        for (int i = 2; i <= Math.sqrt(x); i++)
            if (x % i == 0) return false;
        return x > 1;
    }

    @Override
    public void run() {
        while (count < N) {
            synchronized (buffer) {
                while (buffer.size() == bufferSize) {
                    try {
                        buffer.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                while (!isPrime(count) && count < N) {
                    count++;
                }
                if(isPrime(count)){
                    buffer.add(count);
                    System.out.println("Produced " + count);
                }
                count++;
                if(count >= N)
                    buffer.add(-1);
                buffer.notify();
                try {
                    sleep(0);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("Produces Successfully");
    }
}