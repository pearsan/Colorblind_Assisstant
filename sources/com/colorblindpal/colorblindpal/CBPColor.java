package com.colorblindpal.colorblindpal;

public class CBPColor {

    /* renamed from: b */
    public int f17b;

    /* renamed from: g */
    public int f18g;

    /* renamed from: h */
    public double f19h;

    /* renamed from: l */
    public double f20l;
    public String name;

    /* renamed from: r */
    public int f21r;

    /* renamed from: s */
    public double f22s;

    public static CBPColor cbpColorWithRGB(int red, int green, int blue) {
        CBPColor color = new CBPColor();
        color.f21r = red;
        color.f18g = green;
        color.f17b = blue;
        return color;
    }

    public static CBPColor cbpColorWithHSL(double hue, double saturation, double lightness) {
        CBPColor color = new CBPColor();
        color.f19h = hue;
        color.f22s = saturation;
        color.f20l = lightness;
        return color;
    }
}
