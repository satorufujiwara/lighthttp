package jp.satorufujiwara.http;


import java.io.IOException;

public class ExecutorTask<T> {

    private final Request request;
    private final HttpEngine engine;
    private final ConverterProvider.ResponseConverter<T> converter;

    ExecutorTask(final Request request, final HttpEngine engine,
            final ConverterProvider.ResponseConverter<T> converter) {
        this.request = request;
        this.engine = engine;
        this.converter = converter;
    }

    public Response<T> execute() throws IOException {
        try {
            final RequestConvertTask<?> task = request.getPendingTask();
            if (task != null) {
                request.setBody(task.execute());
            }
            final HttpResponse httpResponse = engine.execute(request);
            if (!httpResponse.isSuccess()) {
                throw new HttpException(httpResponse.getCode(), httpResponse.getBody());
            }
            final T obj = converter.convert(httpResponse.getBody());
            return new Response<>(httpResponse, obj);
        } catch (RuntimeException e) {
            throw new IOException(e);
        }
    }

}
