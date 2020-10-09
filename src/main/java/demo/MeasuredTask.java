package demo;

public class MeasuredTask implements Runnable {

    private final Runnable delegate;
    private TaskState taskState = TaskState.NEW;
    private long executionTimeInMs;

    public MeasuredTask(Runnable delegate) {
        this.delegate = delegate;
    }

    public void run() {
        taskState = TaskState.STARTED;
        long startTime = System.currentTimeMillis();
        delegate.run();
        long stopTime = System.currentTimeMillis();
        executionTimeInMs = stopTime - startTime;
    }

    public long getExecutionTimeInMs() {
        return executionTimeInMs;
    }

    public boolean hasStarted() {
        return taskState != TaskState.NEW;
    }
}
