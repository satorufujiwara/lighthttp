package jp.satorufujiwara.http;


import java.io.IOException;

public class ExecutorTask<T> {

    private final HttpEngine engine;
    private final ConverterProvider.ResponseConverter<T> converter;

    ExecutorTask(final HttpEngine engine, final ConverterProvider.ResponseConverter<T> converter) {
        this.engine = engine;
        this.converter = converter;
    }

    public Response<T> execute() throws IOException {
        final HttpResponse httpResponse = engine.execute();
        if (!httpResponse.isSuccess()) {
            throw new HttpException(httpResponse.getCode(), httpResponse.getBody());
        }
        final T obj = converter.convert(httpResponse.getBody());
        return new Response<>(httpResponse, obj);
    }

}
