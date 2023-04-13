package com.colorblindpal.colorblindpal;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class CameraToggleImageButton extends ImageButton {
    private boolean checked = false;
    public int checkedDrawable = -1;
    public int uncheckedDrawable = -1;

    public CameraToggleImageButton(Context context) {
        super(context);
    }

    public CameraToggleImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraToggleImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked2) {
        this.checked = checked2;
        if (this.checkedDrawable != -1 && this.uncheckedDrawable != -1) {
            if (checked2) {
                setImageResource(this.checkedDrawable);
            } else {
                setImageResource(this.uncheckedDrawable);
            }
        }
    }
}
