package jp.satorufujiwara.http.utils;

import android.webkit.CookieManager;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http://stackoverflow.com/questions/18057624/two-way-sync-for-cookies-between-
 * httpurlconnection-java-net-cookiemanager-and
 */
public class WebkitCookieManagerProxy extends CookieHandler {

    private CookieManager webkitCookieManager;

    public WebkitCookieManagerProxy() {
        webkitCookieManager = CookieManager.getInstance();
    }

    @Override
    public void put(final URI uri, final Map<String, List<String>> responseHeaders)
            throws IOException {
        if (uri == null || responseHeaders == null) {
            return;
        }
        final String url = uri.toString();
        for (final String headerKey : responseHeaders.keySet()) {
            if ((headerKey == null) || !(headerKey.equalsIgnoreCase("Set-Cookie2")
                    || headerKey.equalsIgnoreCase("Set-Cookie"))) {
                continue;
            }
            for (final String headerValue : responseHeaders.get(headerKey)) {
                webkitCookieManager.setCookie(url, headerValue);
            }
        }
    }

    @Override
    public Map<String, List<String>> get(final URI uri,
            final Map<String, List<String>> requestHeaders) throws IOException {
        if (uri == null) {
            throw new IOException("uri is null.");
        }
        final String url = uri.toString();
        final Map<String, List<String>> res = new HashMap<>();
        final String cookie = webkitCookieManager.getCookie(url);
        if (cookie != null) {
            res.put("Cookie", Arrays.asList(cookie));
        }
        return res;
    }
}
