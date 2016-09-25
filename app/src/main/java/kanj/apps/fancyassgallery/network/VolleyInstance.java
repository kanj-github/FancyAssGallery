package kanj.apps.fancyassgallery.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by voldemort on 25/9/16.
 */

public class VolleyInstance {
    private static VolleyInstance instance;
    private RequestQueue queue;
    private ImageLoader imageLoader;
    private static Context ctx;

    private VolleyInstance(Context ctx) {
        this.ctx = ctx;
        queue = getRequestQueue();

        imageLoader = new ImageLoader(queue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static VolleyInstance getInstance(Context ctx) {
        if (instance == null) {
            instance = new VolleyInstance(ctx.getApplicationContext());
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(ctx);
        }
        return queue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
