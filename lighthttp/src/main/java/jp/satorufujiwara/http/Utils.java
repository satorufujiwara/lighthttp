package jp.satorufujiwara.http;

import java.io.Closeable;

public final class Utils {

    private Utils() {

    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (RuntimeException rethrown) {
            throw rethrown;
        } catch (Exception ignore) {

        }

    }

}
