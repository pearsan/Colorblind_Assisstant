package com.colorblindpal.colorblindpal;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ColorSingleton {
    private static ColorSingleton ourInstance = new ColorSingleton();
    private int cameraQuality;
    private ColorBlindnessType colorBlindnessType;
    private ColorNamePack colorNamePack;
    private boolean colorSelectorDeveloperColors;
    private int colorSelectorSampleSize;
    private CBPColor highlightColor;
    private double minimumSaturation;
    private double stripeHue;

    public static ColorSingleton getInstance() {
        return ourInstance;
    }

    private ColorSingleton() {
        updatePreferences();
    }

    private int loadCameraQuality() {
        Context context = ColorBlindPalApp.getAppContext();
        return Utilities.cameraQualityForStringValue(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(C0273R.string.pref_key_cameraquality), context.getString(C0273R.string.pref_cameraquality_defaultvalue)), context);
    }

    private ColorBlindnessType loadColorBlindnessType() {
        Context context = ColorBlindPalApp.getAppContext();
        return Utilities.colorBlindnessTypeForStringValue(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(C0273R.string.pref_key_colorblindnesstype), context.getString(C0273R.string.pref_colorblindnesstype_defaultvalue)), context);
    }

    public ColorBlindnessType getColorBlindnessType() {
        return this.colorBlindnessType;
    }

    private CBPColor loadHighlightColor() {
        Context context = ColorBlindPalApp.getAppContext();
        return Utilities.highlightColorForStringValue(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(C0273R.string.pref_key_highlightcolor), context.getString(C0273R.string.pref_highlightcolor_defaultvalue)), context);
    }

    public CBPColor getHighlightColor() {
        return this.highlightColor;
    }

    private double loadMinimumSaturation() {
        Context context = ColorBlindPalApp.getAppContext();
        return Utilities.minimumSaturationForStringValue(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(C0273R.string.pref_key_minsaturation), context.getString(C0273R.string.pref_minsaturation_defaultvalue)), context);
    }

    public double getMinimumSaturation() {
        return this.minimumSaturation;
    }

    private ColorNamePack loadColorNamePack() {
        Context context = ColorBlindPalApp.getAppContext();
        return Utilities.colorNamePackForStringValue(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(C0273R.string.pref_key_colornamepack), context.getString(C0273R.string.pref_colornamepack_defaultvalue)), context);
    }

    public ColorNamePack getColorNamePack() {
        return this.colorNamePack;
    }

    public int getCameraQuality() {
        return this.cameraQuality;
    }

    public int getCameraFreezeQuality() {
        int medQuality = ColorBlindPalApp.getAppContext().getResources().getInteger(C0273R.integer.cameraquality_med_factor);
        return this.cameraQuality > medQuality ? medQuality : this.cameraQuality;
    }

    private boolean loadColorSelectorDeveloperColors() {
        Context context = ColorBlindPalApp.getAppContext();
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(C0273R.string.pref_key_developercolors), context.getResources().getBoolean(C0273R.bool.pref_developercolors_defaultvalue));
    }

    public boolean getColorSelectorDeveloperColors() {
        return this.colorSelectorDeveloperColors;
    }

    private int loadColorSelectorSampleSize() {
        Context context = ColorBlindPalApp.getAppContext();
        return Utilities.sampleSizeForStringValue(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(C0273R.string.pref_key_samplesize), context.getString(C0273R.string.pref_samplesize_defaultvalue)), context);
    }

    public int getColorSelectorSampleSize() {
        return this.colorSelectorSampleSize;
    }

    private double loadStripeHue() {
        Context context = ColorBlindPalApp.getAppContext();
        return Utilities.stripeHueForStringValue(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(C0273R.string.pref_key_stripehue), context.getString(C0273R.string.pref_stripehue_defaultvalue)), context);
    }

    public double getStripeHue() {
        return this.stripeHue;
    }

    public long getLastTutorialTime() {
        Context context = ColorBlindPalApp.getAppContext();
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(context.getString(C0273R.string.sharedpref_key_lasttutorialtime), 0);
    }

    public void saveLastTutorialTime(long time) {
        Context context = ColorBlindPalApp.getAppContext();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(context.getString(C0273R.string.sharedpref_key_lasttutorialtime), time);
        editor.apply();
    }

    public void updatePreferences() {
        this.colorBlindnessType = loadColorBlindnessType();
        this.highlightColor = loadHighlightColor();
        this.minimumSaturation = loadMinimumSaturation();
        this.cameraQuality = loadCameraQuality();
        this.colorNamePack = loadColorNamePack();
        this.colorSelectorDeveloperColors = loadColorSelectorDeveloperColors();
        this.colorSelectorSampleSize = loadColorSelectorSampleSize();
        this.stripeHue = loadStripeHue();
    }
}
