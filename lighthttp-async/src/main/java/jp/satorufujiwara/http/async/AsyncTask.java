package jp.satorufujiwara.http.async;


import java.io.IOException;

import jp.satorufujiwara.http.ExecutorTask;
import jp.satorufujiwara.http.Response;

class AsyncTask<T> {

    private final ExecutorTask<T> task;
    private final AsyncCallback<T> callback;

    AsyncTask(ExecutorTask<T> task, AsyncCallback<T> callback) {
        this.task = task;
        this.callback = callback;
    }

    public void execute() {
        Exception re = null;
        Response<T> r = null;
        try {
            r = task.execute();
        } catch (IOException e) {
            re = e;
        }
        callback.onResult(r, re);
    }

}
