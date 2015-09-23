package jp.satorufujiwara.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;

public class ResponseBody implements Closeable {

    private final String contentType;
    private final long contentLength;
    private final BufferedInputStream inputStream;

    private ResponseBody(final String contentType, final long contentLength,
            final BufferedInputStream inputStream) {
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.inputStream = inputStream;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    public String contentType() {
        return contentType;
    }

    public BufferedInputStream inputStream() {
        return inputStream;
    }

    public long contentLength() {
        return contentLength;
    }

    public byte[] bytes() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BufferedInputStream is = inputStream();
        try {
            byte[] buffer = new byte[1024];
            int n = 0;
            while (-1 != (n = is.read(buffer))) {
                os.write(buffer, 0, n);
            }
            return os.toByteArray();
        } finally {
            Utils.closeQuietly(os);
            Utils.closeQuietly(is);
        }
    }

    public final String string() throws IOException {
        return new String(bytes(), "UTF-8");
    }

    public static ResponseBody create(final String contentType, final long contentLength,
            final BufferedInputStream inputStream) {
        if (inputStream == null) {
            throw new NullPointerException("inputStream == null");
        }
        return new ResponseBody(contentType, contentLength, inputStream);
    }

}
