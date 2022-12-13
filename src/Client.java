import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {
    public static final AtomicLong executionTime = new AtomicLong(0);
    public static final AtomicInteger residue = new AtomicInteger(0);

    // Тестирование обычных потоков
    public void simpleThread(int numberOfTasks) {
        long begin = System.currentTimeMillis();
        residue.set(numberOfTasks);
        for (int i = 0; i < numberOfTasks; ++i) {
            execute(new Task() {
                @Override
                public void onEnd() {
                    executionTime.addAndGet(System.currentTimeMillis() - begin);
                }

                @Override
                public void run() {
                    double num = 0.1f;
                    for (int i = 0; i < 10000000; ++i) {
                        num += num;
                    }
                    Client.residue.getAndDecrement();
                }
            });
        }
        while (residue.get() != 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        long end = System.currentTimeMillis() - begin;
        /*System.out.println("SimpleThreads\n" + "Number of tasks: " + numberOfTasks + "\nExecution time: " + end + "ms\n" +
                "Delay: " + executionTime.get() / numberOfTasks + "ms");*/
        System.out.println(end + " " + (executionTime.get() / numberOfTasks));
    }

    public void threadsPool(int numberOfTasks, int numberOfThreads) {
        long begin = System.currentTimeMillis();
        // Создание пула потоков
        ThreadPool threadPool = new ThreadPool(numberOfTasks, numberOfThreads);
        for (int i = 0; i < numberOfTasks; ++i)
            threadPool.execute(new Task() {
                @Override
                public void onEnd() {
                    executionTime.addAndGet(System.currentTimeMillis() - begin);
                }

                @Override
                public void run() {
                    double weight = 0;
                    for (int i = 0; i < 1000000; ++i) {
                        weight += Math.sin(weight);
                    }
                    synchronized (residue) {
                        residue.getAndDecrement();
                    }
                }
            });
        threadPool.join();
        threadPool.shutdown();
        long end = System.currentTimeMillis() - begin;
        /*System.out.println("ThreadPool\n" + "Number of threads: " + numberOfThreads + "\nNumber of tasks: " + numberOfTasks + "\nExecution time: " + end + "ms\n" +
                "Delay: " + executionTime.get() / numberOfTasks + "ms");*/
        System.out.println(end + " " + (executionTime.get() / numberOfTasks));
    }

    public static void execute(Task task) {
        new Thread((() -> {
            task.run();
            task.onEnd();
        })).start();
    }
}