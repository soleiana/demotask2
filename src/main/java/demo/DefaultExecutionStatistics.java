package demo;

import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;

public class DefaultExecutionStatistics implements ExecutionStatistics {

    private final List<ExecutedTask> executedTasks;

    public DefaultExecutionStatistics(List<ExecutedTask> executedTasks) {
        this.executedTasks = Collections.unmodifiableList(executedTasks);
    }

    @Override
    public long getMinExecutionTimeInMs() {
        return executedTasks.stream()
                .filter(ExecutedTask::isCompleted)
                .mapToLong(executedTask -> executedTask.getMeasuredTask().getExecutionTimeInMs())
                .min()
                .orElse(0L);
    }

    @Override
    public long getMaxExecutionTimeInMs() {
        return executedTasks.stream()
                .filter(ExecutedTask::isCompleted)
                .mapToLong(executedTask -> executedTask.getMeasuredTask().getExecutionTimeInMs())
                .max()
                .orElse(0L);
    }

    @Override
    public long getAverageExecutionTimeInMs() {
        OptionalDouble avg = executedTasks.stream()
                .filter(ExecutedTask::isCompleted)
                .mapToLong(executedTask -> executedTask.getMeasuredTask().getExecutionTimeInMs())
                .average();

        return avg.isPresent() ? Double.valueOf(avg.getAsDouble()).longValue() : 0;
    }
}
