package jp.satorufujiwara.http;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private String url;
    private String method;
    private Map<String, String> headers;
    private RequestBody body;

    Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers;
        this.body = builder.body;
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

    public static class Builder {

        private String url;
        private String method;
        private Map<String, String> headers;
        private RequestBody body;

        public Builder() {
            method = "GET";
            headers = new HashMap<>();
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder method(String method, RequestBody body) {
            this.method = method;
            this.body = body;
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }
}
