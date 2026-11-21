package com.gathin.tvhome;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;

/** Decode raster banners near their display size instead of retaining oversized app resources. */
final class BannerArt {
    // Android 9 can retain both a Home task and a regular launcher task. Share pixels between them,
    // without keeping an Activity, Resources object or otherwise-unused bitmap alive.
    private static final Map<String, WeakReference<Bitmap>> CACHE =
            new LinkedHashMap<String, WeakReference<Bitmap>>(16, .75f, true) {
                @Override protected boolean removeEldestEntry(Map.Entry<String, WeakReference<Bitmap>> entry) {
                    return size() > 16;
                }
            };

    private BannerArt() { }

    static void clear() {
        CACHE.clear();
    }

    static int sampleSize(int width, int target) {
        int sample = 1;
        if (target <= 0) return sample;
        while (width / sample / 2 >= target) sample *= 2;
        return sample;
    }

    static Drawable load(Resources resources, int id, int target, String apk) {
        // Source paths and configuration separate APK/locale/density variants; broadcasts invalidate updates.
        String key = apk + ':' + id + ':' + target + ':' + resources.getConfiguration().hashCode();
        WeakReference<Bitmap> reference = CACHE.get(key);
        Bitmap cached = reference == null ? null : reference.get();
        if (cached != null && !cached.isRecycled()) return new BitmapDrawable(resources, cached);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inScaled = false;
        BitmapFactory.decodeResource(resources, id, options);
        // XML drawables can wrap arbitrarily large rasters. Use the tile icon when bounds are unknown.
        if (options.outWidth <= 0 || options.outHeight <= 0) return null;
        if (!AppAdapter.isBannerShaped(options.outWidth, options.outHeight)) return null;
        options.inSampleSize = sampleSize(options.outWidth, target);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, id, options);
        if (bitmap == null) return null;
        if (bitmap.getWidth() > target) {
            int height = Math.max(1, Math.round(bitmap.getHeight() * (target / (float) bitmap.getWidth())));
            Bitmap fitted = Bitmap.createScaledBitmap(bitmap, target, height, true);
            if (fitted != bitmap) bitmap.recycle();
            bitmap = fitted;
        }
        CACHE.put(key, new WeakReference<>(bitmap));
        return new BitmapDrawable(resources, bitmap);
    }
}
