package com.colorblindpal.colorblindpal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class CBPPreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Preference aboutPreference;
    private Preference cameraQualityPreference;
    private Preference colorBlindnessTypePreference;
    private Preference colorNamePackPreference;
    ColorSingleton colorSingleton = ColorSingleton.getInstance();
    Context context;
    private Preference highlightColorPreference;
    private Preference minimumSaturationPreference;
    private Preference sampleSizePreference;
    private Preference stripesHuePreferencePreference;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(C0273R.xml.preferences);
        PreferenceManager.getDefaultSharedPreferences(ColorBlindPalApp.getAppContext()).registerOnSharedPreferenceChangeListener(this);
        this.context = getActivity();
        this.aboutPreference = findPreference(getString(C0273R.string.pref_key_about));
        this.colorBlindnessTypePreference = findPreference(getString(C0273R.string.pref_key_colorblindnesstype));
        this.colorNamePackPreference = findPreference(getString(C0273R.string.pref_key_colornamepack));
        this.cameraQualityPreference = findPreference(getString(C0273R.string.pref_key_cameraquality));
        this.sampleSizePreference = findPreference(getString(C0273R.string.pref_key_samplesize));
        this.highlightColorPreference = findPreference(getString(C0273R.string.pref_key_highlightcolor));
        this.minimumSaturationPreference = findPreference(getString(C0273R.string.pref_key_minsaturation));
        this.stripesHuePreferencePreference = findPreference(getString(C0273R.string.pref_key_stripehue));
        updatePreferenceSummaries();
        this.aboutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(CBPPreferencesFragment.this.context).setTitle(CBPPreferencesFragment.this.context.getString(C0273R.string.pref_about_title)).setMessage(CBPPreferencesFragment.this.context.getString(C0273R.string.pref_about_message)).setIcon(C0273R.mipmap.ic_launcher).show();
                return true;
            }
        });
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        ColorSingleton.getInstance().updatePreferences();
        updatePreferenceSummaries();
    }

    private void updatePreferenceSummaries() {
        this.colorBlindnessTypePreference.setSummary(Utilities.stringForColorBlindnessType(this.colorSingleton.getColorBlindnessType(), this.context));
        this.colorNamePackPreference.setSummary(Utilities.stringForColorNamePack(this.colorSingleton.getColorNamePack(), this.context));
        this.cameraQualityPreference.setSummary(Utilities.stringForCameraQuality((double) this.colorSingleton.getCameraQuality(), this.context));
        this.sampleSizePreference.setSummary(Utilities.stringForSampleSize(this.colorSingleton.getColorSelectorSampleSize(), this.context));
        this.highlightColorPreference.setSummary(hueNameForColor(this.colorSingleton.getHighlightColor(), ColorNamePack.Scientific, this.context));
        this.minimumSaturationPreference.setSummary(stringForDoublePercent(this.colorSingleton.getMinimumSaturation()));
        this.stripesHuePreferencePreference.setSummary(Utilities.hueName(this.colorSingleton.getStripeHue(), ColorNamePack.Scientific));
    }

    private String stringForDoublePercent(double percent) {
        return String.valueOf((int) (100.0d * percent)) + "%%";
    }

    private String hueNameForColor(CBPColor color, ColorNamePack colorNamePack, Context context2) {
        if (color.f21r == 255 && color.f18g == 255 && color.f17b == 255) {
            return context2.getString(C0273R.string.white_display);
        }
        if (color.f21r == 0 && color.f18g == 0 && color.f17b == 0) {
            return context2.getString(C0273R.string.black_display);
        }
        float[] hsv = new float[3];
        Color.colorToHSV(Color.rgb(color.f21r, color.f18g, color.f17b), hsv);
        return Utilities.hueName((double) hsv[0], colorNamePack);
    }
}
