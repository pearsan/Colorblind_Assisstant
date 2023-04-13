package android.support.p003v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.p003v7.appcompat.C0191R;
import android.support.p003v7.text.AllCapsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;

/* renamed from: android.support.v7.widget.AppCompatTextHelper */
class AppCompatTextHelper {
    private static final int[] TEXT_APPEARANCE_ATTRS = {C0191R.attr.textAllCaps};
    private static final int[] VIEW_ATTRS = {16842804, 16843119, 16843117, 16843120, 16843118};
    private TintInfo mDrawableBottomTint;
    private TintInfo mDrawableLeftTint;
    private TintInfo mDrawableRightTint;
    private TintInfo mDrawableTopTint;
    final TextView mView;

    static AppCompatTextHelper create(TextView textView) {
        if (Build.VERSION.SDK_INT >= 17) {
            return new AppCompatTextHelperV17(textView);
        }
        return new AppCompatTextHelper(textView);
    }

    AppCompatTextHelper(TextView view) {
        this.mView = view;
    }

    /* access modifiers changed from: package-private */
    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        Context context = this.mView.getContext();
        AppCompatDrawableManager drawableManager = AppCompatDrawableManager.get();
        TypedArray a = context.obtainStyledAttributes(attrs, VIEW_ATTRS, defStyleAttr, 0);
        int ap = a.getResourceId(0, -1);
        if (a.hasValue(1)) {
            this.mDrawableLeftTint = createTintInfo(context, drawableManager, a.getResourceId(1, 0));
        }
        if (a.hasValue(2)) {
            this.mDrawableTopTint = createTintInfo(context, drawableManager, a.getResourceId(2, 0));
        }
        if (a.hasValue(3)) {
            this.mDrawableRightTint = createTintInfo(context, drawableManager, a.getResourceId(3, 0));
        }
        if (a.hasValue(4)) {
            this.mDrawableBottomTint = createTintInfo(context, drawableManager, a.getResourceId(4, 0));
        }
        a.recycle();
        if (!(this.mView.getTransformationMethod() instanceof PasswordTransformationMethod)) {
            boolean allCaps = false;
            boolean allCapsSet = false;
            if (ap != -1) {
                TypedArray appearance = context.obtainStyledAttributes(ap, C0191R.styleable.TextAppearance);
                if (appearance.hasValue(C0191R.styleable.TextAppearance_textAllCaps)) {
                    allCapsSet = true;
                    allCaps = appearance.getBoolean(C0191R.styleable.TextAppearance_textAllCaps, false);
                }
                appearance.recycle();
            }
            TypedArray a2 = context.obtainStyledAttributes(attrs, TEXT_APPEARANCE_ATTRS, defStyleAttr, 0);
            if (a2.hasValue(0)) {
                allCapsSet = true;
                allCaps = a2.getBoolean(0, false);
            }
            a2.recycle();
            if (allCapsSet) {
                setAllCaps(allCaps);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void onSetTextAppearance(Context context, int resId) {
        TypedArray appearance = context.obtainStyledAttributes(resId, TEXT_APPEARANCE_ATTRS);
        if (appearance.getBoolean(0, false)) {
            setAllCaps(true);
        }
        appearance.recycle();
    }

    /* access modifiers changed from: package-private */
    public void setAllCaps(boolean allCaps) {
        this.mView.setTransformationMethod(allCaps ? new AllCapsTransformationMethod(this.mView.getContext()) : null);
    }

    /* access modifiers changed from: package-private */
    public void applyCompoundDrawablesTints() {
        if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
            Drawable[] compoundDrawables = this.mView.getCompoundDrawables();
            applyCompoundDrawableTint(compoundDrawables[0], this.mDrawableLeftTint);
            applyCompoundDrawableTint(compoundDrawables[1], this.mDrawableTopTint);
            applyCompoundDrawableTint(compoundDrawables[2], this.mDrawableRightTint);
            applyCompoundDrawableTint(compoundDrawables[3], this.mDrawableBottomTint);
        }
    }

    /* access modifiers changed from: package-private */
    public final void applyCompoundDrawableTint(Drawable drawable, TintInfo info) {
        if (drawable != null && info != null) {
            AppCompatDrawableManager.tintDrawable(drawable, info, this.mView.getDrawableState());
        }
    }

    protected static TintInfo createTintInfo(Context context, AppCompatDrawableManager drawableManager, int drawableId) {
        ColorStateList tintList = drawableManager.getTintList(context, drawableId);
        if (tintList == null) {
            return null;
        }
        TintInfo tintInfo = new TintInfo();
        tintInfo.mHasTintList = true;
        tintInfo.mTintList = tintList;
        return tintInfo;
    }
}
