package com.biz.widget;

import com.biz.util.DrawableHelper;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Supplier;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.RoundedCornersDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.interfaces.SimpleDraweeControllerBuilder;
import com.facebook.drawee.view.AspectRatioMeasure;
import com.facebook.drawee.view.DraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;

/**
 * Created by johnzheng on 5/6/15.
 */
public class CustomDraweeView extends DraweeView<GenericDraweeHierarchy> {

    private float mAspectRatio = 0.0F;
    private final AspectRatioMeasure.Spec mMeasureSpec = new AspectRatioMeasure.Spec();
    private static Supplier<? extends SimpleDraweeControllerBuilder> sDraweeControllerBuilderSupplier;
    private SimpleDraweeControllerBuilder mSimpleDraweeControllerBuilder;
    private DisplayMetrics dm;
    private int height = 0, width = 0;
    private Drawable overlay;

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setSize(int width, int height) {
        this.height = height;
        this.width = width;
    }

    public static void initialize(Supplier<? extends SimpleDraweeControllerBuilder> draweeControllerBuilderSupplier) {
        sDraweeControllerBuilderSupplier = draweeControllerBuilderSupplier;
    }

    public static void shutDown() {
        sDraweeControllerBuilderSupplier = null;
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (!this.isInEditMode()) {
            inflateHierarchy(context, attrs);

            dm = context.getResources().getDisplayMetrics();
            Preconditions.checkNotNull(sDraweeControllerBuilderSupplier, "SimpleDraweeView was not initialized!");
            this.mSimpleDraweeControllerBuilder = sDraweeControllerBuilderSupplier.get();
            if (this.mSimpleDraweeControllerBuilder instanceof PipelineDraweeControllerBuilder) {
                PipelineDraweeControllerBuilder builder =
                        (PipelineDraweeControllerBuilder) mSimpleDraweeControllerBuilder;
                builder.setAutoPlayAnimations(true);
            }
        }
    }

    protected SimpleDraweeControllerBuilder getControllerBuilder() {
        return this.mSimpleDraweeControllerBuilder;
    }

    public void setImageURI(Uri uri) {
        this.setImageURI(uri, (Object) null);
    }

    public void setImageURI(Uri uri, @Nullable Object callerContext) {
        if (!hasHierarchy()) {
            inflateHierarchy(getContext());
        } else {
            if (overlayId > 0) {
                getHierarchy().setControllerOverlay(DrawableHelper.getDrawable(getContext(), overlayId));
            } else if (overlay != null) {
                getHierarchy().setControllerOverlay(overlay);
            }
        }

        DraweeController controller;
        int width = getLayoutWidth(), height = getLayoutHeight();
        if (this.mSimpleDraweeControllerBuilder instanceof PipelineDraweeControllerBuilder
                && width > 0 && height > 0) {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(width, height))
                    .setAutoRotateEnabled(true)
                    .setLocalThumbnailPreviewsEnabled(true)
                    .setProgressiveRenderingEnabled(true)
                    .build();
            PipelineDraweeControllerBuilder builder =
                    (PipelineDraweeControllerBuilder) mSimpleDraweeControllerBuilder;
            controller = builder
                    .setCallerContext(callerContext)
                    .setOldController(this.getController())
                    .setImageRequest(request)
                    .build();

        } else
            controller = this.mSimpleDraweeControllerBuilder
                    .setCallerContext(callerContext)
                    .setUri(uri)
                    .setOldController(this.getController())
                    .build();

