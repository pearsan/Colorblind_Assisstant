package android.support.p003v7.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import java.lang.ref.WeakReference;

/* renamed from: android.support.v7.widget.TintResources */
public class TintResources extends Resources {
    private final WeakReference<Context> mContextRef;

    public TintResources(@NonNull Context context, @NonNull Resources res) {
        super(res.getAssets(), res.getDisplayMetrics(), res.getConfiguration());
        this.mContextRef = new WeakReference<>(context);
    }

    public Drawable getDrawable(int id) throws Resources.NotFoundException {
        Context context = (Context) this.mContextRef.get();
        if (context != null) {
            return AppCompatDrawableManager.get().onDrawableLoadedFromResources(context, this, id);
        }
        return super.getDrawable(id);
    }

    /* access modifiers changed from: package-private */
    public final Drawable superGetDrawable(int id) {
        return super.getDrawable(id);
    }
}
