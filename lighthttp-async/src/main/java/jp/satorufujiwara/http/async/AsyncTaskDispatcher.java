
package jp.satorufujiwara.http.async;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class AsyncTaskDispatcher {

    final BackgroundService backgroundService;
    final BackgroundThread backgroundThread;
    final BackgroundHandler handler;

    private static final int MESSAGE_TASK = 0;

    public void execute(final AsyncTask<?> task) {
        sendMessage(task);
    }

    void onExecuteTask(final Object obj, final int arg) {
        ((AsyncTask<?>) obj).execute();
    }

    public AsyncTaskDispatcher(final int threadCount) {
        backgroundService = new BackgroundService(threadCount);
        backgroundThread = new BackgroundThread();
        backgroundThread.start();
        handler = new BackgroundHandler(backgroundThread.getLooper(), this);
    }

    public void shutdown() {
        backgroundService.shutdown();
        backgroundThread.quit();
    }

    public void setThreadPoolSize(final int poolSize) {
        if (isShutdown()) {
            return;
        }
        backgroundService.setThreadPoolSize(poolSize);
    }

    protected void onHandleMessage(final int what, final int timeout, final int arg,
            final Object obj) {
        if (isShutdown()) {
            return;
        }
        final BackgroundTask task = new BackgroundTask() {
            @Override
            public void run() {
                onExecuteTask(obj, arg);
            }
        };
        task.mFuture = submit(task);
    }

    protected void sendMessage(final Object obj) {
        sendMessage(obj, 0);
    }

    protected void sendMessage(final Object obj, final int arg) {
        handler.sendMessage(handler.obtainMessage(MESSAGE_TASK, 0, arg, obj));
    }

    protected Future<?> submit(final Runnable task) {
        return backgroundService.submit(task);
    }

    protected boolean isShutdown() {
        return backgroundService.isShutdown();
    }

    static abstract class BackgroundTask implements Runnable {

        Future<?> mFuture;
    }

    static class BackgroundHandler extends Handler {

        private final AsyncTaskDispatcher mDispatcher;

        BackgroundHandler(final Looper looper, final AsyncTaskDispatcher dispatcher) {
            super(looper);
            mDispatcher = dispatcher;
        }

        @Override
        public void handleMessage(final Message msg) {
            mDispatcher.onHandleMessage(msg.what, msg.arg1, msg.arg2, msg.obj);
        }
    }

    static class BackgroundThread extends HandlerThread {

        BackgroundThread() {
            super("BackgroundThread", Process.THREAD_PRIORITY_BACKGROUND);
        }
    }

    static class BackgroundService extends ThreadPoolExecutor {

        BackgroundService(final int defaultPoolSize) {
            super(defaultPoolSize, defaultPoolSize, 0, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(), new BackgroundThreadFactory());
        }

        void setThreadPoolSize(final int poolSize) {
            setCorePoolSize(poolSize);
            setMaximumPoolSize(poolSize);
        }
    }

    private static class BackgroundThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(final Runnable r) {
            return new PriorityBackgroundThread(r);
        }

    }

    private static class PriorityBackgroundThread extends Thread {

        public PriorityBackgroundThread(final Runnable r) {
            super(r);
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            super.run();
        }
    }
}
