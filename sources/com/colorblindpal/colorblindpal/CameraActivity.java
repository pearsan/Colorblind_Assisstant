package com.colorblindpal.colorblindpal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Camera;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class CameraActivity extends Activity implements FrameProcessor {
    /* access modifiers changed from: private */
    public CameraToggleButton actualColorsToggleButton;
    private TextView blueTextView;
    private View bottomBlackBarColorSelectorView;
    private View bottomBlackBarColorWindowView;
    private boolean canGetBmpFromBackground;
    /* access modifiers changed from: private */
    public CBPCameraPreview cbpCameraPreview;
    private TextView colorDescriptionTextView;
    /* access modifiers changed from: private */
    public ColorMode colorMode;
    private Button colorModeButton;
    private RelativeLayout colorSelectorContainerRelativeLayout;
    private CameraToggleButton colorSelectorToolsToggleButton;
    private RelativeLayout colorWindowContainerRelativeLayout;
    private boolean colorWindowToolsButtonSelected = false;
    private int colorWindowToolsButtonVisibility = 0;
    private CameraToggleButton colorWindowToolsToggleButton;
    private final double crosshairDefaultScreenCoordinatePortionX = 0.5d;
    private final double crosshairDefaultScreenCoordinatePortionY = 0.5d;
    /* access modifiers changed from: private */
    public ImageView crosshairImageView;
    /* access modifiers changed from: private */
    public double crosshairScreenCoordinatePortionX = 0.5d;
    /* access modifiers changed from: private */
    public double crosshairScreenCoordinatePortionY = 0.5d;
    /* access modifiers changed from: private */
    public CameraToggleButton customHighlightColorToggleButton;
    private RelativeLayout devColorsRelativeLayout;
    private DisplayManager.DisplayListener displayListener;
    /* access modifiers changed from: private */
    public CameraToggleButton fadeHighlightToggleButton;
    /* access modifiers changed from: private */
    public CameraToggleButton freezeCameraButton;
    private TextView greenTextView;
    private TextView hTextView;
    private boolean hasCheckedCanGetBmpFromBg = false;
    private boolean hasUpdatedCrosshair;
    /* access modifiers changed from: private */
    public CameraToggleImageButton hideButton;
    private TextView hueRangeTextView;
    private SeekBar hueSeekBar;
    private View hueSelectorBarView;
    /* access modifiers changed from: private */
    public SeekBar hueSelectorSeekBar;
    /* access modifiers changed from: private */
    public SeekBar hueWidthSelectorSeekBar;
    /* access modifiers changed from: private */
    public Button infoButton;
    /* access modifiers changed from: private */
    public Bitmap inputImageBitmap;
    /* access modifiers changed from: private */
    public Bitmap lastPreviewImageBitmap;
    /* access modifiers changed from: private */
    public int lastUsedCameraQualityFactor = 1;
    /* access modifiers changed from: private */
    public CameraFrameProcessOptions lastUsedOptions;
    /* access modifiers changed from: private */
    public Camera mCamera;
    private final int maxFramesProcessing = 1;
    /* access modifiers changed from: private */
    public boolean needsToSaveThisFreeze = false;
    private int numFramesProcessing = 0;
    /* access modifiers changed from: private */
    public ImageView outputImageView;
    /* access modifiers changed from: private */
    public boolean previewFrozen = false;
    /* access modifiers changed from: private */
    public ImageView previewImageView;
    /* access modifiers changed from: private */
    public Bitmap previewInputImageBitmap;
    private ImageView previewInputImageView;
    /* access modifiers changed from: private */
    public TextureView previewTextureView;
    /* access modifiers changed from: private */
    public Bitmap previewTextureViewTmpBmp;
    /* access modifiers changed from: private */
    public Bitmap processImageOutputBitmap;
    private boolean queuedUpFrame = false;
    private TextView redTextView;
    private TextView sTextView;
    private SeekBar saturationSeekBar;
    private TextView saturationTextView;
    private LinearLayout secondRowColorSelectorLinearLayout;
    private LinearLayout secondRowColorWindowLinearLayout;
    /* access modifiers changed from: private */
    public double selectColorScreenCoordinatePortionX = -1.0d;
    /* access modifiers changed from: private */
    public double selectColorScreenCoordinatePortionY = -1.0d;
    /* access modifiers changed from: private */
    public CameraToggleButton selectColorToggleButton;
    private View selectedColorBoxView;
    /* access modifiers changed from: private */
    public CameraToggleButton shiftSpectrumToggleButton;
    /* access modifiers changed from: private */
    public CameraToggleButton showFilterToggleButton;
    /* access modifiers changed from: private */
    public boolean showImageViewNext = false;
    /* access modifiers changed from: private */
    public CameraToggleButton showStripeToggleButton;
    /* access modifiers changed from: private */
    public boolean shuttingDownCamera = false;
    private View topBlackBarColorSelectorView;
    private View topBlackBarColorWindowView;
    private TextView vTextView;
    private SeekBar valueSeekBar;
    private TextView valueTextView;
    /* access modifiers changed from: private */
    public boolean waitToProcess = false;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, C0273R.xml.preferences, false);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        getWindow().addFlags(128);
        setContentView(C0273R.layout.activity_camera);
        storeUIFields();
        setColorSelectorToolsButtonListener();
        setColorWindowToolsButtonListener();
        setColorModeButtonListener();
        setHueWidthSelectorSeekBarListener();
        setHueSelectorSeekBarListener();
        setCustomHighlightColorButtonListener();
        setFadeHighlightButtonListener();
        setFreezeButtonListener();
        setGlobalFilterButtonsListeners();
        setHideButtonListener();
        setInfoButtonListener();
        setPreviewTextureViewTouchListener();
        setPreviewImageViewLayoutListener();
        setCrosshairImageViewLayoutListener();
        this.hueSeekBar.setEnabled(false);
        ViewGroup.MarginLayoutParams hueSeekBarNeighborLP = (ViewGroup.MarginLayoutParams) this.selectedColorBoxView.getLayoutParams();
        this.hueSeekBar.setPadding(hueSeekBarNeighborLP.leftMargin, 0, hueSeekBarNeighborLP.rightMargin, 0);
        ViewGroup.MarginLayoutParams hueSelectorSeekBarNeighborLP = (ViewGroup.MarginLayoutParams) this.previewImageView.getLayoutParams();
        this.hueSelectorSeekBar.setPadding(hueSelectorSeekBarNeighborLP.leftMargin, 0, hueSelectorSeekBarNeighborLP.rightMargin, 0);
        setSeekBarThumbTint(this.hueSeekBar, getResources().getColor(C0273R.color.seekbar_thumb_light));
        disableSeekBar(this.saturationSeekBar);
        disableSeekBar(this.valueSeekBar);
        this.hueWidthSelectorSeekBar.setProgress(60);
        this.colorDescriptionTextView.setText(getString(C0273R.string.empty_string));
        this.selectColorToggleButton.setAllText(getString(C0273R.string.empty_string));
        this.hideButton.uncheckedDrawable = C0273R.C0274drawable.hide_button;
        this.hideButton.checkedDrawable = C0273R.C0274drawable.hide_button_selected;
        Utilities.setTextShadow(this, this.colorModeButton);
        Utilities.setTextShadow(this, this.hueRangeTextView);
        Utilities.setTextShadow(this, this.saturationTextView);
        Utilities.setTextShadow(this, this.valueTextView);
        Utilities.setTextShadow(this, this.colorDescriptionTextView);
        updateViews();
        this.colorMode = ColorMode.ColorSelector;
        updateColorMode();
        this.shiftSpectrumToggleButton.callOnClick();
        setInitialPreviewInputImageBitmap();
        showTutorialIfAppropriate();
    }

    /* access modifiers changed from: private */
    public void updateCrosshairImage() {
        final int x = (int) (this.crosshairScreenCoordinatePortionX * ((double) this.previewTextureView.getWidth()));
        final int y = (int) (this.crosshairScreenCoordinatePortionY * ((double) this.previewTextureView.getHeight()));
        runOnUiThread(new Runnable() {
            public void run() {
                CameraActivity.this.crosshairImageView.setX((float) (x - (CameraActivity.this.crosshairImageView.getWidth() / 2)));
                CameraActivity.this.crosshairImageView.setY((float) (y - (CameraActivity.this.crosshairImageView.getHeight() / 2)));
            }
        });
    }

    private void storeUIFields() {
        this.previewTextureView = (TextureView) findViewById(C0273R.C0275id.camera_textureview_preview);
        this.secondRowColorSelectorLinearLayout = (LinearLayout) findViewById(C0273R.C0275id.camera_linearlayout_secondrowcolorselector);
        this.secondRowColorWindowLinearLayout = (LinearLayout) findViewById(C0273R.C0275id.camera_linearlayout_secondrowcolorwindow);
        this.bottomBlackBarColorSelectorView = findViewById(C0273R.C0275id.camera_view_bottomblackbarcolorselector);
        this.bottomBlackBarColorWindowView = findViewById(C0273R.C0275id.camera_view_bottomblackbarcolorwindow);
        this.topBlackBarColorSelectorView = findViewById(C0273R.C0275id.camera_view_topblackbarcolorselector);
        this.topBlackBarColorWindowView = findViewById(C0273R.C0275id.camera_view_topblackbarcolorwindow);
        this.hueSeekBar = (SeekBar) findViewById(C0273R.C0275id.camera_seekbar_hue);
        this.saturationSeekBar = (SeekBar) findViewById(C0273R.C0275id.camera_seekbar_saturation);
        this.valueSeekBar = (SeekBar) findViewById(C0273R.C0275id.camera_seekbar_value);
        this.hueWidthSelectorSeekBar = (SeekBar) findViewById(C0273R.C0275id.camera_seekbar_huewidthselector);
        this.hueSelectorSeekBar = (SeekBar) findViewById(C0273R.C0275id.camera_seekbar_hueselector);
        this.hueSelectorBarView = findViewById(C0273R.C0275id.camera_view_hueselectorbar);
        this.selectColorToggleButton = (CameraToggleButton) findViewById(C0273R.C0275id.camera_button_selectcolor);
        this.hueWidthSelectorSeekBar = (SeekBar) findViewById(C0273R.C0275id.camera_seekbar_huewidthselector);
        this.customHighlightColorToggleButton = (CameraToggleButton) findViewById(C0273R.C0275id.camera_togglebutton_customhighlightcolor);
        this.fadeHighlightToggleButton = (CameraToggleButton) findViewById(C0273R.C0275id.camera_togglebutton_fadehighlightbutton);
        this.shiftSpectrumToggleButton = (CameraToggleButton) findViewById(C0273R.C0275id.camera_togglebutton_shiftspectrum);
        this.showStripeToggleButton = (CameraToggleButton) findViewById(C0273R.C0275id.camera_togglebutton_showstripes);
        this.actualColorsToggleButton = (CameraToggleButton) findViewById(C0273R.C0275id.camera_togglebutton_actualcolors);
        this.showFilterToggleButton = (CameraToggleButton) findViewById(C0273R.C0275id.camera_togglebutton_showfilter);
        this.colorDescriptionTextView = (TextView) findViewById(C0273R.C0275id.camera_textview_colordescription);
        this.selectedColorBoxView = findViewById(C0273R.C0275id.camera_view_selectedcolorbox);
        this.redTextView = (TextView) findViewById(C0273R.C0275id.camera_textview_red);
        this.greenTextView = (TextView) findViewById(C0273R.C0275id.camera_textview_green);
        this.blueTextView = (TextView) findViewById(C0273R.C0275id.camera_textview_blue);
        this.hTextView = (TextView) findViewById(C0273R.C0275id.camera_textview_h);
        this.sTextView = (TextView) findViewById(C0273R.C0275id.camera_textview_s);
        this.vTextView = (TextView) findViewById(C0273R.C0275id.camera_textview_v);
        this.crosshairImageView = (ImageView) findViewById(C0273R.C0275id.camera_imageview_crosshair);
        this.colorSelectorToolsToggleButton = (CameraToggleButton) findViewById(C0273R.C0275id.camera_togglebutton_colorselectortools);
        this.colorWindowToolsToggleButton = (CameraToggleButton) findViewById(C0273R.C0275id.camera_togglebutton_colorwindowstools);
        this.saturationTextView = (TextView) findViewById(C0273R.C0275id.camera_textview_saturation);
        this.valueTextView = (TextView) findViewById(C0273R.C0275id.camera_textview_value);
        this.devColorsRelativeLayout = (RelativeLayout) findViewById(C0273R.C0275id.camera_relativelayout_devcolors);
        this.colorModeButton = (Button) findViewById(C0273R.C0275id.camera_button_colormode);
        this.colorWindowContainerRelativeLayout = (RelativeLayout) findViewById(C0273R.C0275id.camera_relativelayout_colorwindowcontainer);
        this.colorSelectorContainerRelativeLayout = (RelativeLayout) findViewById(C0273R.C0275id.camera_relativelayout_colorselectorcontainer);
        this.hideButton = (CameraToggleImageButton) findViewById(C0273R.C0275id.camera_button_hide);
        this.freezeCameraButton = (CameraToggleButton) findViewById(C0273R.C0275id.camera_button_freezecamera);
        this.infoButton = (Button) findViewById(C0273R.C0275id.camera_button_info);
        this.hueRangeTextView = (TextView) findViewById(C0273R.C0275id.camera_textview_huerange);
        this.previewImageView = (ImageView) findViewById(C0273R.C0275id.camera_imageview_preview);
        this.previewInputImageView = (ImageView) findViewById(C0273R.C0275id.camera_imageview_previewinput);
        this.outputImageView = (ImageView) findViewById(C0273R.C0275id.camera_imageview_output);
    }

    private void showTutorialIfAppropriate() {
        ColorSingleton colorSingleton = ColorSingleton.getInstance();
        if (colorSingleton.getLastTutorialTime() == 0) {
            colorSingleton.saveLastTutorialTime(System.currentTimeMillis());
            new AlertDialog.Builder(this).setTitle(getString(C0273R.string.tutorial_title)).setMessage(getString(C0273R.string.tutorial_message)).setPositiveButton(C0273R.string.tutorial_positivebutton, (DialogInterface.OnClickListener) null).setIcon(C0273R.mipmap.ic_launcher).show();
        }
    }

    private void setInitialPreviewInputImageBitmap() {
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        RelativeLayout.LayoutParams previewInputImageViewLP = (RelativeLayout.LayoutParams) this.previewInputImageView.getLayoutParams();
        this.previewInputImageBitmap = Utilities.getBitmapFromNinepatch(C0273R.C0274drawable.rainbow, displaySize.x - (previewInputImageViewLP.leftMargin + previewInputImageViewLP.rightMargin), previewInputImageViewLP.height, getBaseContext());
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.shuttingDownCamera = false;
        this.hasUpdatedCrosshair = false;
        setUpCamera();
        updateColorSelectorTools();
        resetColorSelectorCrosshair();
        setUpDisplayListener();
        if (this.colorMode == ColorMode.ColorWindow && this.shiftSpectrumToggleButton.isChecked()) {
            updateDaltonizationType(ColorSingleton.getInstance().getColorBlindnessType());
        }
    }

    private void setUpCamera() {
        this.mCamera = getCameraInstance();
        this.cbpCameraPreview = new CBPCameraPreview(this, this.mCamera, this.previewTextureView, this, new Camera.PreviewCallback() {
            public void onPreviewFrame(byte[] data, Camera cam) {
                CameraActivity.this.processFrameColorSelector(data, cam, false);
                CameraActivity.this.cbpCameraPreview.previewCallbackCompleted();
            }
        });
        this.previewTextureView.setVisibility(0);
    }

    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
        }
        if (camera == null) {
            new AlertDialog.Builder(this).setTitle(getString(C0273R.string.no_camera_error_title)).setMessage(getString(C0273R.string.no_camera_error_message)).setPositiveButton(getString(C0273R.string.no_camera_error_positivebutton), (DialogInterface.OnClickListener) null).setIcon(C0273R.mipmap.ic_launcher).setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    CameraActivity.this.finishAffinity();
                }
            }).show();
        }
        return camera;
    }

    /* access modifiers changed from: private */
    public void resetColorSelectorCrosshair() {
        this.crosshairScreenCoordinatePortionX = 0.5d;
        this.crosshairScreenCoordinatePortionY = 0.5d;
        updateCrosshairImage();
    }

    private void disableSeekBar(SeekBar seekBar) {
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    private void setUpDisplayListener() {
        this.displayListener = new DisplayManager.DisplayListener() {
            public void onDisplayAdded(int displayId) {
            }

            public void onDisplayChanged(int displayId) {
                if (CameraActivity.this.cbpCameraPreview != null) {
                    CameraActivity.this.cbpCameraPreview.displayChanged();
                }
            }

            public void onDisplayRemoved(int displayId) {
            }
        };
        ((DisplayManager) getSystemService("display")).registerDisplayListener(this.displayListener, (Handler) null);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (this.previewFrozen) {
            this.previewFrozen = false;
        }
        stopDisplayListener();
        releaseCamera();
    }

    private void stopDisplayListener() {
        ((DisplayManager) getSystemService("display")).unregisterDisplayListener(this.displayListener);
    }

    private void releaseCamera() {
        if (this.mCamera != null) {
            this.mCamera.setPreviewCallback((Camera.PreviewCallback) null);
            this.mCamera.release();
            this.mCamera = null;
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.previewTextureView.setVisibility(8);
        this.outputImageView.setImageResource(C0273R.color.black);
        this.shuttingDownCamera = true;
        this.freezeCameraButton.setChecked(this.previewFrozen);
    }

    /* access modifiers changed from: private */
    public void processFrameColorSelector(byte[] frameData, Camera cam, boolean override) {
        processFrameColorSelector(frameData, cam, override, ColorSelectorCallback.SetColorSelector);
    }

    /* access modifiers changed from: private */
    public void processFrameColorSelector(final byte[] frameData, Camera cam, boolean override, final ColorSelectorCallback callbackType) {
        if (!this.hasUpdatedCrosshair) {
            updateCrosshairImage();
            this.hasUpdatedCrosshair = true;
        }
        if (this.colorMode == ColorMode.ColorSelector || override) {
            final Camera.Size previewSize = cam.getParameters().getPreviewSize();
            new Thread(new Runnable() {
                public void run() {
                    final int color;
                    int sampleSize = ColorSingleton.getInstance().getColorSelectorSampleSize();
                    double portionX = CameraActivity.this.crosshairScreenCoordinatePortionX;
                    double portionY = CameraActivity.this.crosshairScreenCoordinatePortionY;
                    if (callbackType == ColorSelectorCallback.UpdateHueSelector) {
                        portionX = CameraActivity.this.selectColorScreenCoordinatePortionX;
                        portionY = CameraActivity.this.selectColorScreenCoordinatePortionY;
                    }
                    if (frameData != null) {
                        Point selectorCoordinate = CameraActivity.this.cbpCameraPreview.getFrameDataCoordinate(previewSize.width, previewSize.height, portionX, portionY);
                        color = ColorHelper.colorAtCoordinate(frameData, previewSize.width, previewSize.height, selectorCoordinate.x, selectorCoordinate.y, sampleSize);
                    } else if (CameraActivity.this.inputImageBitmap != null) {
                        color = ColorHelper.colorAtCoordinate(CameraActivity.this.inputImageBitmap, (int) (((double) CameraActivity.this.inputImageBitmap.getWidth()) * portionX), (int) (((double) CameraActivity.this.inputImageBitmap.getHeight()) * portionY), sampleSize);
                    } else {
                        return;
                    }
                    CameraActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            if (callbackType == ColorSelectorCallback.SetColorSelector) {
                                CameraActivity.this.setColorSelector(color);
                            } else if (callbackType == ColorSelectorCallback.UpdateHueSelector) {
                                CameraActivity.this.updateHueSelectorCallback(color);
                            }
                        }
                    });
                }
            }).start();
        }
    }

    public void processFrame() {
        processFrame(false, false);
    }

    private void processFrame(boolean shouldForceProcess, boolean calledManually) {
        if (this.colorMode == ColorMode.ColorSelector) {
            if (this.previewFrozen && this.needsToSaveThisFreeze && !calledManually && !shouldForceProcess) {
                saveImageFromTextureView();
            }
        } else if (!this.previewFrozen || !this.needsToSaveThisFreeze) {
            final boolean forceProcess = shouldForceProcess || (this.previewFrozen && !calledManually);
            if (this.numFramesProcessing >= 1) {
                if (forceProcess || calledManually) {
                    this.queuedUpFrame = true;
                }
            } else if (this.colorMode == ColorMode.ColorWindow) {
                this.numFramesProcessing++;
                final boolean isFirstFrameAfterWaitingToProcess = this.waitToProcess;
                final boolean startedProcessingWithPreviewFrozen = this.previewFrozen;
                final boolean z = calledManually;
                new Thread(new Runnable() {
                    public void run() {
                        int cameraQualityFactor;
                        Bitmap inputImage;
                        final Bitmap previewOutputImage;
                        if (CameraActivity.this.previewFrozen && !z) {
                            CameraActivity.this.saveImageFromTextureView();
                        }
                        if (CameraActivity.this.actualColorsToggleButton.isChecked() || CameraActivity.this.shiftSpectrumToggleButton.isChecked()) {
                            CameraActivity.this.processFrameFinish();
                            return;
                        }
                        if (!z && (!CameraActivity.this.previewFrozen || forceProcess)) {
                            inputImage = CameraActivity.this.getPreviewTextureViewBmp();
                            cameraQualityFactor = ColorSingleton.getInstance().getCameraQuality();
                        } else if (CameraActivity.this.inputImageBitmap != null) {
                            if (CameraActivity.this.inputImageBitmap.getWidth() != CameraActivity.this.previewTextureView.getWidth() / CameraActivity.this.lastUsedCameraQualityFactor) {
                                int freezeQualityFactor = ColorSingleton.getInstance().getCameraFreezeQuality();
                                inputImage = CameraActivity.this.getPreviewTextureViewBmp(false, freezeQualityFactor);
                                cameraQualityFactor = freezeQualityFactor;
                            } else {
                                inputImage = CameraActivity.this.inputImageBitmap;
                                cameraQualityFactor = CameraActivity.this.lastUsedCameraQualityFactor;
                            }
                        } else {
                            inputImage = CameraActivity.this.getPreviewTextureViewBmp();
                            cameraQualityFactor = ColorSingleton.getInstance().getCameraQuality();
                        }
                        ColorSingleton colorSingleton = ColorSingleton.getInstance();
                        double hue = (double) CameraActivity.this.hueSelectorSeekBar.getProgress();
                        double hueWidth = (double) CameraActivity.this.hueWidthSelectorSeekBar.getProgress();
                        double minSaturation = colorSingleton.getMinimumSaturation();
                        boolean fadeHighlight = CameraActivity.this.fadeHighlightToggleButton.isChecked();
                        double minHue = hue - (hueWidth / 2.0d);
                        double maxHue = hue + (hueWidth / 2.0d);
                        if (hueWidth == 360.0d) {
                            maxHue = 360.0d;
                            minHue = 0.0d;
                        }
                        boolean showStripe = CameraActivity.this.showStripeToggleButton.isChecked();
                        double stripeMinHue = 0.0d;
                        double stripeMaxHue = 0.0d;
                        if (showStripe) {
                            double h = colorSingleton.getStripeHue();
                            stripeMinHue = h;
                            stripeMaxHue = h;
                        }
                        if (showStripe) {
                            minHue = 0.0d;
                            maxHue = 360.0d;
                            minSaturation = 0.0d;
                        }
                        CBPColor highlightColor = null;
                        if (CameraActivity.this.customHighlightColorToggleButton.isChecked()) {
                            highlightColor = colorSingleton.getHighlightColor();
                        }
                        CameraFrameProcessOptions currentOptions = new CameraFrameProcessOptions(minHue, maxHue, minSaturation, highlightColor, fadeHighlight, showStripe, stripeMinHue, stripeMaxHue, 90.0d);
                        boolean optionsSameAsLastTime = currentOptions.equals(CameraActivity.this.lastUsedOptions);
                        CameraFrameProcessOptions unused = CameraActivity.this.lastUsedOptions = currentOptions;
                        int cameraQualityRatio = CameraActivity.this.getResources().getInteger(C0273R.integer.cameraquality_low_factor) / cameraQualityFactor;
                        if (!Utilities.doesBitmapMatch(CameraActivity.this.processImageOutputBitmap, inputImage)) {
                            Bitmap unused2 = CameraActivity.this.processImageOutputBitmap = Utilities.makeBitmapToMatch(inputImage);
                        }
                        final Bitmap outputImage = ColorHelper.processImage(CameraActivity.this.processImageOutputBitmap, inputImage, minHue, maxHue, minSaturation, highlightColor, fadeHighlight, showStripe, stripeMinHue, stripeMaxHue, 90.0d, cameraQualityRatio, 0);
                        if (!optionsSameAsLastTime || CameraActivity.this.lastPreviewImageBitmap == null) {
                            int previewQualityRatio = CameraActivity.this.getResources().getInteger(C0273R.integer.cameraquality_low_factor);
                            int[] previewImageViewLoc = new int[2];
                            CameraActivity.this.previewImageView.getLocationOnScreen(previewImageViewLoc);
                            int previewStripeOffset = previewImageViewLoc[0] + previewImageViewLoc[1];
                            if (!Utilities.doesBitmapMatch(CameraActivity.this.lastPreviewImageBitmap, CameraActivity.this.previewInputImageBitmap)) {
                                Bitmap unused3 = CameraActivity.this.lastPreviewImageBitmap = Utilities.makeBitmapToMatch(CameraActivity.this.previewInputImageBitmap);
                            }
                            previewOutputImage = ColorHelper.processImage(CameraActivity.this.lastPreviewImageBitmap, CameraActivity.this.previewInputImageBitmap, minHue, maxHue, minSaturation, highlightColor, fadeHighlight, showStripe, stripeMinHue, stripeMaxHue, 90.0d, previewQualityRatio, previewStripeOffset);
                            Bitmap unused4 = CameraActivity.this.lastPreviewImageBitmap = previewOutputImage;
                        } else {
                            previewOutputImage = CameraActivity.this.lastPreviewImageBitmap;
                        }
                        final int i = cameraQualityFactor;
                        final Bitmap bitmap = inputImage;
                        CameraActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                if (CameraActivity.this.actualColorsToggleButton.isChecked() || CameraActivity.this.shiftSpectrumToggleButton.isChecked()) {
                                    CameraActivity.this.processFrameFinish();
                                    return;
                                }
                                if (isFirstFrameAfterWaitingToProcess) {
                                    boolean unused = CameraActivity.this.waitToProcess = false;
                                }
                                if (CameraActivity.this.waitToProcess || CameraActivity.this.shuttingDownCamera || (!startedProcessingWithPreviewFrozen && CameraActivity.this.previewFrozen)) {
                                    CameraActivity.this.processFrameFinish();
                                    return;
                                }
                                CameraActivity.this.outputImageView.setImageBitmap(outputImage);
                                CameraActivity.this.previewImageView.setImageBitmap(previewOutputImage);
                                CameraActivity.this.previewImageView.setColorFilter(Utilities.daltonizationFilter(ColorBlindnessType.Normal));
                                if (CameraActivity.this.showImageViewNext && CameraActivity.this.colorMode == ColorMode.ColorWindow) {
                                    CameraActivity.this.outputImageView.setVisibility(0);
                                    boolean unused2 = CameraActivity.this.showImageViewNext = false;
                                }
                                int unused3 = CameraActivity.this.lastUsedCameraQualityFactor = i;
                                Bitmap unused4 = CameraActivity.this.inputImageBitmap = bitmap;
                                CameraActivity.this.processFrameFinish();
                            }
                        });
                    }
                }).start();
            }
        }
    }

    /* access modifiers changed from: private */
    public Bitmap getPreviewTextureViewBmp() {
        return getPreviewTextureViewBmp(false, ColorSingleton.getInstance().getCameraQuality());
    }

    /* access modifiers changed from: private */
    public Bitmap getPreviewTextureViewBmp(boolean returnDirectly, int bmpPreviewRatio) {
        checkCanGetBmpFromBg();
        if ((!this.hasCheckedCanGetBmpFromBg || !this.canGetBmpFromBackground) && !Utilities.isCurrentThreadUI()) {
            return getPreviewTextureViewBmpOnUI(returnDirectly, bmpPreviewRatio);
        }
        return getPreviewTextureViewBmpWork(returnDirectly, bmpPreviewRatio);
    }

    /* access modifiers changed from: private */
    public Bitmap getPreviewTextureViewBmpWork(boolean returnDirectly, int bmpPreviewRatio) {
        int newWidth = this.previewTextureView.getWidth() / bmpPreviewRatio;
        int newHeight = this.previewTextureView.getHeight() / bmpPreviewRatio;
        if (returnDirectly) {
            return this.previewTextureView.getBitmap(newWidth, newHeight);
        }
        if (this.inputImageBitmap != null && this.inputImageBitmap.getWidth() == newWidth && this.inputImageBitmap.getHeight() == newHeight) {
            this.previewTextureView.getBitmap(this.inputImageBitmap);
        } else {
            this.inputImageBitmap = this.previewTextureView.getBitmap(newWidth, newHeight);
        }
        return this.inputImageBitmap;
    }

    private Bitmap getPreviewTextureViewBmpOnUI(final boolean returnDirectly, final int bmpPreviewRatio) {
        try {
            final AtomicBoolean runnableDone = new AtomicBoolean(false);
            Runnable bitmapRunnable = new Runnable() {
                public void run() {
                    synchronized (this) {
                        Bitmap unused = CameraActivity.this.previewTextureViewTmpBmp = CameraActivity.this.getPreviewTextureViewBmpWork(returnDirectly, bmpPreviewRatio);
                        runnableDone.set(true);
                        notify();
                    }
                }
            };
            runOnUiThread(bitmapRunnable);
            synchronized (bitmapRunnable) {
                while (!runnableDone.get()) {
                    bitmapRunnable.wait();
                }
            }
        } catch (InterruptedException e) {
        }
        return this.previewTextureViewTmpBmp;
    }

    private void checkCanGetBmpFromBg() {
        if (!this.hasCheckedCanGetBmpFromBg && !Utilities.isCurrentThreadUI() && this.previewTextureView != null) {
            try {
                this.previewTextureView.getBitmap(1, 1);
                this.canGetBmpFromBackground = true;
            } catch (Exception e) {
                this.canGetBmpFromBackground = false;
            }
            this.hasCheckedCanGetBmpFromBg = true;
        }
    }

    /* access modifiers changed from: private */
    public void processFrameFinish() {
        this.numFramesProcessing--;
        if (this.queuedUpFrame) {
            this.queuedUpFrame = false;
            new Thread(new Runnable() {
                public void run() {
                    if (CameraActivity.this.previewFrozen) {
                        CameraActivity.this.saveImageFromTextureView();
                    }
                    CameraActivity.this.updateImageIfAppropriateForce(true);
                }
            }).start();
        }
    }

    private void updateViews() {
        updateColorWindowTools();
        updateColorSelectorTools();
        updateHueSliderColor();
        updateSelectColorButtonTitle();
    }

    /* access modifiers changed from: private */
    public void setColorSelector(int color) {
        String hueLabel;
        int redComponent = Color.red(color);
        int greenComponent = Color.green(color);
        int blueComponent = Color.blue(color);
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        Utilities.rgbToHSL(color, new double[3]);
        this.colorDescriptionTextView.setText(Utilities.colorDescription(color, ColorSingleton.getInstance().getColorNamePack()));
        this.selectedColorBoxView.setBackgroundColor(color);
        this.hueSeekBar.setProgress((int) hsv[0]);
        this.saturationSeekBar.setProgress((int) (hsv[1] * 100.0f));
        this.valueSeekBar.setProgress((int) (hsv[2] * 100.0f));
        this.redTextView.setText(String.format("%d", new Object[]{Integer.valueOf(redComponent)}));
        this.greenTextView.setText(String.format("%d", new Object[]{Integer.valueOf(greenComponent)}));
        this.blueTextView.setText(String.format("%d", new Object[]{Integer.valueOf(blueComponent)}));
        if (Float.isNaN(hsv[0])) {
            hueLabel = getString(C0273R.string.not_applicable);
        } else {
            hueLabel = String.format("%d", new Object[]{Integer.valueOf((int) hsv[0])});
        }
        this.hTextView.setText(hueLabel);
        this.sTextView.setText(String.format("%d", new Object[]{Integer.valueOf((int) (hsv[1] * 100.0f))}));
        this.vTextView.setText(String.format("%d", new Object[]{Integer.valueOf((int) (hsv[2] * 100.0f))}));
    }

    /* access modifiers changed from: private */
    public void updateColorSelectorTools() {
        int advancedToolsVisibilityNew;
        boolean showColorValues;
        int devColorsVisibilityNew;
        boolean showAdvancedTools = this.colorSelectorToolsToggleButton.isChecked();
        if (showAdvancedTools) {
            advancedToolsVisibilityNew = 0;
        } else {
            advancedToolsVisibilityNew = 8;
        }
        this.saturationSeekBar.setVisibility(advancedToolsVisibilityNew);
        this.valueSeekBar.setVisibility(advancedToolsVisibilityNew);
        this.saturationTextView.setVisibility(advancedToolsVisibilityNew);
        this.valueTextView.setVisibility(advancedToolsVisibilityNew);
        ColorSingleton colorSingleton = ColorSingleton.getInstance();
        if (!showAdvancedTools || !colorSingleton.getColorSelectorDeveloperColors()) {
            showColorValues = false;
        } else {
            showColorValues = true;
        }
        if (showColorValues) {
            devColorsVisibilityNew = 0;
        } else {
            devColorsVisibilityNew = 8;
        }
        this.devColorsRelativeLayout.setVisibility(devColorsVisibilityNew);
    }

    /* access modifiers changed from: private */
    public void updateColorWindowTools() {
        int visibility = 0;
        if (!(this.colorWindowToolsToggleButton.getVisibility() == 0 && this.colorWindowToolsToggleButton.isChecked())) {
            visibility = 8;
        }
        this.hueWidthSelectorSeekBar.setVisibility(visibility);
        this.customHighlightColorToggleButton.setVisibility(visibility);
        this.fadeHighlightToggleButton.setVisibility(visibility);
        this.hueRangeTextView.setVisibility(visibility);
    }

    private void updateSelectColorButtonTitle() {
        String colorName = Utilities.hueName((double) this.hueSelectorSeekBar.getProgress(), ColorSingleton.getInstance().getColorNamePack());
        if (this.selectColorToggleButton.getText() != colorName) {
            this.selectColorToggleButton.setAllText(colorName);
        }
    }

    private void updateHueSliderColor() {
        if (this.showFilterToggleButton.isChecked()) {
            setSeekBarThumbTint(this.hueSelectorSeekBar, Color.HSVToColor(new float[]{(float) this.hueSelectorSeekBar.getProgress(), 1.0f, 0.75f}));
            if (!this.hideButton.isChecked()) {
                this.hueSelectorSeekBar.setVisibility(0);
            }
            this.hueSelectorBarView.setBackgroundColor(getResources().getColor(C0273R.color.slider_gray));
        } else {
            this.hueSelectorSeekBar.setVisibility(8);
            this.hueSelectorBarView.setBackgroundColor(getResources().getColor(C0273R.color.slider_light_gray));
        }
        if (!this.showFilterToggleButton.isChecked() || this.hideButton.isChecked()) {
            this.selectColorToggleButton.setVisibility(8);
        } else {
            this.selectColorToggleButton.setVisibility(0);
        }
    }

    private void setSeekBarThumbTint(SeekBar seekBar, int color) {
        ShapeDrawable thumb = new ShapeDrawable(new OvalShape());
        int thumbSizePx = Utilities.pxFromDp(this, (double) getResources().getInteger(C0273R.integer.thumb_size_dp));
        thumb.setIntrinsicHeight(thumbSizePx);
        thumb.setIntrinsicWidth(thumbSizePx);
        thumb.getPaint().setColor(color);
        seekBar.setThumb(thumb);
    }

    /* access modifiers changed from: private */
    public void updateHueSelectorCallback(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        if (hsv[1] > 0.0f) {
            this.hueSelectorSeekBar.setProgress((int) hsv[0]);
            updateSliderAndImage();
        }
    }

    private void setColorSelectorToolsButtonListener() {
        this.colorSelectorToolsToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CameraActivity.this.updateColorSelectorTools();
            }
        });
    }

    private void setColorWindowToolsButtonListener() {
        this.colorWindowToolsToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CameraActivity.this.updateColorWindowTools();
            }
        });
    }

    private void setColorModeButtonListener() {
        this.colorModeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (CameraActivity.this.colorMode == ColorMode.ColorWindow) {
                    ColorMode unused = CameraActivity.this.colorMode = ColorMode.ColorSelector;
                } else if (CameraActivity.this.colorMode == ColorMode.ColorSelector) {
                    ColorMode unused2 = CameraActivity.this.colorMode = ColorMode.ColorWindow;
                }
                CameraActivity.this.updateColorMode();
                CameraActivity.this.updateImageIfAppropriate();
            }
        });
    }

    private void setHueWidthSelectorSeekBarListener() {
        this.hueWidthSelectorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                CameraActivity.this.updateImageIfAppropriate();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void setHueSelectorSeekBarListener() {
        this.hueSelectorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                CameraActivity.this.updateSliderAndImage();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateSliderAndImage() {
        updateSelectColorButtonTitle();
        updateImageIfAppropriate();
        updateHueSliderColor();
    }

    private void setCustomHighlightColorButtonListener() {
        this.customHighlightColorToggleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CameraActivity.this.updateImageIfAppropriate();
            }
        });
    }

    private void setFadeHighlightButtonListener() {
        this.fadeHighlightToggleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CameraActivity.this.updateImageIfAppropriate();
            }
        });
    }

    private void setFreezeButtonListener() {
        this.freezeCameraButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        boolean unused = CameraActivity.this.previewFrozen = CameraActivity.this.freezeCameraButton.isChecked();
                        CameraActivity.this.cbpCameraPreview.setFreezePreview(CameraActivity.this.previewFrozen);
                        if (CameraActivity.this.colorMode == ColorMode.ColorWindow) {
                            boolean unused2 = CameraActivity.this.needsToSaveThisFreeze = CameraActivity.this.previewFrozen && (CameraActivity.this.shiftSpectrumToggleButton.isChecked() || CameraActivity.this.actualColorsToggleButton.isChecked());
                            if (CameraActivity.this.showStripeToggleButton.isChecked() || CameraActivity.this.showFilterToggleButton.isChecked()) {
                                CameraActivity.this.updateImageIfAppropriateForce(true);
                            } else if (CameraActivity.this.actualColorsToggleButton.isChecked()) {
                                CameraActivity.this.saveImageFromTextureView();
                            }
                        } else if (CameraActivity.this.colorMode == ColorMode.ColorSelector) {
                            boolean unused3 = CameraActivity.this.needsToSaveThisFreeze = CameraActivity.this.previewFrozen;
                            CameraActivity.this.saveImageFromTextureView();
                        }
                        if (!CameraActivity.this.previewFrozen) {
                            CameraActivity.this.resetColorSelectorCrosshair();
                        }
                    }
                }).start();
            }
        });
    }

    /* access modifiers changed from: private */
    public void saveImageFromTextureView() {
        if (!this.shiftSpectrumToggleButton.isChecked() || this.colorMode == ColorMode.ColorSelector) {
            int freezeQualityFactor = ColorSingleton.getInstance().getCameraFreezeQuality();
            this.lastUsedCameraQualityFactor = freezeQualityFactor;
            this.inputImageBitmap = getPreviewTextureViewBmp(false, freezeQualityFactor);
            if (this.mCamera != null) {
                processFrameColorSelector((byte[]) null, this.mCamera, true);
            }
        }
    }

    public void textureViewOnGlobalLayout() {
        if (this.previewFrozen) {
            new Thread(new Runnable() {
                public void run() {
                    if (CameraActivity.this.needsToSaveThisFreeze) {
                        CameraActivity.this.saveImageFromTextureView();
                        boolean unused = CameraActivity.this.needsToSaveThisFreeze = false;
                        CameraActivity.this.updateImageIfAppropriateForce(true);
                    } else if (CameraActivity.this.previewFrozen) {
                        CameraActivity.this.saveImageFromTextureView();
                    }
                }
            }).start();
        }
    }

    private void setGlobalFilterButtonsListeners() {
        View.OnClickListener colorWindowToggleButtonListener = new View.OnClickListener() {
            public void onClick(View v) {
                CameraActivity.this.toggleGlobalFilterButton((ToggleButton) v);
            }
        };
        Iterator<CameraToggleButton> it = new ArrayList<>(Arrays.asList(new CameraToggleButton[]{this.actualColorsToggleButton, this.shiftSpectrumToggleButton, this.showStripeToggleButton, this.showFilterToggleButton})).iterator();
        while (it.hasNext()) {
            CameraToggleButton toggleButton = it.next();
            toggleButton.setOnClickListener(colorWindowToggleButtonListener);
            toggleButton.handleToggle = false;
        }
    }

    private void setHideButtonListener() {
        this.hideButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CameraActivity.this.hideButton.setChecked(!CameraActivity.this.hideButton.isChecked());
                CameraActivity.this.pressHideButton();
            }
        });
        this.hideButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    CameraActivity.this.hideButton.setAlpha(0.5f);
                    return false;
                } else if (event.getAction() != 1) {
                    return false;
                } else {
                    CameraActivity.this.hideButton.setAlpha(1.0f);
                    return false;
                }
            }
        });
    }

    private void setInfoButtonListener() {
        this.infoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CameraActivity.this.pressInfoButton();
            }
        });
        this.infoButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    Utilities.setTextShadow(CameraActivity.this.getApplicationContext(), CameraActivity.this.infoButton);
                    CameraActivity.this.infoButton.setTextColor(CameraActivity.this.getResources().getColor(C0273R.color.info_button_pressed_gray));
                    return false;
                } else if (event.getAction() != 1) {
                    return false;
                } else {
                    Utilities.clearTextShadow(CameraActivity.this.infoButton);
                    CameraActivity.this.infoButton.setTextColor(CameraActivity.this.getResources().getColor(C0273R.color.white));
                    return false;
                }
            }
        });
    }

    private void setPreviewTextureViewTouchListener() {
        this.previewTextureView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                boolean isEventRelevant;
                boolean isCoordinateOld = false;
                if (event.getAction() == 0 || event.getAction() == 2 || event.getAction() == 1) {
                    isEventRelevant = true;
                } else {
                    isEventRelevant = false;
                }
                if (isEventRelevant) {
                    float coordinateXRatio = event.getX() / ((float) CameraActivity.this.previewTextureView.getWidth());
                    float coordinateYRatio = event.getY() / ((float) CameraActivity.this.previewTextureView.getHeight());
                    if (CameraActivity.this.colorMode == ColorMode.ColorWindow) {
                        if (CameraActivity.this.selectColorToggleButton.isChecked() && CameraActivity.this.showFilterToggleButton.isChecked()) {
                            if (((double) coordinateXRatio) == CameraActivity.this.selectColorScreenCoordinatePortionX && ((double) coordinateYRatio) == CameraActivity.this.selectColorScreenCoordinatePortionY) {
                                isCoordinateOld = true;
                            }
                            if (!isCoordinateOld) {
                                double unused = CameraActivity.this.selectColorScreenCoordinatePortionX = (double) coordinateXRatio;
                                double unused2 = CameraActivity.this.selectColorScreenCoordinatePortionY = (double) coordinateYRatio;
                                CameraActivity.this.processFrameColorSelector((byte[]) null, CameraActivity.this.mCamera, true, ColorSelectorCallback.UpdateHueSelector);
                            }
                        }
                    } else if (CameraActivity.this.colorMode == ColorMode.ColorSelector && CameraActivity.this.previewFrozen) {
                        if (((double) coordinateXRatio) == CameraActivity.this.crosshairScreenCoordinatePortionX && ((double) coordinateYRatio) == CameraActivity.this.crosshairScreenCoordinatePortionY) {
                            isCoordinateOld = true;
                        }
                        double unused3 = CameraActivity.this.crosshairScreenCoordinatePortionX = (double) coordinateXRatio;
                        double unused4 = CameraActivity.this.crosshairScreenCoordinatePortionY = (double) coordinateYRatio;
                        if (!isCoordinateOld) {
                            CameraActivity.this.updateCrosshairImage();
                            if (CameraActivity.this.previewFrozen) {
                                CameraActivity.this.processFrameColorSelector((byte[]) null, CameraActivity.this.mCamera, true);
                            }
                        }
                    }
                }
                return true;
            }
        });
    }

    private void setPreviewImageViewLayoutListener() {
        this.previewImageView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                int width = CameraActivity.this.previewImageView.getWidth();
                int height = CameraActivity.this.previewImageView.getHeight();
                if (CameraActivity.this.previewInputImageBitmap.getWidth() != width || CameraActivity.this.previewInputImageBitmap.getHeight() != height) {
                    Bitmap unused = CameraActivity.this.previewInputImageBitmap = Utilities.getBitmapFromNinepatch(C0273R.C0274drawable.rainbow, width, height, CameraActivity.this.getBaseContext());
                    CameraFrameProcessOptions unused2 = CameraActivity.this.lastUsedOptions = null;
                }
            }
        });
    }

    private void setCrosshairImageViewLayoutListener() {
        this.crosshairImageView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                CameraActivity.this.updateCrosshairImage();
            }
        });
    }

    /* access modifiers changed from: private */
    public void toggleGlobalFilterButton(ToggleButton clickedButton) {
        boolean showFilterViews = true;
        if (!clickedButton.isChecked()) {
            ToggleButton originallyCheckedButton = null;
            Iterator<ToggleButton> it = new ArrayList<>(Arrays.asList(new CameraToggleButton[]{this.actualColorsToggleButton, this.shiftSpectrumToggleButton, this.showStripeToggleButton, this.showFilterToggleButton})).iterator();
            while (it.hasNext()) {
                ToggleButton toggleButton = it.next();
                if (toggleButton.isChecked()) {
                    originallyCheckedButton = toggleButton;
                }
                if (toggleButton == clickedButton) {
                    toggleButton.setChecked(true);
                } else {
                    toggleButton.setChecked(false);
                }
            }
            if (clickedButton != this.showFilterToggleButton) {
                showFilterViews = false;
            }
            this.hueSelectorSeekBar.setEnabled(showFilterViews);
            this.hueWidthSelectorSeekBar.setEnabled(showFilterViews);
            this.customHighlightColorToggleButton.setEnabled(showFilterViews);
            this.fadeHighlightToggleButton.setEnabled(showFilterViews);
            if (showFilterViews) {
                this.colorWindowToolsToggleButton.setVisibility(0);
            } else {
                this.colorWindowToolsToggleButton.setVisibility(8);
            }
            if (clickedButton == this.shiftSpectrumToggleButton) {
                updateDaltonizationType(ColorSingleton.getInstance().getColorBlindnessType());
            } else {
                updateDaltonizationType(ColorBlindnessType.Normal);
                if (originallyCheckedButton == this.shiftSpectrumToggleButton || originallyCheckedButton == this.actualColorsToggleButton) {
                    smoothTransitionToImageView();
                }
            }
            setShowActualColors();
            updateHueSliderColor();
            updateColorWindowTools();
            updateImageIfAppropriate();
        }
    }

    private void smoothTransitionToImageView() {
        if (this.showStripeToggleButton.isChecked() || this.showFilterToggleButton.isChecked()) {
            this.cbpCameraPreview.skipNextFrame();
            this.waitToProcess = true;
            this.outputImageView.setImageBitmap(getPreviewTextureViewBmp(true, ColorSingleton.getInstance().getCameraFreezeQuality()));
        }
    }

    /* access modifiers changed from: private */
    public void pressHideButton() {
        boolean hidden = this.hideButton.isChecked();
        int visibility = 0;
        int overlayColor = getResources().getColor(C0273R.color.black_overlay);
        if (hidden) {
            visibility = 4;
            overlayColor = getResources().getColor(C0273R.color.transparent);
        }
        this.secondRowColorSelectorLinearLayout.setBackgroundColor(overlayColor);
        this.bottomBlackBarColorSelectorView.setBackgroundColor(overlayColor);
        this.bottomBlackBarColorWindowView.setBackgroundColor(overlayColor);
        if (this.colorMode == ColorMode.ColorSelector) {
            this.topBlackBarColorSelectorView.setVisibility(visibility);
            this.colorSelectorToolsToggleButton.setVisibility(visibility);
        } else if (this.colorMode == ColorMode.ColorWindow) {
            this.topBlackBarColorWindowView.setVisibility(visibility);
        }
        this.colorModeButton.setVisibility(visibility);
        this.freezeCameraButton.setVisibility(visibility);
        this.infoButton.setVisibility(visibility);
        this.hueSelectorBarView.setVisibility(visibility);
        this.actualColorsToggleButton.setVisibility(visibility);
        this.showStripeToggleButton.setVisibility(visibility);
        this.showFilterToggleButton.setVisibility(visibility);
        this.shiftSpectrumToggleButton.setVisibility(visibility);
        this.previewImageView.setVisibility(visibility);
        this.previewInputImageView.setVisibility(visibility);
        if (this.colorMode == ColorMode.ColorWindow && this.showFilterToggleButton.isChecked()) {
            this.hueSelectorSeekBar.setVisibility(visibility);
            this.selectColorToggleButton.setVisibility(visibility);
        }
        if (this.colorMode == ColorMode.ColorWindow) {
            if (hidden) {
                this.colorWindowToolsButtonSelected = this.colorWindowToolsToggleButton.isChecked();
                this.colorWindowToolsButtonVisibility = this.colorWindowToolsToggleButton.getVisibility();
                this.colorWindowToolsToggleButton.setChecked(false);
                this.colorWindowToolsToggleButton.setVisibility(8);
            } else {
                this.colorWindowToolsToggleButton.setChecked(this.colorWindowToolsButtonSelected);
                this.colorWindowToolsToggleButton.setVisibility(this.colorWindowToolsButtonVisibility);
            }
            updateColorWindowTools();
        }
    }

    /* access modifiers changed from: private */
    public void pressInfoButton() {
        startActivity(new Intent(this, CBPPreferencesActivity.class));
    }

    private void setShowActualColors() {
        if (this.actualColorsToggleButton.isChecked()) {
            this.outputImageView.setVisibility(8);
            this.previewImageView.setImageBitmap(this.previewInputImageBitmap);
            this.previewImageView.setColorFilter(Utilities.daltonizationFilter(ColorBlindnessType.Normal));
            return;
        }
        updateOutputImageViewVisibilityForColorWindow();
    }

    private void updateOutputImageViewVisibilityForColorWindow() {
        if (this.shiftSpectrumToggleButton.isChecked()) {
            this.outputImageView.setVisibility(8);
        } else {
            this.outputImageView.setVisibility(0);
        }
    }

    private void updateDaltonizationType(ColorBlindnessType colorBlindnessType) {
        if (this.cbpCameraPreview != null) {
            this.cbpCameraPreview.setDaltonizationType(colorBlindnessType);
        }
        if (this.shiftSpectrumToggleButton.isChecked() || this.actualColorsToggleButton.isChecked()) {
            ColorMatrixColorFilter colorFilter = Utilities.daltonizationFilter(colorBlindnessType);
            this.previewImageView.setImageBitmap(this.previewInputImageBitmap);
            this.previewImageView.setColorFilter(colorFilter);
        }
    }

    /* access modifiers changed from: private */
    public void updateColorMode() {
        int colorWindowVisibility;
        int colorSelectorVisibility;
        if (this.colorMode == ColorMode.ColorWindow) {
            colorWindowVisibility = 0;
        } else {
            colorWindowVisibility = 8;
        }
        if (this.colorMode == ColorMode.ColorSelector) {
            colorSelectorVisibility = 0;
        } else {
            colorSelectorVisibility = 8;
        }
        this.colorWindowContainerRelativeLayout.setVisibility(colorWindowVisibility);
        this.colorSelectorContainerRelativeLayout.setVisibility(colorSelectorVisibility);
        this.topBlackBarColorWindowView.setVisibility(colorWindowVisibility);
        this.secondRowColorWindowLinearLayout.setVisibility(colorWindowVisibility);
        this.topBlackBarColorSelectorView.setVisibility(colorSelectorVisibility);
        this.secondRowColorSelectorLinearLayout.setVisibility(colorSelectorVisibility);
        this.colorSelectorToolsToggleButton.setVisibility(colorSelectorVisibility);
        if (this.colorMode != ColorMode.ColorWindow || !this.showFilterToggleButton.isChecked()) {
            this.colorWindowToolsToggleButton.setVisibility(8);
        } else {
            this.colorWindowToolsToggleButton.setVisibility(0);
        }
        if (this.colorMode == ColorMode.ColorWindow) {
            this.colorModeButton.setText(getString(C0273R.string.filtering_colors));
            if (this.shiftSpectrumToggleButton.isChecked()) {
                updateDaltonizationType(ColorSingleton.getInstance().getColorBlindnessType());
            } else if (!this.previewFrozen) {
                smoothTransitionToImageView();
            }
            if (this.previewFrozen) {
                this.showImageViewNext = true;
            } else {
                updateOutputImageViewVisibilityForColorWindow();
                setShowActualColors();
            }
            this.crosshairImageView.setVisibility(8);
        } else if (this.colorMode == ColorMode.ColorSelector) {
            this.colorModeButton.setText(getString(C0273R.string.inspecting_color));
            if (this.cbpCameraPreview != null) {
                this.cbpCameraPreview.setDaltonizationType(ColorBlindnessType.Normal);
            }
            if (this.previewFrozen) {
                processFrameColorSelector((byte[]) null, this.mCamera, true);
            }
            this.outputImageView.setVisibility(8);
            this.crosshairImageView.setVisibility(0);
        }
    }

    /* access modifiers changed from: private */
    public void updateImageIfAppropriate() {
        updateImageIfAppropriateForce(false);
    }

    /* access modifiers changed from: private */
    public void updateImageIfAppropriateForce(boolean forceProcess) {
        if (this.previewFrozen && this.colorMode == ColorMode.ColorWindow) {
            processFrame(forceProcess, true);
        }
    }
}
