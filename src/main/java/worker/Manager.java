package worker;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Manager{
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(180);
        QueueService queue = new QueueService();

        while (true) {
          executorService.execute(new Worker(queue.getDomains().take()));
            System.out.println("<<<<<<<<<<<thread started");
        }

    }
}