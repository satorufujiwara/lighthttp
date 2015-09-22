package jp.satorufujiwara.http;

import java.io.IOException;

public class HttpException extends IOException {

    private final int code;
    private final ResponseBody body;

    public HttpException(final int code, final ResponseBody body) {
        this.code = code;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public ResponseBody getBody() {
        return body;
    }
}
