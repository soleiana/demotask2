package demo;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class TaskFactory {

    static final int NUMBER_OF_TASKS = 20;
    static final int MAX_TIME_TO_RUN_TASK = 500;

    private static final int MIN_TIME_TO_RUN_TASK = 100;

    public Runnable[] createTasksWithRandomTime() {
        IntFunction<Runnable> mapper = this::createRunnableWithRandomTime;
        return IntStream.rangeClosed(1, NUMBER_OF_TASKS)
                .mapToObj(mapper)
                .toArray(Runnable[]::new);
    }

    public Runnable[] createFailingTasksWithRandomTime() {
        Runnable[] tasks = createTasksWithRandomTime();
        tasks[0] = () -> {
            throw new RuntimeException("Exception");
        };
        tasks[1] = () -> {
            throw new RuntimeException("Exception");
        };
        return tasks;
    }

    private Runnable createRunnableWithRandomTime(Integer number) {
        return () -> {
            System.out.println("Task " + number + " running...");
            try {
                Thread.sleep(randomTaskExecutionTime());
            } catch (InterruptedException ignored) {
            }
            System.out.println("Task " + number + " finished");
        };
    }

    private int randomTaskExecutionTime() {
        return ThreadLocalRandom.current().nextInt(MIN_TIME_TO_RUN_TASK, MAX_TIME_TO_RUN_TASK + 1);
    }
}
