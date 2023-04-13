package com.colorblindpal.colorblindpal;

import android.graphics.Bitmap;
import android.graphics.Color;

public class ColorHelper {
    public static Bitmap processImage(Bitmap outBmp, Bitmap inputImage, double minHue, double maxHue, double minSaturation, CBPColor highlightColor, boolean fadeHighlight, boolean showStripe, double stripeMinHue, double stripeMaxHue, double stripeTaperWidth, int cameraQualityRatioForStripe, int stripeOffset) {
        double hueSimilarityFactor;
        if (!(minHue == 0.0d && maxHue == 360.0d)) {
            if (minHue < 0.0d) {
                minHue += 360.0d;
            }
            if (maxHue >= 360.0d) {
                maxHue -= 360.0d;
            }
        }
        double hueWidth = Math.min(Math.abs(maxHue - minHue), Math.abs((maxHue - minHue) + 360.0d));
        if (minHue > maxHue) {
            hueWidth = 360.0d - (minHue - maxHue);
        }
        double averageHue = (minHue + maxHue) / 2.0d;
        if (minHue > maxHue) {
            averageHue = (minHue + (maxHue + 360.0d)) / 2.0d;
            if (averageHue > 360.0d) {
                averageHue -= 360.0d;
            }
        }
        if (stripeMinHue < 0.0d) {
            stripeMinHue += 360.0d;
        }
        if (stripeMaxHue >= 360.0d) {
            stripeMaxHue -= 360.0d;
        }
        int stripeDistance = cameraQualityRatioForStripe * 12;
        int stripeWidth = cameraQualityRatioForStripe * 3;
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        float[] hs = new float[2];
        int[] pixels = new int[(width * height)];
        inputImage.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int posInStripe = ((col + row) + stripeOffset) % stripeDistance;
                if (!showStripe || posInStripe < stripeWidth) {
                    int pixel = pixels[(row * width) + col];
                    int r = (pixel >> 16) & 255;
                    int g = (pixel >> 8) & 255;
                    int b = pixel & 255;
                    Utilities.rgbToHS(r, g, b, hs);
                    float h = hs[0];
                    float s = hs[1];
                    if (showStripe) {
                        if (!Float.isNaN(h)) {
                            if (Utilities.isHueInRange((double) h, stripeMinHue, stripeMaxHue)) {
                                hueSimilarityFactor = 1.0d;
                            } else {
                                hueSimilarityFactor = Math.max(Math.max(1.0d - (Math.min(Math.min(Math.abs(((double) h) - stripeMinHue), Math.abs((((double) h) - stripeMinHue) + 360.0d)), Math.abs((((double) h) - stripeMinHue) - 360.0d)) / stripeTaperWidth), 1.0d - (Math.min(Math.min(Math.abs(((double) h) - stripeMaxHue), Math.abs((((double) h) - stripeMaxHue) + 360.0d)), Math.abs((((double) h) - stripeMinHue) - 360.0d)) / stripeTaperWidth)), 0.0d);
                            }
                            double hueSimilarityFactor2 = hueSimilarityFactor * Math.sqrt((double) s);
                            double stripeRed = 0.0d;
                            double stripeGreen = 0.0d;
                            double stripeBlue = 0.0d;
                            if (posInStripe >= stripeWidth / 3 && posInStripe < (stripeWidth * 2) / 3) {
                                stripeRed = 255.0d;
                                stripeGreen = 255.0d;
                                stripeBlue = 255.0d;
                            }
                            pixels[(row * width) + col] = Color.rgb((int) (((stripeRed - ((double) r)) * hueSimilarityFactor2) + ((double) r)), (int) (((stripeGreen - ((double) g)) * hueSimilarityFactor2) + ((double) g)), (int) (((stripeBlue - ((double) b)) * hueSimilarityFactor2) + ((double) b)));
                        }
                    } else if (Utilities.isHueInRange((double) h, minHue, maxHue)) {
                        double avg = (0.2126d * ((double) r)) + (0.7152d * ((double) g)) + (0.0722d * ((double) b));
                        double targetRed = (double) r;
                        double targetGreen = (double) g;
                        double targetBlue = (double) b;
                        if (highlightColor != null) {
                            targetRed = (double) highlightColor.f21r;
                            targetGreen = (double) highlightColor.f18g;
                            targetBlue = (double) highlightColor.f17b;
                        }
                        double blendFactor = 1.0d;
                        if (fadeHighlight) {
                            double distToAvgHue = Math.min(Math.abs(((double) h) - averageHue), Math.min(Math.abs((((double) h) - averageHue) - 360.0d), Math.abs((((double) h) - averageHue) + 360.0d)));
                            double hueSimilarityFactor3 = 0.0d;
                            if (distToAvgHue < hueWidth / 2.0d) {
                                hueSimilarityFactor3 = 1.0d - (distToAvgHue / (hueWidth / 2.0d));
                            }
                            blendFactor = ((double) s) * hueSimilarityFactor3;
                        } else if (((double) s) < minSaturation) {
                            blendFactor = 0.0d;
                        }
                        pixels[(row * width) + col] = Color.rgb((int) (((targetRed - avg) * blendFactor) + avg), (int) (((targetGreen - avg) * blendFactor) + avg), (int) (((targetBlue - avg) * blendFactor) + avg));
                    } else {
                        int avg2 = (int) ((0.2126d * ((double) r)) + (0.7152d * ((double) g)) + (0.0722d * ((double) b)));
                        pixels[(row * width) + col] = Color.rgb(avg2, avg2, avg2);
                    }
                }
            }
        }
        if (outBmp == null) {
            outBmp = Bitmap.createBitmap(inputImage.getWidth(), inputImage.getHeight(), inputImage.getConfig());
        }
        outBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return outBmp;
    }

    public static int colorAtCoordinate(byte[] yuv420sp, int width, int height, int x, int y, int size) {
        return colorAtCoordinate(yuv420sp, width, height, x, y, size, (Bitmap) null);
    }

    public static int colorAtCoordinate(Bitmap image, int x, int y, int size) {
        return colorAtCoordinate((byte[]) null, image.getWidth(), image.getHeight(), x, y, size, image);
    }

    private static int colorAtCoordinate(byte[] yuv420sp, int width, int height, int x, int y, int size, Bitmap image) {
        int pixelColor;
        double sigma = ((double) size) * 0.5d;
        double redVal = 0.0d;
        double greenVal = 0.0d;
        double blueVal = 0.0d;
        double totalGaussian = 0.0d;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int sampleX = (x - (size / 2)) + i;
                int sampleY = (y - (size / 2)) + j;
                if (sampleX >= 0 && sampleX < width && sampleY >= 0 && sampleY < height) {
                    if (image == null) {
                        pixelColor = getPixelColor(yuv420sp, width, height, sampleX, sampleY);
                    } else {
                        pixelColor = image.getPixel(sampleX, sampleY);
                    }
                    double gaussian = Utilities.gaussian((double) ((size / 2) - i), (double) ((size / 2) - j), sigma);
                    redVal += ((double) Color.red(pixelColor)) * gaussian;
                    greenVal += ((double) Color.green(pixelColor)) * gaussian;
                    blueVal += ((double) Color.blue(pixelColor)) * gaussian;
                    totalGaussian += gaussian;
                }
            }
        }
        return Color.rgb((int) (redVal / totalGaussian), (int) (greenVal / totalGaussian), (int) (blueVal / totalGaussian));
    }

    private static int getPixelColor(byte[] yuv420sp, int width, int height, int getX, int getY) {
        return convertYUVtoRGB(yuv420sp[(getY * width) + getX] & 255, (yuv420sp[(((width * height) + ((getX / 2) * 2)) + ((getY / 2) * width)) + 1] & 255) - 128, (yuv420sp[((width * height) + ((getX / 2) * 2)) + ((getY / 2) * width)] & 255) - 128);
    }

    private static int convertYUVtoRGB(int y, int u, int v) {
        int r = (int) (((float) y) + (1.13983f * ((float) v)));
        int g = (int) ((((float) y) - (0.39485f * ((float) u))) - (0.5806f * ((float) v)));
        int b = (int) (((float) y) + (2.03211f * ((float) u)));
        if (r > 255) {
            r = 255;
        } else if (r < 0) {
            r = 0;
        }
        if (g > 255) {
            g = 255;
        } else if (g < 0) {
            g = 0;
        }
        if (b > 255) {
            b = 255;
        } else if (b < 0) {
            b = 0;
        }
        return -16777216 | (r << 16) | (g << 8) | b;
    }
}
