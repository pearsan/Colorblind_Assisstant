package com.colorblindpal.colorblindpal;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.Display;
import android.view.TextureView;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

public class CBPCameraPreview extends TextureView implements TextureView.SurfaceTextureListener {
    private FrameProcessor frameProcessor;
    private Camera mCamera;
    private final int mCameraId = 0;
    private Camera.PreviewCallback mPreviewCallback;
    private TextureView mTextureView;
    private byte[] previewData;
    private boolean skipNextFrame = false;
    private int surfaceRotation = -1;

    public CBPCameraPreview(Context context, Camera camera, TextureView textureView, final FrameProcessor frameProcessor2, Camera.PreviewCallback previewCallback) {
        super(context);
        this.mCamera = camera;
        this.mPreviewCallback = previewCallback;
        this.frameProcessor = frameProcessor2;
        this.mTextureView = textureView;
        this.mTextureView.setSurfaceTextureListener(this);
        this.mTextureView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                frameProcessor2.textureViewOnGlobalLayout();
            }
        });
        if (this.mTextureView.isAvailable()) {
            onSurfaceTextureAvailable(this.mTextureView.getSurfaceTexture(), this.mTextureView.getWidth(), this.mTextureView.getHeight());
        }
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        try {
            Camera.Parameters parameters = this.mCamera.getParameters();
            if (parameters.getSupportedFocusModes().contains("continuous-picture")) {
                parameters.setFocusMode("continuous-picture");
                this.mCamera.setParameters(parameters);
            }
            this.mCamera.setPreviewTexture(surface);
            this.previewData = getPreviewBuffer();
            this.mCamera.addCallbackBuffer(this.previewData);
            this.mCamera.setPreviewCallbackWithBuffer(this.mPreviewCallback);
            this.mCamera.startPreview();
        } catch (Exception e) {
        }
        setCameraDisplayOrientation();
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        if (this.skipNextFrame) {
            this.skipNextFrame = false;
        } else {
            this.frameProcessor.processFrame();
        }
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return true;
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        setCameraDisplayOrientation();
    }

    public void setDaltonizationType(ColorBlindnessType colorBlindnessType) {
        Paint paint = new Paint();
        if (colorBlindnessType != ColorBlindnessType.Normal) {
            paint.setColorFilter(Utilities.daltonizationFilter(colorBlindnessType));
        }
        this.mTextureView.setLayerType(this.mTextureView.getLayerType(), paint);
    }

    public void skipNextFrame() {
        this.skipNextFrame = true;
    }

    public void setFreezePreview(boolean freezePreview) {
        if (!freezePreview) {
            this.mCamera.addCallbackBuffer(this.previewData);
            this.mCamera.setPreviewCallbackWithBuffer(this.mPreviewCallback);
            this.mCamera.startPreview();
            return;
        }
        this.mCamera.stopPreview();
    }

    private byte[] getPreviewBuffer() {
        int bufferSize;
        Camera.Parameters parameters = this.mCamera.getParameters();
        int previewFormat = parameters.getPreviewFormat();
        Camera.Size previewSize = parameters.getPreviewSize();
        if (previewFormat != 842094169) {
            bufferSize = (int) (((float) (previewSize.width * previewSize.height)) * (((float) ImageFormat.getBitsPerPixel(previewFormat)) / 8.0f));
        } else {
            int yStride = ((int) Math.ceil(((double) previewSize.width) / 16.0d)) * 16;
            bufferSize = (yStride * previewSize.height) + (((previewSize.height * (((int) Math.ceil(((double) (yStride / 2)) / 16.0d)) * 16)) / 2) * 2);
        }
        return new byte[bufferSize];
    }

    public void previewCallbackCompleted() {
        this.mCamera.addCallbackBuffer(this.previewData);
    }

    public void displayChanged() {
        setCameraDisplayOrientation();
    }

    private void setCameraDisplayOrientation() {
        setCameraDisplayOrientation(((WindowManager) getContext().getSystemService("window")).getDefaultDisplay(), 0, this.mCamera);
    }

    public void setCameraDisplayOrientation(Display display, int cameraId, Camera camera) {
        int result;
        int rotation = display.getRotation();
        if (this.surfaceRotation != rotation) {
            this.surfaceRotation = rotation;
            int degrees = 0;
            switch (rotation) {
                case 0:
                    degrees = 0;
                    break;
                case 1:
                    degrees = 90;
                    break;
                case 2:
                    degrees = 180;
                    break;
                case 3:
                    degrees = 270;
                    break;
            }
            if (camera != null) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(cameraId, info);
                if (info.facing == 1) {
                    result = (360 - ((info.orientation + degrees) % 360)) % 360;
                } else {
                    result = ((info.orientation - degrees) + 360) % 360;
                }
                try {
                    camera.setDisplayOrientation(result);
                } catch (Exception e) {
                }
            }
        }
    }

    public Point getFrameDataCoordinate(int previewSizeWidth, int previewSizeHeight, double xRatio, double yRatio) {
        int surfaceRotation2 = this.surfaceRotation;
        if (surfaceRotation2 == 0) {
            return new Point((int) (((double) previewSizeWidth) * yRatio), (int) ((1.0d - xRatio) * ((double) previewSizeHeight)));
        }
        if (surfaceRotation2 == 1) {
            return new Point((int) (((double) previewSizeWidth) * xRatio), (int) (((double) previewSizeHeight) * yRatio));
        }
        if (surfaceRotation2 == 3) {
            return new Point((int) ((1.0d - xRatio) * ((double) previewSizeWidth)), (int) ((1.0d - yRatio) * ((double) previewSizeHeight)));
        }
        if (surfaceRotation2 == 2) {
            return new Point((int) ((1.0d - yRatio) * ((double) previewSizeWidth)), (int) (((double) previewSizeHeight) * xRatio));
        }
        return new Point((int) (0.5d * ((double) previewSizeWidth)), (int) (0.5d * ((double) previewSizeHeight)));
    }
}
