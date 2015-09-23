
package jp.satorufujiwara.http.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.http.HttpResponseCache;
import android.os.Build;
import android.os.StatFs;

import java.io.File;
import java.io.IOException;

public final class CacheUtil {

    private static final Object LOCK = new Object();
    private static final int MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB
    private static volatile Object CACHE;

    private CacheUtil() {
        // util class
    }

    public static void install(final Context context, final String dirName) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return;
        }
        installCacheIcs(context, dirName);
    }

    private static void installCacheIcs(final Context context, final String dirName) {
        // DCL + volatile should be safe after Java 5.
        if (CACHE == null) {
            try {
                synchronized (LOCK) {
                    if (CACHE == null) {
                        CACHE = installHttpResponseCache(createCacheDir(context, dirName));
                    }
                }
            } catch (final IOException ignored) {
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static HttpResponseCache installHttpResponseCache(final File cacheDir)
            throws IOException {
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache == null) {
            final long maxSize = calculateDiskCacheSize(cacheDir);
            cache = HttpResponseCache.install(cacheDir, maxSize);
        }
        return cache;
    }

    private static File createCacheDir(final Context context, final String dirName) {
        final File cache = new File(context.getApplicationContext().getCacheDir(), dirName);
        if (!cache.exists()) {
            cache.mkdirs();
        }
        return cache;
    }

    private static long calculateDiskCacheSize(final File dir) {
        long size = MIN_DISK_CACHE_SIZE;
        try {
            // Target 2% of the total space.
            size = getAvailable(dir) / 50;
        } catch (final IllegalArgumentException ignored) {
        }
        return Math.max(Math.min(size, MAX_DISK_CACHE_SIZE), MIN_DISK_CACHE_SIZE);
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static long getAvailable(final File dir) {
        final StatFs statFs = new StatFs(dir.getAbsolutePath());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return ((long) statFs.getBlockCount()) * statFs.getBlockSize();
        }
        return statFs.getBlockCountLong() * statFs.getBlockSizeLong();
    }

}
