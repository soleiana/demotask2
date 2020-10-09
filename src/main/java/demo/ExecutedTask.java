package demo;

import java.util.concurrent.CompletableFuture;

public class ExecutedTask {

    private final MeasuredTask measuredTask;
    private final CompletableFuture<Void> result;

    public ExecutedTask(MeasuredTask measuredTask, CompletableFuture<Void> result) {
        this.measuredTask = measuredTask;
        this.result = result;
    }

    public MeasuredTask getMeasuredTask() {
        return measuredTask;
    }

    public CompletableFuture<Void> getResult() {
        return result;
    }

    public boolean isCompleted() {
        return result.isDone() && !result.isCompletedExceptionally();
    }
}
