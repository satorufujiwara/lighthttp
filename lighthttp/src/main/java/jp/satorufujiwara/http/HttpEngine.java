package jp.satorufujiwara.http;

import android.annotation.SuppressLint;
import android.os.Build;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

class HttpEngine {

    private final HttpConfig config;
    private final Request request;
    private final ConnectionListener connectionListener;

    static {
        // Work around pre-Froyo bugs in HTTP connection reuse.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    HttpEngine(final HttpConfig config, final Request request, final ConnectionListener l) {
        this.config = config;
        this.request = request;
        this.connectionListener = l;
    }

    public HttpResponse execute() throws IOException {
        final URL httpUrl = new URL(request.getUrl());
        final HttpURLConnection urlConnection = (HttpURLConnection) httpUrl.openConnection();
        urlConnection.setRequestMethod(request.getMethod());
        urlConnection.setReadTimeout(config.readTimeout);
        urlConnection.setConnectTimeout(config.connectTimeout);
        final Map<String, String> headers = request.getHeaders();
        if (headers != null && !headers.isEmpty()) {
            addRequestProperties(urlConnection, headers);
        }
        if (isOutput(request)) {
            doOutput(urlConnection, request.getBody());
        }
        urlConnection.setDoInput(true);
        connectionListener.onPreConnect(request, urlConnection);
        urlConnection.connect();
        connectionListener.onPostConnect(request, urlConnection);
        final int statusCode = urlConnection.getResponseCode();
        final Map<String, List<String>> responseHeaders = urlConnection.getHeaderFields();
        final ResponseBody body = doInput(urlConnection);
        return new HttpResponse(request, statusCode, responseHeaders, body);
    }

    private static void addRequestProperties(final HttpURLConnection urlConnection,
            final Map<String, String> headers) {
        for (final Map.Entry<String, String> entry : headers.entrySet()) {
            final String value = entry.getValue();
            if (value != null) {
                urlConnection.addRequestProperty(entry.getKey(), value);
            }
        }
    }

    private static boolean isOutput(final Request request) {
        if (request.getBody() == null) {
            return false;
        }
        final String method = request.getMethod();
        return "POST".equals(method) || "PUT".equals(method);
    }

    private static void doOutput(final HttpURLConnection urlConnection, final RequestBody body)
            throws IOException {
        urlConnection.setDoOutput(true);
        final long contentLength = body.contentLength();
        if (contentLength > 0) {
            setFixedLengthStreamingMode(urlConnection, contentLength);
        } else {
            urlConnection.setChunkedStreamingMode(0);
        }
        BufferedOutputStream os = null;
        try {
            os = new BufferedOutputStream(urlConnection.getOutputStream());
            body.writeTo(os);
        } finally {
            Utils.closeQuietly(os);
        }
    }

    private static ResponseBody doInput(final HttpURLConnection urlConnection)
            throws IOException {
        final String contentType = urlConnection.getContentType();
        final long contentLength = urlConnection.getContentLength();
        return new ResponseBody(contentType, contentLength,
                new BufferedInputStream(getResponseStream(urlConnection)), urlConnection);
    }

    private static InputStream getResponseStream(final HttpURLConnection urlConnection) {
        try {
            return new BufferedInputStream(urlConnection.getInputStream());
        } catch (final IOException e) {
            return urlConnection.getErrorStream();
        }
    }

    @SuppressLint("NewApi")
    private static void setFixedLengthStreamingMode(final HttpURLConnection urlConnection,
            final long contentLength) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            urlConnection.setFixedLengthStreamingMode((int) contentLength);
        } else {
            urlConnection.setFixedLengthStreamingMode(contentLength);
        }
    }

}
