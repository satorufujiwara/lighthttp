package jp.satorufujiwara.http;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private String url;
    private String method;
    private Map<String, String> headers;
    private RequestBody body;
    private RequestConvertTask<?> pendingTask;

    Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers;
        this.body = builder.body;
        this.pendingTask = builder.task;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getMethod() {
        return method;
    }

    public RequestBody getBody() {
        return body;
    }

    void setBody(RequestBody body) {
        this.body = body;
    }

    RequestConvertTask<?> getPendingTask() {
        return pendingTask;
    }

    public static class Builder {

        private final ConverterProvider converterProvider;
        private String url;
        private String method;
        private Map<String, String> headers;
        private RequestBody body;
        private RequestConvertTask<?> task;

        Builder(final ConverterProvider converterProvider) {
            this.converterProvider = converterProvider;
            method = "GET";
            headers = new HashMap<>();
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder get() {
            return method("GET", null);
        }

        public Builder post(RequestBody body) {
            return method("POST", body);
        }

        public <T> Builder post(T body, Type type) {
            return task("POST", body, type);
        }

        public Builder delete(RequestBody body) {
            return method("DELETE", body);
        }

        public <T> Builder delete(T body, Type type) {
            return task("DELETE", body, type);
        }

        public Builder put(RequestBody body) {
            return method("PUT", body);
        }

        public <T> Builder put(T body, Type type) {
            return task("PUT", body, type);
        }

        public Builder patch(RequestBody body) {
            return method("PATCH", body);
        }

        public <T> Builder patch(T body, Type type) {
            return task("PATCH", body, type);
        }

        public Builder addHeader(String name, String value) {
            headers.put(name, value);
            return this;
        }

        public Builder method(String method, RequestBody body) {
            this.method = method;
            this.body = body;
            this.task = null;
            return this;
        }

        private <T> Builder task(String method, T body, Type type) {
            this.method = method;
            this.body = null;
            this.task = new RequestConvertTask<>(body, converterProvider.requestConverter(type));
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }
}
