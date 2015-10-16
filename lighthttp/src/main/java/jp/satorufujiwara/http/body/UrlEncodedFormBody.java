package jp.satorufujiwara.http.body;

import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import jp.satorufujiwara.http.RequestBody;

public class UrlEncodedFormBody extends RequestBody {

    private static final String DEFAULT_CHARSET = "UTF-8";
    private final Map<String, String> params;
    private final String charSet;

    public static UrlEncodedFormBody create(final Map<String, String> params) {
        return new UrlEncodedFormBody(params, DEFAULT_CHARSET);
    }

    public static UrlEncodedFormBody create(final Map<String, String> params,
            final String charSet) {
        return new UrlEncodedFormBody(params, charSet);
    }

    private UrlEncodedFormBody(final Map<String, String> params, String charSet) {
        this.params = params;
        this.charSet = charSet;
    }

    @Override
    public String contentType() {
        return "application/x-www-form-urlencoded; charset=" + charSet;
    }

    @Override
    public void writeTo(BufferedOutputStream os) throws IOException {
        final PrintWriter writer = new PrintWriter(os);
        boolean isFirst = true;
        for (final Map.Entry<String, String> entry : params.entrySet()) {
            final String encodedName = encode(entry.getKey(), charSet);
            final String encodedValue = encode(entry.getValue(), charSet);
            if (!isFirst) {
                writer.append('&');
            }
            isFirst = false;
            writer.append(encodedName).append('=').append(encodedValue);
        }
        writer.flush();
    }

    private static String encode(final String content, final String encoding) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        try {
            return URLEncoder.encode(content, encoding != null ? encoding : DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
