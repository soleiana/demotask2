package demo;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultContext implements Context {

    private final List<ExecutedTask> executedTasks;
    private final AtomicLong interruptedCounter = new AtomicLong();

    public DefaultContext(List<ExecutedTask> executedTasks) {
        this.executedTasks = Collections.unmodifiableList(executedTasks);
    }

    @Override
    public long getCompletedTaskCount() {
        long finishedTasks = executedTasks.stream()
                .map(ExecutedTask::getResult)
                .filter(CompletableFuture::isDone)
                .count();
        return finishedTasks - getInterruptedTaskCount() - getFailedTaskCount();
    }

    @Override
    public long getFailedTaskCount() {
        long completedExceptionally = executedTasks.stream()
                .map(ExecutedTask::getResult)
                .filter(CompletableFuture::isCompletedExceptionally)
                .count();
        return completedExceptionally - getInterruptedTaskCount();
    }

    @Override
    public long getInterruptedTaskCount() {
        return interruptedCounter.get();
    }

    @Override
    public void interrupt() {
        executedTasks.stream()
                .filter(executedTask -> !executedTask.getMeasuredTask().hasStarted())
                .map(ExecutedTask::getResult)
                .forEach(result -> {
                    result.cancel(false);
                    System.out.println("Cancelling task");
                    interruptedCounter.getAndIncrement();
                });
    }

    @Override
    public boolean isFinished() {
        return executedTasks.size() == getInterruptedTaskCount() + getCompletedTaskCount();
    }

    @Override
    public ExecutionStatistics getStatistics() {
        return new DefaultExecutionStatistics(executedTasks);
    }
}
