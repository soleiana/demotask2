package demo;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DefaultExecutionManager implements ExecutionManager {

    private static final int THREAD_POOL_SIZE = 10;

    @Override
    public Context execute(Runnable callback, Runnable... tasks) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        List<MeasuredTask> measuredTasks = Arrays.stream(tasks)
                .map(MeasuredTask::new)
                .collect(Collectors.toList());

        List<CompletableFuture<Void>> results = measuredTasks.stream()
                .map(task -> CompletableFuture.runAsync(task, executor))
                .collect(Collectors.toList());

        CompletableFuture.allOf(results.toArray(new CompletableFuture[0]))
                .whenCompleteAsync((res, throwable) -> callback.run());

        return new DefaultContext(executedTasks(measuredTasks, results));
    }

    private List<ExecutedTask> executedTasks(List<MeasuredTask> measuredTasks,
                                             List<CompletableFuture<Void>> results) {
        return IntStream.range(0, measuredTasks.size())
                .mapToObj(index -> new ExecutedTask(measuredTasks.get(index), results.get(index)))
                .collect(Collectors.toList());
    }
}
