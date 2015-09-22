package jp.satorufujiwara.http.rx;

import java.io.IOException;

import jp.satorufujiwara.http.Executor;
import jp.satorufujiwara.http.ExecutorTask;
import jp.satorufujiwara.http.Response;
import rx.Observable;
import rx.Subscriber;

public class RxExecutor<T> extends Executor<T> {

    RxExecutor(ExecutorTask<T> task) {
        super(task);
    }

    public static <R> Provider<R> provide() {
        return new Provider<>();
    }

    public Observable<Response<T>> asObservable() {
        return Observable.create(new Observable.OnSubscribe<Response<T>>() {
            @Override
            public void call(Subscriber<? super Response<T>> subscriber) {
                try {
                    subscriber.onNext(getTask().execute());
                } catch (IOException e) {
                    subscriber.onError(e);
                    return;
                }
                subscriber.onCompleted();
            }
        });
    }

    static class Provider<T> implements Executor.Provider<T, RxExecutor<T>> {

        Provider() {

        }

        @Override
        public RxExecutor<T> newExecutor(ExecutorTask<T> task) {
            return new RxExecutor<>(task);
        }
    }
}
