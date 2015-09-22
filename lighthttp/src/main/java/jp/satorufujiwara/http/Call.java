package jp.satorufujiwara.http;

import java.io.IOException;

public class Call<T> {

    private final ExecutorTask<T> task;

    Call(final ExecutorTask<T> task) {
        this.task = task;
    }

    public <E extends Executor<T>> E executeOn(final Executor.Provider<T, E> provider) {
        return provider.newExecutor(task);
    }

    public Response<T> execute() throws IOException {
        return task.execute();
    }

}
