package android.support.p003v7.widget;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.p000v4.view.ViewCompat;
import android.support.p003v7.appcompat.C0191R;
import android.util.AttributeSet;
import android.view.View;

/* renamed from: android.support.v7.widget.AppCompatBackgroundHelper */
class AppCompatBackgroundHelper {
    private TintInfo mBackgroundTint;
    private final AppCompatDrawableManager mDrawableManager;
    private TintInfo mInternalBackgroundTint;
    private TintInfo mTmpInfo;
    private final View mView;

    AppCompatBackgroundHelper(View view, AppCompatDrawableManager drawableManager) {
        this.mView = view;
        this.mDrawableManager = drawableManager;
    }

    /* access modifiers changed from: package-private */
    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        ColorStateList tint;
        TypedArray a = this.mView.getContext().obtainStyledAttributes(attrs, C0191R.styleable.ViewBackgroundHelper, defStyleAttr, 0);
        try {
            if (a.hasValue(C0191R.styleable.ViewBackgroundHelper_android_background) && (tint = this.mDrawableManager.getTintList(this.mView.getContext(), a.getResourceId(C0191R.styleable.ViewBackgroundHelper_android_background, -1))) != null) {
                setInternalBackgroundTint(tint);
            }
            if (a.hasValue(C0191R.styleable.ViewBackgroundHelper_backgroundTint)) {
                ViewCompat.setBackgroundTintList(this.mView, a.getColorStateList(C0191R.styleable.ViewBackgroundHelper_backgroundTint));
            }
            if (a.hasValue(C0191R.styleable.ViewBackgroundHelper_backgroundTintMode)) {
                ViewCompat.setBackgroundTintMode(this.mView, DrawableUtils.parseTintMode(a.getInt(C0191R.styleable.ViewBackgroundHelper_backgroundTintMode, -1), (PorterDuff.Mode) null));
            }
        } finally {
            a.recycle();
        }
    }

    /* access modifiers changed from: package-private */
    public void onSetBackgroundResource(int resId) {
        setInternalBackgroundTint(this.mDrawableManager != null ? this.mDrawableManager.getTintList(this.mView.getContext(), resId) : null);
    }

    /* access modifiers changed from: package-private */
    public void onSetBackgroundDrawable(Drawable background) {
        setInternalBackgroundTint((ColorStateList) null);
    }

    /* access modifiers changed from: package-private */
    public void setSupportBackgroundTintList(ColorStateList tint) {
        if (this.mBackgroundTint == null) {
            this.mBackgroundTint = new TintInfo();
        }
        this.mBackgroundTint.mTintList = tint;
        this.mBackgroundTint.mHasTintList = true;
        applySupportBackgroundTint();
    }

    /* access modifiers changed from: package-private */
    public ColorStateList getSupportBackgroundTintList() {
        if (this.mBackgroundTint != null) {
            return this.mBackgroundTint.mTintList;
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void setSupportBackgroundTintMode(PorterDuff.Mode tintMode) {
        if (this.mBackgroundTint == null) {
            this.mBackgroundTint = new TintInfo();
        }
        this.mBackgroundTint.mTintMode = tintMode;
        this.mBackgroundTint.mHasTintMode = true;
        applySupportBackgroundTint();
    }

    /* access modifiers changed from: package-private */
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        if (this.mBackgroundTint != null) {
            return this.mBackgroundTint.mTintMode;
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void applySupportBackgroundTint() {
        Drawable background = this.mView.getBackground();
        if (background == null) {
            return;
        }
        if (this.mBackgroundTint != null) {
            AppCompatDrawableManager.tintDrawable(background, this.mBackgroundTint, this.mView.getDrawableState());
        } else if (this.mInternalBackgroundTint != null) {
            AppCompatDrawableManager.tintDrawable(background, this.mInternalBackgroundTint, this.mView.getDrawableState());
        } else if (shouldCompatTintUsingFrameworkTint(background)) {
            compatTintDrawableUsingFrameworkTint(background);
        }
    }

    /* access modifiers changed from: package-private */
    public void setInternalBackgroundTint(ColorStateList tint) {
        if (tint != null) {
            if (this.mInternalBackgroundTint == null) {
                this.mInternalBackgroundTint = new TintInfo();
            }
            this.mInternalBackgroundTint.mTintList = tint;
            this.mInternalBackgroundTint.mHasTintList = true;
        } else {
            this.mInternalBackgroundTint = null;
        }
        applySupportBackgroundTint();
    }

    private boolean shouldCompatTintUsingFrameworkTint(@NonNull Drawable background) {
        return Build.VERSION.SDK_INT == 21 && (background instanceof GradientDrawable);
    }

    private void compatTintDrawableUsingFrameworkTint(@NonNull Drawable background) {
        if (this.mTmpInfo == null) {
            this.mTmpInfo = new TintInfo();
        }
        TintInfo info = this.mTmpInfo;
        info.clear();
        ColorStateList tintList = ViewCompat.getBackgroundTintList(this.mView);
        if (tintList != null) {
            info.mHasTintList = true;
            info.mTintList = tintList;
        }
        PorterDuff.Mode mode = ViewCompat.getBackgroundTintMode(this.mView);
        if (mode != null) {
            info.mHasTintMode = true;
            info.mTintMode = mode;
        }
        if (info.mHasTintList || info.mHasTintMode) {
            AppCompatDrawableManager.tintDrawable(background, info, this.mView.getDrawableState());
        }
    }
}
