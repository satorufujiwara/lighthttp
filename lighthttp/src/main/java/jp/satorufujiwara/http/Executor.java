package jp.satorufujiwara.http;

public abstract class Executor<T> {

    private final ExecutorTask<T> task;

    protected Executor(ExecutorTask<T> task) {
        this.task = task;
    }

    protected ExecutorTask<T> getTask() {
        return task;
    }

    public interface Provider<T, E extends Executor<T>> {

        E newExecutor(ExecutorTask<T> task);

    }
}
