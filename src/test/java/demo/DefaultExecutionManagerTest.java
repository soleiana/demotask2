package demo;

import org.junit.jupiter.api.Assertions;

import static demo.TaskFactory.MAX_TIME_TO_RUN_TASK;
import static demo.TaskFactory.NUMBER_OF_TASKS;

class DefaultExecutionManagerTest {

    private TaskFactory taskFactory;
    private DefaultExecutionManager target;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        taskFactory = new TaskFactory();
        target = new DefaultExecutionManager();
    }

    @org.junit.jupiter.api.Test
    void shouldInterruptTasks() throws Exception {
        Runnable[] tasks = taskFactory.createTasksWithRandomTime();
        Context context = target.execute(() -> System.out.println("Callback running..."), tasks);
        Thread.sleep(100);
        Assertions.assertEquals(0, context.getInterruptedTaskCount());
        context.interrupt();
        Assertions.assertTrue(context.getInterruptedTaskCount() > 0);
    }

    @org.junit.jupiter.api.Test
    void shouldFailTasks() throws Exception {
        Runnable[] tasks = taskFactory.createFailingTasksWithRandomTime();
        Context context = target.execute(() -> System.out.println("Callback running..."), tasks);
        Thread.sleep(MAX_TIME_TO_RUN_TASK * 10);
        Assertions.assertEquals(2, context.getFailedTaskCount());
    }

    @org.junit.jupiter.api.Test
    void shouldGetCompletedTasks() throws Exception {
        Runnable[] tasks = taskFactory.createFailingTasksWithRandomTime();
        Context context = target.execute(() -> System.out.println("Callback running..."), tasks);
        Thread.sleep(100);
        context.interrupt();

        Thread.sleep(MAX_TIME_TO_RUN_TASK * 10);
        Assertions.assertEquals(NUMBER_OF_TASKS - context.getFailedTaskCount() - context.getInterruptedTaskCount(),
                context.getCompletedTaskCount());
    }

    @org.junit.jupiter.api.Test
    void shouldBeFinished() throws Exception {
        Runnable[] tasks = taskFactory.createTasksWithRandomTime();
        Context context = target.execute(() -> System.out.println("Callback running..."), tasks);
        Thread.sleep(MAX_TIME_TO_RUN_TASK);
        context.interrupt();
        Thread.sleep(MAX_TIME_TO_RUN_TASK * 10);
        Assertions.assertTrue(context.isFinished());
    }

    @org.junit.jupiter.api.Test
    void shouldNotBeFinished() throws Exception {
        Runnable[] tasks = taskFactory.createFailingTasksWithRandomTime();
        Context context = target.execute(() -> System.out.println("Callback running..."), tasks);
        Thread.sleep(MAX_TIME_TO_RUN_TASK);
        context.interrupt();
        Thread.sleep(MAX_TIME_TO_RUN_TASK * 10);
        Assertions.assertFalse(context.isFinished());
    }
}
