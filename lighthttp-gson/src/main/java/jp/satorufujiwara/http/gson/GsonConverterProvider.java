package jp.satorufujiwara.http.gson;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

import jp.satorufujiwara.http.ConverterProvider;
import jp.satorufujiwara.http.RequestBody;
import jp.satorufujiwara.http.ResponseBody;
import jp.satorufujiwara.http.Utils;

public class GsonConverterProvider extends ConverterProvider {

    private final Gson gson;

    public GsonConverterProvider() {
        this(new Gson());
    }

    public GsonConverterProvider(Gson gson) {
        this.gson = gson;
    }

    @Override
    protected <T> RequestConverter<T> requestConverter(final Type type) {
        return new RequestConverter<T>() {
            @Override
            public RequestBody convert(final T origin) throws IOException {
                return new RequestBody() {
                    @Override
                    public String contentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public void writeTo(BufferedOutputStream os) throws IOException {
                        final JsonWriter writer = new JsonWriter(
                                new OutputStreamWriter(os, "UTF-8"));
                        try {
                            gson.toJson(origin, type, writer);
                        } catch (JsonIOException e) {
                            throw new IOException(e);
                        } finally {
                            Utils.closeQuietly(writer);
                        }
                    }
                };
            }
        };
    }

    @Override
    protected <T> ResponseConverter<T> responseConverter(final Type type) {
        return new ResponseConverter<T>() {
            @Override
            public T convert(ResponseBody body) throws IOException {
                try {
                    return gson.fromJson(body.string(), type);
                } catch (JsonSyntaxException e) {
                    throw new IOException(e);
                }
            }
        };
    }
}
