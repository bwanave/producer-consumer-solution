package com.company;

import java.util.LinkedList;
import java.util.Queue;

public class Main {

    public static void main(String[] args) {
        Queue<Integer> queue = new LinkedList<>();
        int maxQueueSize = 10;
        Thread producer = new Thread(new Producer(queue, maxQueueSize));
        Thread consumer = new Thread(new Consumer(queue, maxQueueSize));
        producer.start();
        consumer.start();
    }
}
