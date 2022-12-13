public abstract class Task implements Runnable {
    public long time;
    public Task() {
        time = System.currentTimeMillis();
    }
    public  abstract void onEnd();
}