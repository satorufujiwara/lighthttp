package jp.satorufujiwara.http;


import java.io.IOException;

public class RequestConvertTask<T> {

    T obj;
    ConverterProvider.RequestConverter<T> converter;

    RequestConvertTask(T obj, ConverterProvider.RequestConverter<T> converter) {
        this.obj = obj;
        this.converter = converter;
    }

    RequestBody execute() throws IOException {
        return converter.convert(obj);
    }

}
