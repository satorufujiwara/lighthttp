
package jp.satorufujiwara.http.utils;

import java.net.Authenticator;
import java.net.InetAddress;
import java.net.PasswordAuthentication;
import java.util.HashMap;
import java.util.Map;

public class BasicAuthenticator extends Authenticator {

    private final Map<String, PasswordAuthentication> authMap = new HashMap<>();

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        final InetAddress address = getRequestingSite();
        final String hostName = address != null ? address.getHostName() : null;
        return hostName != null ? authMap.get(hostName) : null;
    }

    public void addAuthorize(final String host, final String userName, final String password) {
        if (host == null || userName == null || password == null) {
            return;
        }
        authMap.put(host, new PasswordAuthentication(userName, password.toCharArray()));
    }
}