        this.setController(controller);

    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        //super.setScaleType(scaleType);
    }


    public void setController(@Nullable DraweeController draweeController) {
        if (!hasHierarchy()) {
            inflateHierarchy(getContext());
        }
        super.setController(draweeController);
    }

    public CustomDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context);
        this.init(context, null, 0, 0);
        this.setHierarchy(hierarchy);
    }

    public CustomDraweeView(Context context) {
        super(context);
        this.init(context, null, 0, 0);
    }

    public CustomDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs, 0, 0);
    }

    public CustomDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(context, attrs, defStyle, 0);
    }

    private void inflateHierarchy(Context context) {
        Resources resources = context.getResources();
        GenericDraweeHierarchyBuilder builder1 = new GenericDraweeHierarchyBuilder(resources);
        builder1.setFadeDuration(fadeDuration);
        if (placeholderId > 0) {
            builder1.setPlaceholderImage(DrawableHelper.getDrawable(context, placeholderId), placeholderScaleType);
        }

        if (retryImageId > 0) {
            builder1.setRetryImage(DrawableHelper.getDrawable(context, retryImageId), retryImageScaleType);
        }

        if (failureImageId > 0) {
            RoundedCornersDrawable drawable =
                    new RoundedCornersDrawable(DrawableHelper.getDrawable(context, failureImageId));
            drawable.setRadius(roundedCornerRadius);
            drawable.setType(RoundedCornersDrawable.Type.CLIPPING);
            int overlayColor = roundWithOverlayColor > 0 ? roundWithOverlayColor : Color.TRANSPARENT;
            drawable.setOverlayColor(overlayColor);
            int color = roundingBorderColor > 0 ? roundingBorderColor : Color.TRANSPARENT;
            int width = roundingBorderWidth > 0 ? roundingBorderWidth : 0;
            drawable.setBorder(color, width);
            builder1.setFailureImage(drawable, failureImageScaleType);
        }

        if (progressBarId > 0) {
            Object roundingParams = DrawableHelper.getDrawable(context, progressBarId);
            if (progressBarAutoRotateInterval > 0) {
                roundingParams = new AutoRotateDrawable((Drawable) roundingParams, progressBarAutoRotateInterval);
            }

            builder1.setProgressBarImage((Drawable) roundingParams, progressBarScaleType);
        }

        if (backgroundId > 0) {
            builder1.setBackground(DrawableHelper.getDrawable(context, backgroundId));
        }

        if (overlayId > 0) {
            builder1.setOverlay(DrawableHelper.getDrawable(context, overlayId));
        } else if (overlay != null) {
            builder1.setOverlay(overlay);
        }

        if (pressedStateOverlayId > 0) {
            builder1.setPressedStateOverlay(DrawableHelper.getDrawable(context, pressedStateOverlayId));
        }

        builder1.setActualImageScaleType(actualImageScaleType);
        if (roundAsCircle || roundedCornerRadius > 0) {
            RoundingParams roundingParams1 = new RoundingParams();
            roundingParams1.setRoundAsCircle(roundAsCircle);
            if (roundedCornerRadius > 0) {
                roundingParams1.setCornersRadii(roundTopLeft ? (float) roundedCornerRadius : 0.0F, roundTopRight ? (float) roundedCornerRadius : 0.0F, roundBottomRight ? (float) roundedCornerRadius : 0.0F, roundBottomLeft ? (float) roundedCornerRadius : 0.0F);
            }

            if (roundWithOverlayColor != 0) {
                roundingParams1.setOverlayColor(roundWithOverlayColor);
            }

            if (roundingBorderColor != 0 && roundingBorderWidth > 0) {
                roundingParams1.setBorder(roundingBorderColor, (float) roundingBorderWidth);
            }

            builder1.setRoundingParams(roundingParams1);
        }

        this.setHierarchy(builder1.build());
    }


    public void setAspectRatio(float aspectRatio) {
        if (aspectRatio != this.mAspectRatio) {
            this.mAspectRatio = aspectRatio;
            this.requestLayout();
        }
    }

    public float getAspectRatio() {
        return this.mAspectRatio;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mMeasureSpec.width = widthMeasureSpec;
        this.mMeasureSpec.height = heightMeasureSpec;
        AspectRatioMeasure.updateMeasureSpec(this.mMeasureSpec, this.mAspectRatio, this.getLayoutParams(), this.getPaddingLeft() + this.getPaddingRight(), this.getPaddingTop() + this.getPaddingBottom());
        super.onMeasure(this.mMeasureSpec.width, this.mMeasureSpec.height);
    }


    public void setProgressBarAutoRotateInterval(int progressBarAutoRotateInterval) {
        this.progressBarAutoRotateInterval = progressBarAutoRotateInterval;
    }

    public void setRoundingBorderColor(int roundingBorderColor) {
        this.roundingBorderColor = roundingBorderColor;
    }

    public void setRoundingBorderWidth(int roundingBorderWidth) {
        this.roundingBorderWidth = roundingBorderWidth;
    }

    public void setRoundWithOverlayColor(int roundWithOverlayColor) {
        this.roundWithOverlayColor = roundWithOverlayColor;
    }

    public void setRoundBottomLeft(boolean roundBottomLeft) {
        this.roundBottomLeft = roundBottomLeft;
    }

    public void setRoundBottomRight(boolean roundBottomRight) {
        this.roundBottomRight = roundBottomRight;
    }

    public void setRoundTopRight(boolean roundTopRight) {
        this.roundTopRight = roundTopRight;
    }

    public void setRoundTopLeft(boolean roundTopLeft) {
        this.roundTopLeft = roundTopLeft;
    }

    public void setRoundedCornerRadius(int roundedCornerRadius) {
        this.roundedCornerRadius = roundedCornerRadius;
    }

    public void setRoundAsCircle(boolean roundAsCircle) {
        this.roundAsCircle = roundAsCircle;
    }

    public void setPressedStateOverlayId(int pressedStateOverlayId) {
        this.pressedStateOverlayId = pressedStateOverlayId;
    }

    public void setOverlayId(int overlayId) {
        this.overlayId = overlayId;
    }


    public void setOverlay(Drawable overlay) {
        this.overlay = overlay;
    }

    public void setBackgroundId(int backgroundId) {
        this.backgroundId = backgroundId;
    }

    public void setActualImageScaleType(ScalingUtils.ScaleType actualImageScaleType) {
        this.actualImageScaleType = actualImageScaleType;
    }

    public void setProgressBarScaleType(ScalingUtils.ScaleType progressBarScaleType) {
        this.progressBarScaleType = progressBarScaleType;
    }

    public void setProgressBarId(int progressBarId) {
        this.progressBarId = progressBarId;
    }

    public void setFailureImageScaleType(ScalingUtils.ScaleType failureImageScaleType) {
        this.failureImageScaleType = failureImageScaleType;
    }

    public void setFailureImageId(int failureImageId) {
        this.failureImageId = failureImageId;
    }

    public void setRetryImageId(int retryImageId) {
        this.retryImageId = retryImageId;
    }

    public void setPlaceholderId(int placeholderId) {
        this.placeholderId = placeholderId;
    }

    public void setFadeDuration(int fadeDuration) {
        this.fadeDuration = fadeDuration;
    }

    public int getFilterColor() {
        return filterColor;
    }

    public void setFilterColor(int filterColor) {
        this.filterColor = filterColor;
    }

    public void setPlaceholderScaleType(ScalingUtils.ScaleType placeholderScaleType) {
        this.placeholderScaleType = placeholderScaleType;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Width is defined by target {@link android.view.View view} parameters, configuration
     * parameters or device display dimensions.<br />
     * Size computing algorithm (go by steps until get non-zero value):<br />
     * 1) Get the actual drawn <b>getWidth()</b> of the View<br />
     * 2) Get <b>layout_width</b>
     */
    public int getLayoutWidth() {
        final ViewGroup.LayoutParams params = getLayoutParams();
        int width = dm.widthPixels;
        if (params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
            width = getWidth(); // Get actual image width
        }
        if (width <= 0 && params != null) width = params.width; // Get layout width parameter
        if (width <= 0) {
            width = getImageViewFieldValue(this, "mMaxWidth"); // Check maxWidth parameter
        }
        if (width <= 0) width = this.width > 0 ? this.width : dm.widthPixels;
        if (this.width > 0) width = this.width;
        return width;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Height is defined by target {@link android.view.View view} parameters, configuration
     * parameters or device display dimensions.<br />
     * Size computing algorithm (go by steps until get non-zero value):<br />
     * 1) Get the actual drawn <b>getHeight()</b> of the View<br />
     * 2) Get <b>layout_height</b>
     */
    public int getLayoutHeight() {
        final ViewGroup.LayoutParams params = getLayoutParams();
        int height = dm.heightPixels;
        if (params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
            height = getHeight(); // Get actual image height
        }
        if (height <= 0 && params != null) height = params.height; // Get layout height parameter
        if (height <= 0) {
            height = getImageViewFieldValue(this, "mMaxHeight"); // Check maxHeight parameter
        }
        if (height <= 0) height = this.height > 0 ? this.height : dm.heightPixels;
        if (this.height > 0) height = this.height;
        return height;
    }

    private static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = (Integer) field.get(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {
        }
        return value;
    }


    private int fadeDuration = 200;
    private int placeholderId = 0;
    private ScalingUtils.ScaleType placeholderScaleType = ScalingUtils.ScaleType.FIT_CENTER;
    private int retryImageId = 0;
    private ScalingUtils.ScaleType retryImageScaleType = ScalingUtils.ScaleType.FIT_CENTER;
    private int failureImageId = 0;
    private ScalingUtils.ScaleType failureImageScaleType = ScalingUtils.ScaleType.FIT_CENTER;
    private int progressBarId = 0;
    private ScalingUtils.ScaleType progressBarScaleType = GenericDraweeHierarchyBuilder.DEFAULT_SCALE_TYPE;
    private ScalingUtils.ScaleType actualImageScaleType = GenericDraweeHierarchyBuilder.DEFAULT_ACTUAL_IMAGE_SCALE_TYPE;
    private int backgroundId = 0;
    private int overlayId = 0;
    private int pressedStateOverlayId = 0;
    private boolean roundAsCircle = false;
    private int roundedCornerRadius = 0;
    private boolean roundTopLeft = true;
    private boolean roundTopRight = true;
    private boolean roundBottomRight = true;
    private boolean roundBottomLeft = true;
    private int roundWithOverlayColor = 0;
    private int roundingBorderWidth = 0;
    private int roundingBorderColor = 0;
    private int progressBarAutoRotateInterval = 0;
    private int filterColor = 0;


    private void inflateHierarchy(Context context, @Nullable AttributeSet attrs) {
        Resources resources = context.getResources();
        if (attrs != null) {
            TypedArray gdhAttrs = context.obtainStyledAttributes(
                    attrs,
                    com.facebook.drawee.R.styleable.GenericDraweeHierarchy);
            try {
                // fade duration
                fadeDuration = gdhAttrs.getInt(
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_fadeDuration,
                        fadeDuration);

                // aspect ratio
                mAspectRatio = gdhAttrs.getFloat(
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_viewAspectRatio,
                        mAspectRatio);

                // placeholder image
                placeholderId = gdhAttrs.getResourceId(
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_placeholderImage,
                        placeholderId);
                // placeholder image scale type
                placeholderScaleType = getScaleTypeFromXml(
                        gdhAttrs,
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_placeholderImageScaleType,
                        placeholderScaleType);

                // retry image
                retryImageId = gdhAttrs.getResourceId(
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_retryImage,
                        retryImageId);
                // retry image scale type
                retryImageScaleType = getScaleTypeFromXml(
                        gdhAttrs,
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_retryImageScaleType,
                        retryImageScaleType);

                // failure image
                failureImageId = gdhAttrs.getResourceId(
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_failureImage,
                        failureImageId);
                // failure image scale type
                failureImageScaleType = getScaleTypeFromXml(
                        gdhAttrs,
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_failureImageScaleType,
                        failureImageScaleType);

                // progress bar image
                progressBarId = gdhAttrs.getResourceId(
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_progressBarImage,
                        progressBarId);
                // progress bar image scale type
                progressBarScaleType = getScaleTypeFromXml(
                        gdhAttrs,
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_progressBarImageScaleType,
                        progressBarScaleType);
                // progress bar auto rotate interval
                progressBarAutoRotateInterval = gdhAttrs.getInteger(
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_progressBarAutoRotateInterval,
                        0);

                // actual image scale type
                actualImageScaleType = getScaleTypeFromXml(
                        gdhAttrs,
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_actualImageScaleType,
                        actualImageScaleType);

                // background
                backgroundId = gdhAttrs.getResourceId(
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_backgroundImage,
                        backgroundId);

                // overlay
                overlayId = gdhAttrs.getResourceId(
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_overlayImage,
                        overlayId);

                // pressedState overlay
                pressedStateOverlayId = gdhAttrs.getResourceId(
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_pressedStateOverlayImage,
                        pressedStateOverlayId);

                // rounding parameters
                roundAsCircle = gdhAttrs.getBoolean(
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_roundAsCircle,
                        roundAsCircle);
                roundedCornerRadius = gdhAttrs.getDimensionPixelSize(
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_roundedCornerRadius,
                        roundedCornerRadius);
                roundTopLeft = gdhAttrs.getBoolean(
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_roundTopLeft,
                        roundTopLeft);
                roundTopRight = gdhAttrs.getBoolean(
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_roundTopRight,
                        roundTopRight);
                roundBottomRight = gdhAttrs.getBoolean(
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_roundBottomRight,
                        roundBottomRight);
                roundBottomLeft = gdhAttrs.getBoolean(
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_roundBottomLeft,
                        roundBottomLeft);
                roundWithOverlayColor = gdhAttrs.getColor(
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_roundWithOverlayColor,
                        roundWithOverlayColor);
                roundingBorderWidth = gdhAttrs.getDimensionPixelSize(
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_roundingBorderWidth,
                        roundingBorderWidth);
                roundingBorderColor = gdhAttrs.getColor(
                        com.facebook.drawee.R.styleable.GenericDraweeHierarchy_roundingBorderColor,
                        roundingBorderColor);
            } finally {
                gdhAttrs.recycle();
            }
        }
    }

    private static ScalingUtils.ScaleType getScaleTypeFromXml(
            TypedArray gdhAttrs,
            int attrId, ScalingUtils.ScaleType type) {
        switch (gdhAttrs.getInt(attrId, -2)) {
            case -1: // none
                return null;
            case 0: // fitXY
                return ScalingUtils.ScaleType.FIT_XY;
            case 1: // fitStart
                return ScalingUtils.ScaleType.FIT_START;
            case 2: // fitCenter
                return ScalingUtils.ScaleType.FIT_CENTER;
            case 3: // fitEnd
                return ScalingUtils.ScaleType.FIT_END;
            case 4: // center
                return ScalingUtils.ScaleType.CENTER;
            case 5: // centerInside
                return ScalingUtils.ScaleType.CENTER_INSIDE;
            case 6: // centerCrop
                return ScalingUtils.ScaleType.CENTER_CROP;
            case 7: // focusCrop
                return ScalingUtils.ScaleType.FOCUS_CROP;
            default:
                return type;
            // this method is supposed to be called only when XML attribute is specified.
            //throw new RuntimeException("XML attribute not specified!");
        }
    }
}
