package com.colorblindpal.colorblindpal;

import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;

public class CBPPreferencesActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(16908290, new CBPPreferencesFragment()).commit();
        getWindow().getDecorView().setSystemUiVisibility(4);
    }
}
