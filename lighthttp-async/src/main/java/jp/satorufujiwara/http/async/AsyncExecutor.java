package jp.satorufujiwara.http.async;


import jp.satorufujiwara.http.Executor;
import jp.satorufujiwara.http.ExecutorTask;

public class AsyncExecutor<T> extends Executor<T> {

    private static final AsyncTaskDispatcher DISPATCHER = new AsyncTaskDispatcher(
            Math.max(1, Runtime.getRuntime().availableProcessors()));

    AsyncExecutor(ExecutorTask<T> task) {
        super(task);
    }

    public static <R> Provider<R> provide() {
        return new Provider<>();
    }

    public void executeAsync(final AsyncCallback<T> callback) {
        DISPATCHER.execute(new AsyncTask<>(getTask(), callback));
    }

    public static class Provider<T> implements Executor.Provider<T, AsyncExecutor<T>> {

        Provider() {

        }

        @Override
        public AsyncExecutor<T> newExecutor(ExecutorTask<T> task) {
            return new AsyncExecutor<>(task);
        }
    }
}
