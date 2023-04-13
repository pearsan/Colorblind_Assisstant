package android.support.p003v7.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/* renamed from: android.support.v7.widget.TintContextWrapper */
public class TintContextWrapper extends ContextWrapper {
    private static final ArrayList<WeakReference<TintContextWrapper>> sCache = new ArrayList<>();
    private Resources mResources;
    private final Resources.Theme mTheme = getResources().newTheme();

    public static Context wrap(@NonNull Context context) {
        if (!shouldWrap(context)) {
            return context;
        }
        int count = sCache.size();
        for (int i = 0; i < count; i++) {
            WeakReference<TintContextWrapper> ref = sCache.get(i);
            TintContextWrapper wrapper = ref != null ? (TintContextWrapper) ref.get() : null;
            if (wrapper != null && wrapper.getBaseContext() == context) {
                return wrapper;
            }
        }
        TintContextWrapper wrapper2 = new TintContextWrapper(context);
        sCache.add(new WeakReference(wrapper2));
        return wrapper2;
    }

    private static boolean shouldWrap(@NonNull Context context) {
        if (!(context instanceof TintContextWrapper) && !(context.getResources() instanceof TintResources)) {
            return true;
        }
        return false;
    }

    private TintContextWrapper(@NonNull Context base) {
        super(base);
        this.mTheme.setTo(base.getTheme());
    }

    public Resources.Theme getTheme() {
        return this.mTheme;
    }

    public void setTheme(int resid) {
        this.mTheme.applyStyle(resid, true);
    }

    public Resources getResources() {
        if (this.mResources == null) {
            this.mResources = new TintResources(this, super.getResources());
        }
        return this.mResources;
    }
}
