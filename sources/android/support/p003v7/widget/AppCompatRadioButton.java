package android.support.p003v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.p000v4.content.ContextCompat;
import android.support.p000v4.widget.TintableCompoundButton;
import android.support.p003v7.appcompat.C0191R;
import android.util.AttributeSet;
import android.widget.RadioButton;

/* renamed from: android.support.v7.widget.AppCompatRadioButton */
public class AppCompatRadioButton extends RadioButton implements TintableCompoundButton {
    private AppCompatCompoundButtonHelper mCompoundButtonHelper;
    private AppCompatDrawableManager mDrawableManager;

    public AppCompatRadioButton(Context context) {
        this(context, (AttributeSet) null);
    }

    public AppCompatRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, C0191R.attr.radioButtonStyle);
    }

    public AppCompatRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(TintContextWrapper.wrap(context), attrs, defStyleAttr);
        this.mDrawableManager = AppCompatDrawableManager.get();
        this.mCompoundButtonHelper = new AppCompatCompoundButtonHelper(this, this.mDrawableManager);
        this.mCompoundButtonHelper.loadFromAttributes(attrs, defStyleAttr);
    }

    public void setButtonDrawable(Drawable buttonDrawable) {
        super.setButtonDrawable(buttonDrawable);
        if (this.mCompoundButtonHelper != null) {
            this.mCompoundButtonHelper.onSetButtonDrawable();
        }
    }

    public void setButtonDrawable(@DrawableRes int resId) {
        setButtonDrawable(this.mDrawableManager != null ? this.mDrawableManager.getDrawable(getContext(), resId) : ContextCompat.getDrawable(getContext(), resId));
    }

    public int getCompoundPaddingLeft() {
        int value = super.getCompoundPaddingLeft();
        return this.mCompoundButtonHelper != null ? this.mCompoundButtonHelper.getCompoundPaddingLeft(value) : value;
    }

    public void setSupportButtonTintList(@Nullable ColorStateList tint) {
        if (this.mCompoundButtonHelper != null) {
            this.mCompoundButtonHelper.setSupportButtonTintList(tint);
        }
    }

    @Nullable
    public ColorStateList getSupportButtonTintList() {
        if (this.mCompoundButtonHelper != null) {
            return this.mCompoundButtonHelper.getSupportButtonTintList();
        }
        return null;
    }

    public void setSupportButtonTintMode(@Nullable PorterDuff.Mode tintMode) {
        if (this.mCompoundButtonHelper != null) {
            this.mCompoundButtonHelper.setSupportButtonTintMode(tintMode);
        }
    }

    @Nullable
    public PorterDuff.Mode getSupportButtonTintMode() {
        if (this.mCompoundButtonHelper != null) {
            return this.mCompoundButtonHelper.getSupportButtonTintMode();
        }
        return null;
    }
}
