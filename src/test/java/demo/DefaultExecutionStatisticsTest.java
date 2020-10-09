package demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static demo.TaskFactory.MAX_TIME_TO_RUN_TASK;

class DefaultExecutionStatisticsTest {

    private DefaultExecutionManager executionManager;
    private TaskFactory taskFactory;

    @BeforeEach
    void setUp() {
        executionManager = new DefaultExecutionManager();
        taskFactory = new TaskFactory();
    }

    @Test
    void shouldGetMinRandomExecutionTimeInMs() throws Exception {
        Runnable[] tasks = taskFactory.createTasksWithRandomTime();
        Context context = executionManager.execute(() -> System.out.println("Callback running..."), tasks);
        Thread.sleep(MAX_TIME_TO_RUN_TASK);
        long result = context.getStatistics().getMinExecutionTimeInMs();
        Assertions.assertTrue(result > 0);
    }

    @Test
    void shouldGetMaxRandomExecutionTimeInMs() throws Exception {
        Runnable[] tasks = taskFactory.createTasksWithRandomTime();
        Context context = executionManager.execute(() -> System.out.println("Callback running..."), tasks);
        Thread.sleep(MAX_TIME_TO_RUN_TASK);
        long result = context.getStatistics().getMaxExecutionTimeInMs();
        Assertions.assertTrue(result > 0);
    }

    @Test
    void shouldGetAverageRandomExecutionTimeInMs() throws Exception {
        Runnable[] tasks = taskFactory.createTasksWithRandomTime();
        Context context = executionManager.execute(() -> System.out.println("Callback running..."), tasks);
        Thread.sleep(MAX_TIME_TO_RUN_TASK * 10);
        long result = context.getStatistics().getAverageExecutionTimeInMs();
        long min = context.getStatistics().getMinExecutionTimeInMs();
        long max = context.getStatistics().getMaxExecutionTimeInMs();
        Assertions.assertTrue(result > 0);
        Assertions.assertTrue(result > min);
        Assertions.assertTrue(result < max);
    }
}
