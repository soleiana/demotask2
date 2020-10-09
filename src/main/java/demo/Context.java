package demo;

public interface Context {

    long getCompletedTaskCount();

    long getFailedTaskCount();

    long getInterruptedTaskCount();

    void interrupt();

    boolean isFinished();

    ExecutionStatistics getStatistics();
}
