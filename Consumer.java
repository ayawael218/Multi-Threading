import javax.swing.*;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;


public class Consumer extends Thread {
    int N, count, max;
    private JLabel maxNum;
    private JLabel NOP;
    private JLabel timeElapsed;
    long startTime, endTime;
    String file;
    Queue<Integer> buffer;
    BufferedWriter writer;

    Consumer(int N, String file, Queue<Integer> buffer, JLabel maxNum, JLabel NOP, JLabel timeElapsed, Long startTime) throws IOException {
        this.N = N;
        this.file = file;
        this.buffer = buffer;
        writer = new BufferedWriter(new FileWriter(file));
        this.NOP = NOP;
        this.maxNum = maxNum;
        this.timeElapsed = timeElapsed;
        this.startTime = startTime;
    }

    @Override
    public void run() {
        long endTime;
        int num = 1, mx = 0;
        boolean finished = false;
        while (true) {
            synchronized (buffer) {
                while (buffer.isEmpty()) {
                    try {
                        buffer.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                mx = buffer.poll(); // largest prime
                if (mx == -1)
                    break;
                num++; // number of primes generated
                endTime = System.currentTimeMillis(); // Main.startTime - endTime --> time elapsed
                System.out.println("Consumed " + mx);
                maxNum.setText(Integer.toString(mx));
                NOP.setText(Integer.toString(num));
                timeElapsed.setText(Long.toString((endTime - Main.startTime)) + " ms");
                try {
                    writer.write("\"" + mx + "\", ");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                buffer.notify();
                try {
                    sleep(0);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("Consumed Successfully");
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}