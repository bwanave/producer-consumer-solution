package com.company;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable {

    private final Queue<Integer> queue;
    private final int maxQueueSize;

    public Consumer(Queue<Integer> queue, int maxQueueSize) {
        this.queue = queue;
        this.maxQueueSize = maxQueueSize;
    }

    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while (true) {
            synchronized (queue) {
                while (queue.isEmpty()) {
                    log("Queue is empty. Waiting until get notification from Producer");
                    waitForItemToBeProduced();
                }
                Integer item = queue.remove();
                log(String.format("Consumed Item %s from queue", item));
                queue.notifyAll();
                sleep(2);
            }
        }
    }

    private void waitForItemToBeProduced() {
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
        String log = String.format("CONSUMER: %s: %s", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")), message);
        System.out.println(log);
    }
}
