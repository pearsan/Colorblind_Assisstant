package com.colorblindpal.colorblindpal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Looper;
import android.support.p000v4.media.TransportMediator;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Utilities {
    private static ArrayList<CBPColor> colloquialColorNamesArrayBacking = null;
    private static final ArrayList<ColorRange> hueLightnessArrayCommon = new ArrayList<>(Arrays.asList(new ColorRange[]{ColorRange.colorRangeWithHueAndLightness(51.1d, 66.0d, 0.1d, 0.38d, "Olive"), ColorRange.colorRangeWithHueAndLightness(167.0d, 184.0d, 0.5d, 1.1d, "Cyan"), ColorRange.colorRangeWithHueAndLightness(189.9d, 193.8d, 0.58d, 1.1d, "Sky Blue"), ColorRange.colorRangeWithHueAndLightness(180.7d, 201.0d, 0.51d, 0.7d, "Light Blue"), ColorRange.colorRangeWithHueAndLightness(194.6d, 266.0d, 0.0d, 0.09d, "Navy Blue"), ColorRange.colorRangeWithHueAndLightness(317.0d, 344.0d, 0.36d, 1.1d, "Pink"), ColorRange.colorRangeWithHueAndLightness(327.0d, 339.0d, 0.26d, 0.36d, "Magenta"), ColorRange.colorRangeWithHueAndLightness(331.0d, 8.0d, 0.0d, 0.22d, "Maroon"), ColorRange.colorRangeWithHueAndLightness(37.9d, 44.2d, 0.41d, 0.44d, "Gold"), ColorRange.colorRangeWithHueAndLightness(11.0d, 51.1d, 0.0d, 0.05d, "Dark Brown"), ColorRange.colorRangeWithHueAndLightness(11.0d, 51.1d, 0.05d, 0.4d, "Brown"), ColorRange.colorRangeWithHueAndLightness(38.0d, 70.2d, 0.5d, 0.7d, "Yellow"), ColorRange.colorRangeWithHueAndLightness(46.0d, 75.2d, 0.55d, 0.7d, "Yellow"), ColorRange.colorRangeWithHueAndLightness(77.6d, 100.8d, 0.58d, 0.62d, "Lime Green"), ColorRange.colorRangeWithHueAndLightness(38.7d, 65.0d, 0.39d, 0.42d, "Mustard")}));
    private static final ArrayList<ColorRange> hueRangesArrayCommon = new ArrayList<>(Arrays.asList(new ColorRange[]{ColorRange.colorRangeWithHues(65.0d, 156.5d, "Green"), ColorRange.colorRangeWithHues(156.5d, 186.0d, "Teal"), ColorRange.colorRangeWithHues(186.0d, 255.0d, "Blue"), ColorRange.colorRangeWithHues(255.0d, 333.0d, "Purple"), ColorRange.colorRangeWithHues(333.0d, 9.0d, "Red"), ColorRange.colorRangeWithHues(9.0d, 50.0d, "Orange"), ColorRange.colorRangeWithHues(50.0d, 65.0d, "Yellow")}));
    private static final ArrayList<ColorRange> hueRangesArrayScientific = new ArrayList<>(Arrays.asList(new ColorRange[]{ColorRange.colorRangeWithHues(345.0d, 15.0d, "Red"), ColorRange.colorRangeWithHues(15.0d, 45.0d, "Orange"), ColorRange.colorRangeWithHues(45.0d, 75.0d, "Yellow"), ColorRange.colorRangeWithHues(75.0d, 105.0d, "Lime"), ColorRange.colorRangeWithHues(105.0d, 135.0d, "Green"), ColorRange.colorRangeWithHues(135.0d, 165.0d, "Turquoise"), ColorRange.colorRangeWithHues(165.0d, 195.0d, "Cyan"), ColorRange.colorRangeWithHues(195.0d, 225.0d, "Cobalt"), ColorRange.colorRangeWithHues(225.0d, 255.0d, "Blue"), ColorRange.colorRangeWithHues(255.0d, 285.0d, "Violet"), ColorRange.colorRangeWithHues(285.0d, 315.0d, "Magenta"), ColorRange.colorRangeWithHues(315.0d, 345.0d, "Crimson")}));
    private static final ArrayList<ColorRange> lightnessRangesArrayCommon = new ArrayList<>(Arrays.asList(new ColorRange[]{ColorRange.colorRangeWithLightness(0.0d, 0.1d, "Very Dark"), ColorRange.colorRangeWithLightness(0.1d, 0.3d, "Dark"), ColorRange.colorRangeWithLightness(0.7d, 0.9d, "Light"), ColorRange.colorRangeWithLightness(0.9d, 1.1d, "Very Light")}));
    private static final ArrayList<ColorRange> lightnessRangesArrayScientific = new ArrayList<>(Arrays.asList(new ColorRange[]{ColorRange.colorRangeWithLightness(0.0d, 0.1d, "Very Dark"), ColorRange.colorRangeWithLightness(0.1d, 0.3d, "Dark"), ColorRange.colorRangeWithLightness(0.3d, 0.7d, "Medium"), ColorRange.colorRangeWithLightness(0.7d, 0.9d, "Light"), ColorRange.colorRangeWithLightness(0.9d, 1.1d, "Very Light")}));
    private static final ArrayList<LightSatWord> lightnessSaturationWordsArrayCommon = new ArrayList<>(Arrays.asList(new LightSatWord[]{new LightSatWord("Dark", "Bold", "Deep"), new LightSatWord("Dark", "Vivid", "Deep"), new LightSatWord("Very Dark", "Bold", "Deep"), new LightSatWord("Very Dark", "Vivid", "Very Deep"), new LightSatWord("Light", "Dull", "Faint"), new LightSatWord("Light", "Very Dull", "Faint"), new LightSatWord("Very Light", "Dull", "Faint"), new LightSatWord("Very Light", "Very Dull", "Very Faint")}));
    private static final ArrayList<ColorRange> saturationRangesArrayCommon = new ArrayList<>(Arrays.asList(new ColorRange[]{ColorRange.colorRangeWithSaturation(0.0d, 0.1d, "Very Dull"), ColorRange.colorRangeWithSaturation(0.1d, 0.45d, "Dull"), ColorRange.colorRangeWithSaturation(0.75d, 0.9d, "Bold"), ColorRange.colorRangeWithSaturation(0.9d, 1.1d, "Vivid")}));
    private static final ArrayList<ColorRange> saturationRangesArrayScientific = new ArrayList<>(Arrays.asList(new ColorRange[]{ColorRange.colorRangeWithSaturation(0.0d, 0.1d, "Very Dull"), ColorRange.colorRangeWithSaturation(0.1d, 0.45d, "Dull"), ColorRange.colorRangeWithSaturation(0.45d, 0.75d, "Normal"), ColorRange.colorRangeWithSaturation(0.75d, 0.9d, "Bold"), ColorRange.colorRangeWithSaturation(0.9d, 1.1d, "Very Bold")}));

    public static String stringForColorBlindnessType(ColorBlindnessType colorBlindnessType, Context context) {
        if (colorBlindnessType == ColorBlindnessType.Normal) {
            return context.getString(C0273R.string.normal_vision_display);
        }
        if (colorBlindnessType == ColorBlindnessType.Protanopia) {
            return context.getString(C0273R.string.protanopia_display);
        }
        if (colorBlindnessType == ColorBlindnessType.Deuteranopia) {
            return context.getString(C0273R.string.deuteranopia_display);
        }
        if (colorBlindnessType == ColorBlindnessType.Tritanopia) {
            return context.getString(C0273R.string.tritanopia_display);
        }
        if (colorBlindnessType == ColorBlindnessType.ProtanopiaSimulate) {
            return context.getString(C0273R.string.protanopia_simulation_display);
        }
        if (colorBlindnessType == ColorBlindnessType.ProtanomalySimulate) {
            return context.getString(C0273R.string.protanomaly_simulation_display);
        }
        if (colorBlindnessType == ColorBlindnessType.DeuteranopiaSimulate) {
            return context.getString(C0273R.string.deuteranopia_simulation_display);
        }
        if (colorBlindnessType == ColorBlindnessType.DeuteranomalySimulate) {
            return context.getString(C0273R.string.deuteranomaly_simulation_display);
        }
        if (colorBlindnessType == ColorBlindnessType.TritanopiaSimulate) {
            return context.getString(C0273R.string.tritanopia_simulation_display);
        }
        return context.getString(C0273R.string.empty_string);
    }

    public static ColorBlindnessType colorBlindnessTypeForStringValue(String value, Context context) {
        if (value.equals(context.getString(C0273R.string.normal_vision_value))) {
            return ColorBlindnessType.Normal;
        }
        if (value.equals(context.getString(C0273R.string.protanopia_value))) {
            return ColorBlindnessType.Protanopia;
        }
        if (value.equals(context.getString(C0273R.string.deuteranopia_value))) {
            return ColorBlindnessType.Deuteranopia;
        }
        if (value.equals(context.getString(C0273R.string.tritanopia_value))) {
            return ColorBlindnessType.Tritanopia;
        }
        if (value.equals(context.getString(C0273R.string.protanopia_simulation_value))) {
            return ColorBlindnessType.ProtanopiaSimulate;
        }
        if (value.equals(context.getString(C0273R.string.protanomaly_simulation_value))) {
            return ColorBlindnessType.ProtanomalySimulate;
        }
        if (value.equals(context.getString(C0273R.string.deuteranopia_simulation_value))) {
            return ColorBlindnessType.DeuteranopiaSimulate;
        }
        if (value.equals(context.getString(C0273R.string.deuteranomaly_simulation_value))) {
            return ColorBlindnessType.DeuteranomalySimulate;
        }
        if (value.equals(context.getString(C0273R.string.tritanopia_simulation_value))) {
            return ColorBlindnessType.TritanopiaSimulate;
        }
        return ColorBlindnessType.Normal;
    }

    public static ColorNamePack colorNamePackForStringValue(String value, Context context) {
        if (value.equals(context.getString(C0273R.string.common_value))) {
            return ColorNamePack.Common;
        }
        if (value.equals(context.getString(C0273R.string.scientific_value))) {
            return ColorNamePack.Scientific;
        }
        if (value.equals(context.getString(C0273R.string.colloquial_value))) {
            return ColorNamePack.Colloquial;
        }
        return ColorNamePack.Common;
    }

    public static int cameraQualityForStringValue(String value, Context context) {
        if (value.equals(context.getString(C0273R.string.lowquality_value))) {
            return context.getResources().getInteger(C0273R.integer.cameraquality_low_factor);
        }
        if (value.equals(context.getString(C0273R.string.medquality_value))) {
            return context.getResources().getInteger(C0273R.integer.cameraquality_med_factor);
        }
        if (value.equals(context.getString(C0273R.string.highquality_value))) {
            return context.getResources().getInteger(C0273R.integer.cameraquality_high_factor);
        }
        return context.getResources().getInteger(C0273R.integer.cameraquality_high_factor);
    }

    public static int sampleSizeForStringValue(String value, Context context) {
        if (value.equals(context.getString(C0273R.string.small_value))) {
            return context.getResources().getInteger(C0273R.integer.samplesize_small_pixels);
        }
        if (value.equals(context.getString(C0273R.string.medium_value))) {
            return context.getResources().getInteger(C0273R.integer.samplesize_medium_pixels);
        }
        if (value.equals(context.getString(C0273R.string.large_value))) {
            return context.getResources().getInteger(C0273R.integer.samplesize_large_pixels);
        }
        return context.getResources().getInteger(C0273R.integer.samplesize_small_pixels);
    }

    public static String stringForSampleSize(int sampleSize, Context context) {
        if (sampleSize <= context.getResources().getInteger(C0273R.integer.samplesize_small_pixels)) {
            return context.getString(C0273R.string.small_display);
        }
        if (sampleSize <= context.getResources().getInteger(C0273R.integer.samplesize_medium_pixels)) {
            return context.getString(C0273R.string.medium_display);
        }
        return context.getString(C0273R.string.large_display);
    }

    public static CBPColor highlightColorForStringValue(String value, Context context) {
        if (value.equals(context.getString(C0273R.string.red_value))) {
            return CBPColor.cbpColorWithRGB(255, 0, 0);
        }
        if (value.equals(context.getString(C0273R.string.orange_value))) {
            return CBPColor.cbpColorWithRGB(255, TransportMediator.KEYCODE_MEDIA_PAUSE, 0);
        }
        if (value.equals(context.getString(C0273R.string.yellow_value))) {
            return CBPColor.cbpColorWithRGB(255, 255, 0);
        }
        if (value.equals(context.getString(C0273R.string.lime_value))) {
            return CBPColor.cbpColorWithRGB(TransportMediator.KEYCODE_MEDIA_PAUSE, 255, 0);
        }
        if (value.equals(context.getString(C0273R.string.green_value))) {
            return CBPColor.cbpColorWithRGB(0, 255, 0);
        }
        if (value.equals(context.getString(C0273R.string.turquoise_value))) {
            return CBPColor.cbpColorWithRGB(0, 255, TransportMediator.KEYCODE_MEDIA_PAUSE);
        }
        if (value.equals(context.getString(C0273R.string.cyan_value))) {
            return CBPColor.cbpColorWithRGB(0, 255, 255);
        }
        if (value.equals(context.getString(C0273R.string.cobalt_value))) {
            return CBPColor.cbpColorWithRGB(0, TransportMediator.KEYCODE_MEDIA_PAUSE, 255);
        }
        if (value.equals(context.getString(C0273R.string.blue_value))) {
            return CBPColor.cbpColorWithRGB(0, 0, 255);
        }
        if (value.equals(context.getString(C0273R.string.violet_value))) {
            return CBPColor.cbpColorWithRGB(TransportMediator.KEYCODE_MEDIA_PAUSE, 0, 255);
        }
        if (value.equals(context.getString(C0273R.string.magenta_value))) {
            return CBPColor.cbpColorWithRGB(255, 0, 255);
        }
        if (value.equals(context.getString(C0273R.string.crimson_value))) {
            return CBPColor.cbpColorWithRGB(255, 0, TransportMediator.KEYCODE_MEDIA_PAUSE);
        }
        if (value.equals(context.getString(C0273R.string.black_value))) {
            return CBPColor.cbpColorWithRGB(0, 0, 0);
        }
        if (value.equals(context.getString(C0273R.string.white_value))) {
            return CBPColor.cbpColorWithRGB(255, 255, 255);
        }
        return CBPColor.cbpColorWithRGB(0, TransportMediator.KEYCODE_MEDIA_PAUSE, 255);
    }

    public static double minimumSaturationForStringValue(String value, Context context) {
        if (value.equals(context.getString(C0273R.string.zero_percent_value))) {
            return 0.0d;
        }
        if (value.equals(context.getString(C0273R.string.ten_percent_value))) {
            return 0.1d;
        }
        if (value.equals(context.getString(C0273R.string.twenty_percent_value))) {
            return 0.2d;
        }
        if (value.equals(context.getString(C0273R.string.thirty_percent_value))) {
            return 0.3d;
        }
        if (value.equals(context.getString(C0273R.string.forty_percent_value))) {
            return 0.4d;
        }
        if (value.equals(context.getString(C0273R.string.fifty_percent_value))) {
            return 0.5d;
        }
        if (value.equals(context.getString(C0273R.string.sixty_percent_value))) {
            return 0.6d;
        }
        if (value.equals(context.getString(C0273R.string.seventy_percent_value))) {
            return 0.7d;
        }
        if (value.equals(context.getString(C0273R.string.eighty_percent_value))) {
            return 0.8d;
        }
        if (value.equals(context.getString(C0273R.string.ninety_percent_value))) {
            return 0.9d;
        }
        if (value.equals(context.getString(C0273R.string.hundred_percent_value))) {
            return 1.0d;
        }
        return 0.1d;
    }

    public static double stripeHueForStringValue(String value, Context context) {
        if (value.equals(context.getString(C0273R.string.red_value))) {
            return 0.0d;
        }
        if (value.equals(context.getString(C0273R.string.orange_value))) {
            return 30.0d;
        }
        if (value.equals(context.getString(C0273R.string.yellow_value))) {
            return 60.0d;
        }
        if (value.equals(context.getString(C0273R.string.lime_value))) {
            return 90.0d;
        }
        if (value.equals(context.getString(C0273R.string.green_value))) {
            return 120.0d;
        }
        if (value.equals(context.getString(C0273R.string.turquoise_value))) {
            return 150.0d;
        }
        if (value.equals(context.getString(C0273R.string.cyan_value))) {
            return 180.0d;
        }
        if (value.equals(context.getString(C0273R.string.cobalt_value))) {
            return 210.0d;
        }
        if (value.equals(context.getString(C0273R.string.blue_value))) {
            return 240.0d;
        }
        if (value.equals(context.getString(C0273R.string.violet_value))) {
            return 270.0d;
        }
        if (value.equals(context.getString(C0273R.string.magenta_value))) {
            return 300.0d;
        }
        if (value.equals(context.getString(C0273R.string.crimson_value))) {
            return 330.0d;
        }
        return 0.0d;
    }

    public static String stringForColorNamePack(ColorNamePack colorNamePack, Context context) {
        if (colorNamePack == ColorNamePack.Common) {
            return context.getString(C0273R.string.common_display);
        }
        if (colorNamePack == ColorNamePack.Scientific) {
            return context.getString(C0273R.string.scientific_display);
        }
        if (colorNamePack == ColorNamePack.Colloquial) {
            return context.getString(C0273R.string.colloquial_display);
        }
        return context.getString(C0273R.string.empty_string);
    }

    public static String stringForCameraQuality(double cameraQuality, Context context) {
        if (cameraQuality >= ((double) context.getResources().getInteger(C0273R.integer.cameraquality_low_factor))) {
            return context.getString(C0273R.string.lowquality_display);
        }
        if (cameraQuality >= ((double) context.getResources().getInteger(C0273R.integer.cameraquality_med_factor))) {
            return context.getString(C0273R.string.medquality_display);
        }
        return context.getString(C0273R.string.highquality_display);
    }

    public static void rgbToHS(int redValue, int greenValue, int blueValue, float[] hs) {
        float s;
        float h;
        float h2;
        float r = ((float) redValue) / 255.0f;
        float g = ((float) greenValue) / 255.0f;
        float b = ((float) blueValue) / 255.0f;
        float max = Math.max(Math.max(r, g), b);
        float min = Math.min(Math.min(r, g), b);
        float l = (max + min) / 2.0f;
        if (max == min) {
            h2 = 0.0f;
            s = 0.0f;
        } else {
            double d = (double) (max - min);
            s = (float) (((double) l) > 0.5d ? d / ((double) ((2.0f - max) - min)) : d / ((double) (max + min)));
            if (max == r) {
                h = (float) (((double) (g < b ? 6 : 0)) + (((double) (g - b)) / d));
            } else if (max == g) {
                h = (float) ((((double) (b - r)) / d) + 2.0d);
            } else {
                h = (float) ((((double) (r - g)) / d) + 4.0d);
            }
            h2 = h / 6.0f;
        }
        hs[0] = h2 * 360.0f;
        hs[1] = s;
    }

    public static void rgbToHSL(int redValue, int greenValue, int blueValue, double[] hsl) {
        double s;
        double h;
        double h2;
        double r = ((double) redValue) / 255.0d;
        double g = ((double) greenValue) / 255.0d;
        double b = ((double) blueValue) / 255.0d;
        double max = Math.max(Math.max(r, g), b);
        double min = Math.min(Math.min(r, g), b);
        double d = (max + min) / 2.0d;
        double d2 = (max + min) / 2.0d;
        double l = (max + min) / 2.0d;
        if (max == min) {
            h2 = 0.0d;
            s = 0.0d;
        } else {
            double d3 = max - min;
            s = l > 0.5d ? d3 / ((2.0d - max) - min) : d3 / (max + min);
            if (max == r) {
                h = ((g - b) / d3) + ((double) (g < b ? 6 : 0));
            } else if (max == g) {
                h = ((b - r) / d3) + 2.0d;
            } else {
                h = ((r - g) / d3) + 4.0d;
            }
            h2 = h / 6.0d;
        }
        hsl[0] = h2 * 360.0d;
        hsl[1] = s;
        hsl[2] = l;
    }

    public static void rgbToHSL(int colorInt, double[] hsl) {
        rgbToHSL(Color.red(colorInt), Color.green(colorInt), Color.blue(colorInt), hsl);
    }

    public static boolean isColorBlindnessTypeSimulation(ColorBlindnessType colorBlindnessType) {
        return colorBlindnessType == ColorBlindnessType.ProtanopiaSimulate || colorBlindnessType == ColorBlindnessType.DeuteranopiaSimulate || colorBlindnessType == ColorBlindnessType.TritanopiaSimulate;
    }

    private static ColorBlindnessType colorBlindnessTypeNormal(ColorBlindnessType colorBlindnessType) {
        if (colorBlindnessType == ColorBlindnessType.ProtanopiaSimulate) {
            return ColorBlindnessType.Protanopia;
        }
        if (colorBlindnessType == ColorBlindnessType.DeuteranopiaSimulate) {
            return ColorBlindnessType.Deuteranopia;
        }
        if (colorBlindnessType == ColorBlindnessType.TritanopiaSimulate) {
            return ColorBlindnessType.Tritanopia;
        }
        return colorBlindnessType;
    }

    public static double clampTo255(double value) {
        if (value < 0.0d) {
            return 0.0d;
        }
        if (value > 255.0d) {
            return 255.0d;
        }
        return value;
    }

    public static double clampTo1(double value) {
        if (value < 0.0d) {
            return 0.0d;
        }
        if (value > 1.0d) {
            return 1.0d;
        }
        return value;
    }

    public static void daltonizeColor(double r, double g, double b, ColorBlindnessType colorBlindnessType, boolean simulateColorBlindness, int[] newRGB) {
        double cvd_a;
        double cvd_b;
        double cvd_c;
        double cvd_d;
        double cvd_e;
        double cvd_f;
        double cvd_g;
        double cvd_h;
        double cvd_i;
        if (simulateColorBlindness) {
            colorBlindnessType = colorBlindnessTypeNormal(colorBlindnessType);
        }
        if (colorBlindnessType == ColorBlindnessType.Normal) {
            cvd_a = 1.0d;
            cvd_b = 0.0d;
            cvd_c = 0.0d;
            cvd_d = 0.0d;
            cvd_e = 1.0d;
            cvd_f = 0.0d;
            cvd_g = 0.0d;
            cvd_h = 0.0d;
            cvd_i = 1.0d;
        } else if (colorBlindnessType == ColorBlindnessType.Protanopia) {
            cvd_a = 0.0d;
            cvd_b = 2.02344d;
            cvd_c = -2.52581d;
            cvd_d = 0.0d;
            cvd_e = 1.0d;
            cvd_f = 0.0d;
            cvd_g = 0.0d;
            cvd_h = 0.0d;
            cvd_i = 1.0d;
        } else if (colorBlindnessType == ColorBlindnessType.Deuteranopia) {
            cvd_a = 1.0d;
            cvd_b = 0.0d;
            cvd_c = 0.0d;
            cvd_d = 0.494207d;
            cvd_e = 0.0d;
            cvd_f = 1.24827d;
            cvd_g = 0.0d;
            cvd_h = 0.0d;
            cvd_i = 1.0d;
        } else if (colorBlindnessType == ColorBlindnessType.Tritanopia) {
            cvd_a = 1.0d;
            cvd_b = 0.0d;
            cvd_c = 0.0d;
            cvd_d = 0.0d;
            cvd_e = 1.0d;
            cvd_f = 0.0d;
            cvd_g = -0.395913d;
            cvd_h = 0.801109d;
            cvd_i = 0.0d;
        } else {
            cvd_a = 1.0d;
            cvd_b = 0.0d;
            cvd_c = 0.0d;
            cvd_d = 0.0d;
            cvd_e = 1.0d;
            cvd_f = 0.0d;
            cvd_g = 0.0d;
            cvd_h = 0.0d;
            cvd_i = 1.0d;
        }
        double L = (17.8824d * r) + (43.5161d * g) + (4.11935d * b);
        double M = (3.45565d * r) + (27.1554d * g) + (3.86714d * b);
        double S = (0.0299566d * r) + (0.184309d * g) + (1.46709d * b);
        double l = (cvd_a * L) + (cvd_b * M) + (cvd_c * S);
        double m = (cvd_d * L) + (cvd_e * M) + (cvd_f * S);
        double s = (cvd_g * L) + (cvd_h * M) + (cvd_i * S);
        double outputR = (0.0809444479d * l) + (-0.130504409d * m) + (0.116721066d * s);
        double outputG = (-0.0102485335d * l) + (0.0540193266d * m) + (-0.113614708d * s);
        double outputB = (-3.65296938E-4d * l) + (-0.00412161469d * m) + (0.693511405d * s);
        if (!simulateColorBlindness) {
            double outputR2 = r - outputR;
            double outputG2 = g - outputG;
            double outputB2 = b - outputB;
            double GG = (0.7d * outputR2) + (1.0d * outputG2) + (0.0d * outputB2);
            outputR = (0.0d * outputR2) + (0.0d * outputG2) + (0.0d * outputB2) + r;
            outputG = GG + g;
            outputB = (0.7d * outputR2) + (0.0d * outputG2) + (1.0d * outputB2) + b;
        }
        double outputR3 = clampTo255(outputR);
        double outputG3 = clampTo255(outputG);
        double outputB3 = clampTo255(outputB);
        newRGB[0] = (int) outputR3;
        newRGB[1] = (int) outputG3;
        newRGB[2] = (int) outputB3;
    }

    public static ColorMatrixColorFilter daltonizationFilter(ColorBlindnessType colorBlindnessType) {
        float[] transformationArray;
        if (colorBlindnessType == ColorBlindnessType.Normal) {
            transformationArray = new ColorMatrix().getArray();
        } else if (colorBlindnessType == ColorBlindnessType.Protanopia || colorBlindnessType == ColorBlindnessType.ProtanopiaSimulate) {
            transformationArray = correctionTransformation(new float[]{0.0f, 2.02344f, -2.52581f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}, isColorBlindnessTypeSimulation(colorBlindnessType));
        } else if (colorBlindnessType == ColorBlindnessType.Deuteranopia || colorBlindnessType == ColorBlindnessType.DeuteranopiaSimulate) {
            transformationArray = correctionTransformation(new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.494207f, 0.0f, 1.24827f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}, isColorBlindnessTypeSimulation(colorBlindnessType));
        } else if (colorBlindnessType == ColorBlindnessType.Tritanopia || colorBlindnessType == ColorBlindnessType.TritanopiaSimulate) {
            transformationArray = correctionTransformation(new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, -0.395913f, 0.801109f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}, isColorBlindnessTypeSimulation(colorBlindnessType));
        } else if (colorBlindnessType == ColorBlindnessType.ProtanomalySimulate) {
            transformationArray = new float[]{0.458f, 0.68f, -0.138f, 0.0f, 0.0f, 0.093f, 0.846f, 0.061f, 0.0f, 0.0f, -0.007f, -0.017f, 1.024f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
        } else if (colorBlindnessType == ColorBlindnessType.DeuteranomalySimulate) {
            transformationArray = new float[]{0.547f, 0.608f, -0.155f, 0.0f, 0.0f, 0.182f, 0.782f, 0.037f, 0.0f, 0.0f, -0.01f, 0.027f, 0.983f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
        } else {
            transformationArray = new ColorMatrix().getArray();
        }
        return new ColorMatrixColorFilter(new ColorMatrix(transformationArray));
    }

    private static float[] correctionTransformation(float[] toSimulated, boolean simulateOnly) {
        ColorMatrix rgbToLms = new ColorMatrix(new float[]{17.8824f, 43.5161f, 4.11935f, 0.0f, 0.0f, 3.45565f, 27.1554f, 3.86714f, 0.0f, 0.0f, 0.0299566f, 0.184309f, 1.46709f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
        ColorMatrix lmsToLmsSimulated = new ColorMatrix(toSimulated);
        ColorMatrix lmsColorBlind = new ColorMatrix();
        lmsColorBlind.setConcat(lmsToLmsSimulated, rgbToLms);
        ColorMatrix lmsToRgb = new ColorMatrix(new float[]{0.08094445f, -0.13050441f, 0.116721064f, 0.0f, 0.0f, -0.010248533f, 0.05401933f, -0.11361471f, 0.0f, 0.0f, -3.6529693E-4f, -0.0041216146f, 0.6935114f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
        ColorMatrix rgbColorBlind = new ColorMatrix();
        rgbColorBlind.setConcat(lmsToRgb, lmsColorBlind);
        if (simulateOnly) {
            return rgbColorBlind.getArray();
        }
        float[] rgbColorBlindArray = rgbColorBlind.getArray();
        float[] error = new float[20];
        float[] normal = new ColorMatrix().getArray();
        for (int i = 0; i < 20; i++) {
            error[i] = normal[i] - rgbColorBlindArray[i];
        }
        ColorMatrix errorMatrix = new ColorMatrix(error);
        ColorMatrix errorModification = new ColorMatrix(new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.7f, 1.0f, 0.0f, 0.0f, 0.0f, 0.7f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
        ColorMatrix modifiedErrorMatrix = new ColorMatrix();
        modifiedErrorMatrix.setConcat(errorModification, errorMatrix);
        float[] result = new float[20];
        for (int i2 = 0; i2 < 20; i2++) {
            result[i2] = normal[i2] + modifiedErrorMatrix.getArray()[i2];
        }
        return new ColorMatrix(result).getArray();
    }

    public static boolean isHueInRange(double hue, double minHue, double maxHue) {
        boolean isInHueRange;
        if (minHue < 0.0d || maxHue < 0.0d) {
            minHue += 360.0d;
            maxHue += 360.0d;
        }
        if (hue >= maxHue || hue < minHue) {
            isInHueRange = false;
        } else {
            isInHueRange = true;
        }
        if (minHue <= maxHue) {
            return isInHueRange;
        }
        if (hue >= minHue || hue < maxHue) {
            return true;
        }
        return false;
    }

    private static boolean isLightnessInRange(double lightness, double minLightness, double maxLightness) {
        return lightness >= minLightness && lightness < maxLightness;
    }

    private static boolean isSaturationInRange(double saturation, double minSaturation, double maxSaturation) {
        return saturation >= minSaturation && saturation < maxSaturation;
    }

    private static ArrayList<ColorRange> hueRangesArray(ColorNamePack colorNamePack) {
        if (colorNamePack == ColorNamePack.Common) {
            return hueRangesArrayCommon;
        }
        if (colorNamePack == ColorNamePack.Scientific) {
            return hueRangesArrayScientific;
        }
        return hueRangesArrayCommon;
    }

    private static ArrayList<ColorRange> lightnessRangesArray(ColorNamePack colorNamePack) {
        if (colorNamePack == ColorNamePack.Common) {
            return lightnessRangesArrayCommon;
        }
        if (colorNamePack == ColorNamePack.Scientific) {
            return lightnessRangesArrayScientific;
        }
        return null;
    }

    private static ArrayList<ColorRange> saturationRangesArray(ColorNamePack colorNamePack) {
        if (colorNamePack == ColorNamePack.Common) {
            return saturationRangesArrayCommon;
        }
        if (colorNamePack == ColorNamePack.Scientific) {
            return saturationRangesArrayScientific;
        }
        return null;
    }

    private static ArrayList<LightSatWord> lightnessSaturationWordsArray(ColorNamePack colorNamePack) {
        if (colorNamePack == ColorNamePack.Common) {
            return lightnessSaturationWordsArrayCommon;
        }
        return null;
    }

    private static ArrayList<ColorRange> hueLightnessArray(ColorNamePack colorNamePack) {
        if (colorNamePack == ColorNamePack.Common) {
            return hueLightnessArrayCommon;
        }
        return null;
    }

    private static ArrayList<CBPColor> colloquialColorNamesArray() {
        if (colloquialColorNamesArrayBacking == null) {
            ArrayList<CBPColor> colloquialColorNamesArrayBackingTemp = new ArrayList<>();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ColorBlindPalApp.getAppContext().getAssets().open("colloquial-color-names.txt")));
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    String[] nameAndHex = line.split(", #");
                    String colorName = nameAndHex[0];
                    double[] hsl = new double[3];
                    rgbToHSL(colorForHexString(nameAndHex[1]), hsl);
                    CBPColor color = CBPColor.cbpColorWithHSL(hsl[0], hsl[1], hsl[2]);
                    color.name = colorName;
                    colloquialColorNamesArrayBackingTemp.add(color);
                }
                colloquialColorNamesArrayBacking = colloquialColorNamesArrayBackingTemp;
            } catch (Exception e) {
                return null;
            }
        }
        return colloquialColorNamesArrayBacking;
    }

    private static int colorForHexString(String hexString) {
        return Color.rgb(Integer.parseInt(hexString.substring(0, 2), 16), Integer.parseInt(hexString.substring(2, 4), 16), Integer.parseInt(hexString.substring(4, 6), 16));
    }

    private static CBPColor closestColor(double hue, double saturation, double lightness, ArrayList<CBPColor> colorNamesArray) {
        CBPColor closestColor = null;
        double closestColorDistance = -1.0d;
        Iterator<CBPColor> it = colorNamesArray.iterator();
        while (it.hasNext()) {
            CBPColor candidateColor = it.next();
            double colorDistance = colorDistance(hue, saturation, lightness, candidateColor.f19h, candidateColor.f22s, candidateColor.f20l);
            if (colorDistance < closestColorDistance || closestColorDistance == -1.0d) {
                closestColorDistance = colorDistance;
                closestColor = candidateColor;
            }
        }
        return closestColor;
    }

    private static double colorDistance(double h1, double s1, double l1, double h2, double s2, double l2) {
        return ((Math.abs(h1 - h2) / 360.0d) * Math.sqrt(s1) * Math.sqrt(s2)) + Math.abs(s1 - s2) + Math.abs(l1 - l2);
    }

    public static String hueName(double hue, ColorNamePack colorNamePack) {
        if (Double.isNaN(hue)) {
            return "Gray";
        }
        Iterator<ColorRange> it = hueRangesArray(colorNamePack).iterator();
        while (it.hasNext()) {
            ColorRange colorRange = it.next();
            if (isHueInRange(hue, colorRange.minHue, colorRange.maxHue)) {
                return colorRange.name;
            }
        }
        return "Unknown";
    }

    private static String lightnessName(double lightness, ColorNamePack colorNamePack) {
        Iterator<ColorRange> it = lightnessRangesArray(colorNamePack).iterator();
        while (it.hasNext()) {
            ColorRange colorRange = it.next();
            if (isLightnessInRange(lightness, colorRange.minLightness, colorRange.maxLightness)) {
                return colorRange.name;
            }
        }
        return null;
    }

    private static String saturationName(double saturation, ColorNamePack colorNamePack) {
        Iterator<ColorRange> it = saturationRangesArray(colorNamePack).iterator();
        while (it.hasNext()) {
            ColorRange colorRange = it.next();
            if (isSaturationInRange(saturation, colorRange.minSaturation, colorRange.maxSaturation)) {
                return colorRange.name;
            }
        }
        return null;
    }

    private static String colorAdjective(String lightnessName, String saturationName, ColorNamePack colorNamePack) {
        ArrayList<LightSatWord> wordsArray = lightnessSaturationWordsArray(colorNamePack);
        if (wordsArray != null) {
            Iterator<LightSatWord> it = wordsArray.iterator();
            while (it.hasNext()) {
                LightSatWord lightSatWord = it.next();
                if (lightSatWord.lightness.equals(lightnessName) && lightSatWord.saturation.equals(saturationName)) {
                    return lightSatWord.word;
                }
            }
        }
        return String.format("%s, %s", new Object[]{lightnessName, saturationName});
    }

    private static String hueLightnessName(double hue, double lightness, ArrayList<ColorRange> colorRanges) {
        Iterator<ColorRange> it = colorRanges.iterator();
        while (it.hasNext()) {
            ColorRange colorRange = it.next();
            if (isLightnessInRange(lightness, colorRange.minLightness, colorRange.maxLightness)) {
                if (isHueInRange(hue, colorRange.minHue, colorRange.maxHue)) {
                    return colorRange.name;
                }
            }
        }
        return null;
    }

    public static String colorDescription(double HSVh, double HSVs, double HSVv, double HSLh, double HSLs, double HSLl, ColorNamePack colorNamePack) {
        double maxBlackL;
        String hueLightnessName;
        if (colorNamePack == ColorNamePack.Colloquial) {
            return closestColor(HSLh, HSLs, HSLl, colloquialColorNamesArray()).name;
        }
        double h = HSVh;
        double s = HSVs;
        double l = HSLl;
        if (colorNamePack == ColorNamePack.Common) {
            maxBlackL = 0.03d;
        } else if (colorNamePack == ColorNamePack.Scientific) {
            maxBlackL = 0.0d;
        } else {
            maxBlackL = 0.0d;
        }
        if (l <= maxBlackL) {
            return "Black";
        }
        if (l >= 1.0d) {
            return "White";
        }
        String lightnessName = lightnessName(l, colorNamePack);
        if (s > 0.01d) {
            String hueName = hueName(h, colorNamePack);
            String saturationName = saturationName(s, colorNamePack);
            ArrayList<ColorRange> hueLightnessArray = hueLightnessArray(colorNamePack);
            if (hueLightnessArray == null || (hueLightnessName = hueLightnessName(h, l, hueLightnessArray)) == null) {
                if (saturationName != null && lightnessName != null) {
                    return String.format("%s %s", new Object[]{colorAdjective(lightnessName, saturationName, colorNamePack), hueName});
                } else if (saturationName != null && lightnessName == null) {
                    return String.format("%s %s", new Object[]{saturationName, hueName});
                } else if (saturationName != null || lightnessName == null) {
                    return hueName;
                } else {
                    return String.format("%s %s", new Object[]{lightnessName, hueName});
                }
            } else if (saturationName == null) {
                return hueLightnessName;
            } else {
                return String.format("%s %s", new Object[]{saturationName, hueLightnessName});
            }
        } else if (lightnessName == null) {
            return "Gray";
        } else {
            return String.format("%s Gray", new Object[]{lightnessName});
        }
    }

    public static String colorDescription(int color, ColorNamePack colorNamePack) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        double[] hsl = new double[3];
        rgbToHSL(color, hsl);
        return colorDescription((double) hsv[0], (double) hsv[1], (double) hsv[2], hsl[0], hsl[1], hsl[2], colorNamePack);
    }

    public static double gaussian(double x, double y, double sigma) {
        return Math.exp(-(((x * x) + (y * y)) / ((2.0d * sigma) * sigma)));
    }

    public static Bitmap getBitmapFromNinepatch(int ninepatchId, int width, int height, Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), ninepatchId);
        NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(context.getResources(), bitmap, bitmap.getNinePatchChunk(), new Rect(), (String) null);
        ninePatchDrawable.setBounds(0, 0, width, height);
        Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        ninePatchDrawable.draw(new Canvas(outputBitmap));
        return outputBitmap;
    }

    public static boolean doesBitmapMatch(Bitmap bitmap, Bitmap referenceBitmap) {
        return bitmap != null && bitmap.getWidth() == referenceBitmap.getWidth() && bitmap.getHeight() == referenceBitmap.getHeight();
    }

    public static Bitmap makeBitmapToMatch(Bitmap referenceBitmap) {
        return Bitmap.createBitmap(referenceBitmap.getWidth(), referenceBitmap.getHeight(), referenceBitmap.getConfig());
    }

    public static int pxFromDp(Context context, double dp) {
        return (int) (((double) context.getResources().getDisplayMetrics().density) * dp);
    }

    public static void setTextShadow(Context context, TextView textView) {
        int shadowDimenPx = pxFromDp(context, (double) context.getResources().getInteger(C0273R.integer.shadow_dimen_dp));
        textView.setShadowLayer((float) shadowDimenPx, (float) shadowDimenPx, (float) shadowDimenPx, C0273R.color.black);
    }

    public static void clearTextShadow(TextView textView) {
        textView.setShadowLayer(0.0f, 0.0f, 0.0f, C0273R.color.transparent);
    }

    public static boolean isCurrentThreadUI() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
