import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool {
    private final ArrayBlockingQueue<Task> taskQueue;
    private final List<MyThread> myThread = new ArrayList<>();
    public static final AtomicInteger numberOfTasks = new AtomicInteger();

    public ThreadPool(int numberOfTasks, int numberOfThreads) {
        taskQueue = new ArrayBlockingQueue<>(numberOfTasks);
        ThreadPool.numberOfTasks.set(numberOfTasks);
        // Добавление заданий в очередь
        for (int i = 0; i < numberOfThreads; ++i)
            myThread.add(new MyThread(taskQueue));
        // Выполнение доступных заданий
        for (MyThread myThread : myThread) {
            new Thread(myThread).start();
        }
    }

    // Task добавляется в очередь
    public synchronized void execute(Task task) {
        taskQueue.offer(task);
    }

    // Остановка всех потоков
    public synchronized void shutdown() {
        for (MyThread myThread : myThread) {
            myThread.interrupt();
        }
    }

    // Усыпляет поток Client пока не выполнятся все задания в очереди
    public synchronized void join() {
        while (!(numberOfTasks.get() == 0 && taskQueue.isEmpty())) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}