package jp.satorufujiwara.http;

import java.io.IOException;

public abstract class ConverterProvider {

    protected abstract <T> RequestConverter<T> requestConverter(Class<T> clz);

    protected abstract <T> ResponseConverter<T> responseConverter(Class<T> clz);

    public interface RequestConverter<T> {

        RequestBody convert(T origin) throws IOException;

    }

    public interface ResponseConverter<T> {

        T convert(ResponseBody body) throws IOException;

    }
}
