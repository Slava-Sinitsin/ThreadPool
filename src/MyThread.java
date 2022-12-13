import java.util.concurrent.ArrayBlockingQueue;

public class MyThread implements Runnable {
    private Thread myThread = null;
    private boolean isInterrupt = false;
    public ArrayBlockingQueue<Task> taskQueue;

    public MyThread(ArrayBlockingQueue<Task> queue) {
        taskQueue = queue;
    }

    // Выполнение и взятие из очереди задания
    @Override
    public void run() {
        myThread = Thread.currentThread();
        while (!isInterrupt) {
            try {
                Task runnable = taskQueue.take();
                runnable.run();
                runnable.onEnd();
                synchronized (ThreadPool.numberOfTasks) {
                    ThreadPool.numberOfTasks.getAndDecrement();
                }
            } catch (Exception ignored) {
            }
        }
    }

    // Остановка потока
    public synchronized void interrupt() {
        isInterrupt = true;
        myThread.interrupt();
    }
}