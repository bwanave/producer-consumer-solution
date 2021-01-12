package com.company;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Producer implements Runnable {

    private final Random random = new Random();
    private final Queue<Integer> queue;
    private final int maxQueueSize;

    public Producer(Queue<Integer> queue, int maxQueueSize) {
        this.queue = queue;
        this.maxQueueSize = maxQueueSize;
    }

    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while (true) {
            synchronized (queue) {
                while (queue.size() == maxQueueSize) {
                    log("Queue is full. Waiting until get notification from Consumer");
                    waitForItemToBeConsumed();
                }
                Integer item = random.nextInt();
                queue.add(item);
                log(String.format("Produced Item %s into queue", item));
                queue.notifyAll();
                sleep(2);
            }
        }
    }

    private void waitForItemToBeConsumed() {
        try {
            queue.wait();
        } catch (InterruptedException e) {
            log("Interrupted while waiting: " + e.toString());
        }
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void log(String message) {
        String log = String.format("PRODUCER: %s: %s", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")), message);
        System.out.println(log);
    }
}
