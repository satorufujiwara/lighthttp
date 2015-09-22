package jp.satorufujiwara.http.gson;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import jp.satorufujiwara.http.ConverterProvider;
import jp.satorufujiwara.http.ResponseBody;

public class GsonConverterProvider extends ConverterProvider {

    private final Gson gson;

    public GsonConverterProvider() {
        this(new Gson());
    }

    public GsonConverterProvider(Gson gson) {
        this.gson = gson;
    }

    @Override
    protected <T> RequestConverter<T> requestConverter(Class<T> clz) {
        return null;
    }

    @Override
    protected <T> ResponseConverter<T> responseConverter(final Class<T> clz) {
        return new ResponseConverter<T>() {
            @Override
            public T convert(ResponseBody body) throws IOException {
                try {
                    return gson.fromJson(body.string(), clz);
                } catch (JsonSyntaxException e) {
                    throw new IOException(e);
                }
            }
        };
    }
}
