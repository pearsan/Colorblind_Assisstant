package com.colorblindpal.colorblindpal;

public class ColorRange {
    public double maxHue;
    public double maxLightness;
    public double maxSaturation;
    public double minHue;
    public double minLightness;
    public double minSaturation;
    public String name;

    public ColorRange(String name2) {
        this.name = name2;
    }

    public static ColorRange colorRangeWithHues(double minHue2, double maxHue2, String name2) {
        ColorRange colorRange = new ColorRange(name2);
        colorRange.minHue = minHue2;
        colorRange.maxHue = maxHue2;
        return colorRange;
    }

    public static ColorRange colorRangeWithLightness(double minLightness2, double maxLightness2, String name2) {
        ColorRange colorRange = new ColorRange(name2);
        colorRange.minLightness = minLightness2;
        colorRange.maxLightness = maxLightness2;
        return colorRange;
    }

    public static ColorRange colorRangeWithSaturation(double minSaturation2, double maxSaturation2, String name2) {
        ColorRange colorRange = new ColorRange(name2);
        colorRange.minSaturation = minSaturation2;
        colorRange.maxSaturation = maxSaturation2;
        return colorRange;
    }

    public static ColorRange colorRangeWithHueAndLightness(double minHue2, double maxHue2, double minLightness2, double maxLightness2, String name2) {
        ColorRange colorRange = new ColorRange(name2);
        colorRange.minHue = minHue2;
        colorRange.maxHue = maxHue2;
        colorRange.minLightness = minLightness2;
        colorRange.maxLightness = maxLightness2;
        return colorRange;
    }
}
