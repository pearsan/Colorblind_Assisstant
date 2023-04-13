package com.colorblindpal.colorblindpal;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

public class CameraToggleButton extends ToggleButton {
    public boolean handleToggle = true;

    public CameraToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDisplay();
    }

    public void toggle() {
        if (this.handleToggle) {
            super.toggle();
        }
    }

    public void setChecked(boolean checked) {
        super.setChecked(checked);
        setDisplay();
    }

    public void setAllText(String text) {
        setText(text);
        setTextOn(text);
        setTextOff(text);
    }

    private void setDisplay() {
        if (isChecked()) {
            Utilities.clearTextShadow(this);
        } else {
            Utilities.setTextShadow(getContext(), this);
        }
    }
}
