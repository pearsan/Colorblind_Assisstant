package com.colorblindpal.colorblindpal;

public class CameraFrameProcessOptions {
    private boolean fadeHighlight;
    private CBPColor highlightColor;
    private double maxHue;
    private double minHue;
    private double minSaturation;
    private boolean showStripe;
    private double stripeMaxHue;
    private double stripeMinHue;
    private double stripeTaperWidth;

    public CameraFrameProcessOptions(double minHue2, double maxHue2, double minSaturation2, CBPColor highlightColor2, boolean fadeHighlight2, boolean showStripe2, double stripeMinHue2, double stripeMaxHue2, double stripeTaperWidth2) {
        this.minHue = minHue2;
        this.maxHue = maxHue2;
        this.minSaturation = minSaturation2;
        this.highlightColor = highlightColor2;
        this.fadeHighlight = fadeHighlight2;
        this.showStripe = showStripe2;
        this.stripeMinHue = stripeMinHue2;
        this.stripeMaxHue = stripeMaxHue2;
        this.stripeTaperWidth = stripeTaperWidth2;
    }

    public boolean equals(CameraFrameProcessOptions other) {
        return other != null && this.minHue == other.minHue && this.maxHue == other.maxHue && this.minSaturation == other.minSaturation && this.highlightColor == other.highlightColor && this.fadeHighlight == other.fadeHighlight && this.showStripe == other.showStripe && this.stripeMinHue == other.stripeMinHue && this.stripeMaxHue == other.stripeMaxHue && this.stripeTaperWidth == other.stripeTaperWidth;
    }
}
