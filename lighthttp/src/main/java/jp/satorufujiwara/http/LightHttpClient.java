package jp.satorufujiwara.http;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

public class LightHttpClient {

    private final HttpConfig httpConfig = new HttpConfig();
    private ConverterProvider converterProvider = DEFAULT_CONVERTER;
    private ConnectionListener connectionListener = emptyListener();

    public LightHttpClient() {

    }

    public void setConnectionListener(final ConnectionListener listener) {
        connectionListener = listener;
    }

    public void setConnectTimeout(int timeoutMillis) {
        httpConfig.connectTimeout = timeoutMillis;
    }

    public void setReadTimeout(int timeoutMillis) {
        httpConfig.readTimeout = timeoutMillis;
    }

    public void setConverterProvider(ConverterProvider provider) {
        converterProvider = provider;
    }

    public <T> Call<T> newCall(final Request request,
            ConverterProvider.ResponseConverter<T> converter) {
        return new Call<>(new ExecutorTask<>(request,
                new HttpEngine(httpConfig, connectionListener), converter));
    }

    public <T> Call<T> newCall(final Request request, Type type) {
        return newCall(request, converterProvider.<T>responseConverter(type));
    }

    public Call<String> newCall(final Request request) {
        return newCall(request, DEFAULT_CONVERTER.<String>responseConverter(String.class));
    }

    public Request.Builder newRequest() {
        return new Request.Builder(converterProvider);
    }

    private static ConnectionListener emptyListener() {
        return new ConnectionListener() {
        };
    }

    private static final ConverterProvider DEFAULT_CONVERTER =
            new ConverterProvider() {
                @Override
                protected <T> RequestConverter<T> requestConverter(Type type) {
                    return new RequestConverter<T>() {
                        @Override
                        public RequestBody convert(final T origin) throws IOException {
                            return new RequestBody() {
                                @Override
                                public String contentType() {
                                    return null;
                                }

                                @Override
                                public void writeTo(BufferedOutputStream os) throws IOException {
                                    if (origin == null) {
                                        return;
                                    }
                                    os.write(origin.toString().getBytes(Charset.forName("UTF-8")));
                                }
                            };
                        }
                    };
                }

                @Override
                protected <T> ResponseConverter<T> responseConverter(Type type) {
                    return new ResponseConverter<T>() {
                        @Override
                        public T convert(ResponseBody body) throws IOException {
                            try {
                                return (T) body.string();
                            } catch (ClassCastException e) {
                                throw new IOException(e);
                            }
                        }
                    };
                }
            };

}
