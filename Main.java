import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

public class Main extends JDialog {
    private JPanel contentPane;
    private JTextField textN;
    public static long startTime;
    public static int N; // hygy mn el GUI
    public static int bufferSize; // GUI
    public static String fileName; // GUI
    private JTextField textFile;
    private JTextField textBuffer;
    private JButton startProducerButton;
    private JLabel maxNum;
    private JLabel NOP;
    private JLabel timeElapsed;

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            N = Integer.parseInt(textN.getText());
            bufferSize = Integer.parseInt(textBuffer.getText());
            fileName = textFile.getText();
            Queue<Integer> buffer = new ArrayDeque<>();
            Producer producer = new Producer(N, bufferSize, buffer);
            Consumer consumer;
            try {
                consumer = new Consumer(N, fileName, buffer, maxNum, NOP, timeElapsed,startTime);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            startTime = System.currentTimeMillis();
            producer.start();
            consumer.start();
//            try {
//                producer.join();
//            } catch (InterruptedException ex) {
//                throw new RuntimeException(ex);
//            }
//            try {
//                consumer.join();
//            } catch (InterruptedException ex) {
//                throw new RuntimeException(ex);
//            }
        }
    };

    public Main() {
        setContentPane(contentPane);
        setModal(true);
        startProducerButton.addActionListener(actionListener);
    }

    public static void main(String[] args) {
        Main dialog = new Main();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
