package jp.satorufujiwara.http;

import java.io.IOException;
import java.net.CookieHandler;

public class LightHttpClient {

    private final HttpConfig httpConfig = new HttpConfig();
    private ConverterProvider converterProvider = DEFAULT_CONVERTER;

    public LightHttpClient() {
        CookieHandler.setDefault(new WebkitCookieManagerProxy());
    }

    public void setConnectTimeout(int timeoutMillis) {
        this.httpConfig.connectTimeout = timeoutMillis;
    }

    public void setReadTimeout(int timeoutMillis) {
        this.httpConfig.readTimeout = timeoutMillis;
    }

    public void setConverterProvider(ConverterProvider provider) {
        this.converterProvider = provider;
    }

    public <T> Call<T> newCall(final Request request, Class<T> clz) {
        return new Call<>(new ExecutorTask<>(new HttpEngine(httpConfig, request),
                converterProvider.responseConverter(clz)));
    }

    public Call<String> newCall(final Request request) {
        return new Call<>(new ExecutorTask<>(new HttpEngine(httpConfig, request),
                DEFAULT_CONVERTER.responseConverter(String.class)));
    }

    private static final ConverterProvider DEFAULT_CONVERTER =
            new ConverterProvider() {
                @Override
                protected <T> RequestConverter<T> requestConverter(Class<T> clz) {
                    return new RequestConverter<T>() {
                        @Override
                        public RequestBody convert(T origin) throws IOException {
                            return null;
                        }
                    };
                }

                @Override
                protected <T> ResponseConverter<T> responseConverter(Class<T> clz) {
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
